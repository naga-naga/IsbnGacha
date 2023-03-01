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
        val bookDao = MainActivity.db.bookDao()

        // isbn を受け取る
        val isbn = SecondFragmentArgs.fromBundle(requireArguments()).isbn

        // 書籍情報を取得する際のコールバック
        val client = OpenbdClient()
        var bookInfoWithoutCoverImage = Book(isbn)
        val bookInfoCallback = object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                // 書籍情報を取得する時のコールバック．書影は URL が返ってくるので，別に取得する．
                bookInfoWithoutCoverImage.lastFetchUnixTime = System.currentTimeMillis()
                try {
                    bookInfoWithoutCoverImage = client.parseResponse(response)
                } catch (e: Exception) {
                    e.printStackTrace()
                    runBlocking {
                        bookDao.update(bookInfoWithoutCoverImage)
                    }
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
                        val book = bookInfoWithoutCoverImage.copy()
                        book.coverImage = bitmap

                        activity?.runOnUiThread {
                            updateDetailView(book)
                        }

                        // DB に保存
                        runBlocking {
                            bookDao.update(book)
                        }
                    }
                }
                client.fetchCoverImage(coverUrl, coverImageCallback)
            }
        }

        // 取得済みの場合は DB から読み出す
        var book: Book
        runBlocking {
            book = bookDao.getBookWithIsbn(isbn)
        }

        if (book.lastFetchUnixTime == null) {
            client.fetchBookInfo(isbn, bookInfoCallback)
        } else {
            updateDetailView(book)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateDetailView(book: Book) {
        binding.isbnTextView.text = book.isbn
        binding.titleTextView.text = book.title
        binding.authorTextView.text = book.author
        binding.publisherTextView.text = book.publisher
        binding.pubdateTextView.text = book.pubdate
        binding.bookImage.setImageBitmap(book.coverImage)
    }
}