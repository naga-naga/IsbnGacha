package com.example.isbngacha

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.isbngacha.databinding.FragmentBookDetailBinding
import kotlinx.coroutines.runBlocking
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private val TAG = "SecondFragment"

    private var _binding: FragmentBookDetailBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentBookDetailBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // isbn を受け取る
        val isbn = SecondFragmentArgs.fromBundle(requireArguments()).isbn

        // 書籍情報を取得
        val client = OpenbdClient()
        var bookInfoWithoutCoverImage: Book
        val bookInfoCallback = object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                // 書籍情報を取得する時のコールバック．書影は URL が返ってくるので，別に取得する．
                bookInfoWithoutCoverImage = client.parseResponse(response)
                activity?.runOnUiThread {
                    binding.isbnTextView.text = isbn
                    binding.titleTextView.text = bookInfoWithoutCoverImage.title
                    binding.authorTextView.text = bookInfoWithoutCoverImage.author
                    binding.publisherTextView.text = bookInfoWithoutCoverImage.publisher
                    binding.pubdateTextView.text = bookInfoWithoutCoverImage.pubdate
                }
                Log.d(TAG, bookInfoWithoutCoverImage.toString())

                val coverUrl = bookInfoWithoutCoverImage.coverUrl
                val coverImageCallback = object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        e.printStackTrace()
                    }

                    override fun onResponse(call: Call, response: Response) {
                        // 書影を取得する時のコールバック
                        val bitmap =
                            BitmapFactory.decodeStream(response.body!!.byteStream(), null, null)
                        activity?.runOnUiThread {
                            binding.bookImage.setImageBitmap(bitmap)
                        }

                        // DB に保存
                        val bookDao = MainActivity.db.bookDao()
                        val book = bookInfoWithoutCoverImage.copy()
                        book.coverImage = bitmap
                        runBlocking {
                            bookDao.update(book)
                        }
                    }
                }
                client.fetchCoverImage(coverUrl, coverImageCallback)
            }
        }
        client.fetchBookInfo(isbn, bookInfoCallback)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}