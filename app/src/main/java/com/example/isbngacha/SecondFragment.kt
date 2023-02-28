package com.example.isbngacha

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.isbngacha.databinding.FragmentBookDetailBinding
import kotlinx.coroutines.runBlocking

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

        // データを受け取る
        val isbn = SecondFragmentArgs.fromBundle(requireArguments()).isbn

        val bookDao = MainActivity.db.bookDao()
        var book: Book
        runBlocking {
            book = bookDao.getBookWithIsbn(isbn)
        }
        Log.d(TAG, book.toString())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}