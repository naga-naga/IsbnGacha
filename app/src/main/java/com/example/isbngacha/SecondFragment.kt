package com.example.isbngacha

import android.app.AlertDialog
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
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
                try {
                    bookInfoWithoutCoverImage = client.parseResponse(response)
                    bookInfoWithoutCoverImage.lastFetchUnixTime = System.currentTimeMillis()
                } catch (e: Exception) {
                    // 書籍情報が見つからなかった場合
                    e.printStackTrace()
                    bookInfoWithoutCoverImage.lastFetchUnixTime = System.currentTimeMillis()
                    runBlocking {
                        bookDao.update(bookInfoWithoutCoverImage)
                    }
                    activity?.runOnUiThread {
                        popupAlertDialog(isbn)
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
        if (book.title == null) {
            popupAlertDialog(book.isbn)
            return
        }

        val pubdate = addSlashToPubdate(book.pubdate)

        binding.isbnTextView.text = book.isbn
        binding.titleTextView.text = book.title
        binding.authorTextView.text = book.author
        binding.publisherTextView.text = book.publisher
        binding.pubdateTextView.text = pubdate
        binding.bookImage.setImageBitmap(book.coverImage)
    }

    private fun popupAlertDialog(isbn: String) {
        AlertDialog.Builder(activity)
            .setMessage("書籍情報が見つかりませんでした")
            .setPositiveButton("削除") { _, _ ->
                val bookDao = MainActivity.db.bookDao()
                runBlocking {
                    bookDao.deleteWithIsbn(isbn)
                }
                findNavController().popBackStack()
            }
            .show()
    }

    /**
     * YYYYMMDD -> YYYY/MM/DD にする
     * YYYYMMDD の形式でない場合はそのまま返す
     */
    private fun addSlashToPubdate(pubdate: String?): String {
        if (pubdate?.length != 8) {
            return pubdate ?: ""
        }

        val year = pubdate.substring(0..3)
        val month = pubdate.substring(4..5)
        val day = pubdate.substring(6..7)
        return "$year/$month/$day"
    }
}