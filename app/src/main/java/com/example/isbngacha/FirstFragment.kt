package com.example.isbngacha

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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

        binding.isbnGeneratorButton.setOnClickListener {
            val isbnGenerator: IsbnGenerator = RandomIsbnGenerator()
            val isbn = isbnGenerator.generate()
            Log.d(TAG, "ISBN generated: $isbn")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}