package com.example.isbngacha

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.isbngacha.databinding.FragmentIsbnListBinding
import kotlinx.coroutines.runBlocking

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private val TAG = "FirstFragment"

    private var _binding: FragmentIsbnListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var isbnGenerator: IsbnGenerator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentIsbnListBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1列に並べる
        val layoutManager = LinearLayoutManager(context)
        binding.recyclerView.layoutManager = layoutManager

        // 区切り線を入れる
        val decoration = DividerItemDecoration(context, layoutManager.orientation)
        binding.recyclerView.addItemDecoration(decoration)

        val bookDao = MainActivity.db.bookDao()
        var dataSet: MutableList<Book>
        // DB へのアクセスはメインスレッドから行えないため，コルーチンを使用
        runBlocking {
            dataSet = bookDao.getAllBooks()
        }
        val adapter = RecyclerViewAdapter(dataSet)
        adapter.setOnClickListener {
            // RecyclerView 内の TextView から ISBN を取得
            val isbn = it.findViewById<TextView>(R.id.itemIsbnTextView).text.toString()

            // データを渡しつつ画面遷移
            val action = FirstFragmentDirections.actionFirstFragmentToSecondFragment(isbn)
            it.findNavController().navigate(action)
        }
        binding.recyclerView.adapter = adapter

        // ISBN 生成
        isbnGenerator = RandomIsbnGenerator()

        // 単発
        binding.oneIsbnGeneratorButton.setOnClickListener {
            val isbn = isbnGenerator.generate()
            val book = Book(isbn)
            Log.d(TAG, "ISBN generated: $isbn")
            adapter.addData(book)

            // DB に保存
            runBlocking {
                bookDao.insert(book)
            }
        }

        // 10回
        binding.tenIsbnsGeneratorButton.setOnClickListener {
            val bookList: MutableList<Book> = mutableListOf()
            for (i in 0..9) {
                val isbn = isbnGenerator.generate()
                val book = Book(isbn)
                Log.d(TAG, "ISBN generated: $isbn")
                adapter.addData(book)
                bookList.add(book)
            }

            runBlocking {
                bookDao.insert(bookList)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}