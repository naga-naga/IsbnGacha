package com.example.isbngacha

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewAdapter(private val dataSet: MutableList<Book>) :
    RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    private lateinit var onClickListener: OnClickListener

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView

        init {
            textView = view.findViewById(R.id.itemIsbnTextView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_view_item, parent, false)
        view.setOnClickListener(onClickListener)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val isbn = dataSet[position].isbn
        holder.textView.text = isbn
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    fun setOnClickListener(listener: OnClickListener) {
        onClickListener = listener
    }

    fun addData(data: Book) {
        dataSet.add(data)
        notifyItemInserted(dataSet.size - 1)
    }
}