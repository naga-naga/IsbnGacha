package com.example.isbngacha

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.isbngacha.databinding.FragmentIsbnListBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private val TAG = "FirstFragment"

    private var _binding: FragmentIsbnListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

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

        val dataSet: MutableList<String> = mutableListOf()
        val adapter = RecyclerViewAdapter(dataSet)
        binding.recyclerView.adapter = adapter

        // ISBN 生成ボタン
        binding.isbnGeneratorButton.setOnClickListener {
            val isbnGenerator: IsbnGenerator = RandomIsbnGenerator()
            val isbn = isbnGenerator.generate()
            Log.d(TAG, "ISBN generated: $isbn")
            adapter.addData(isbn)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}