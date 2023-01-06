package com.example.loltest.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class BaseRecyclerView<T>(
    private val dataSet: ArrayList<T>, @LayoutRes val layoutId: Int,
    private val bindingInterface: BaseRecyclerViewBindingInterface<T>,
) : RecyclerView.Adapter<BaseRecyclerView.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(v: View, pos: Int)
    }

    private lateinit var mListener: OnItemClickListener

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.mListener = listener
    }

    // 바인딩 인터페이스를 사용하여 데이터 바인딩
    interface BaseRecyclerViewBindingInterface<T> {
        fun bindData(item: T, view: View)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataSet[position]
        holder.bind(item, bindingInterface)

//        holder.itemView.setOnClickListener {
//            if (position != RecyclerView.NO_POSITION) {
//                mListener.onItemClick(holder.itemView, position)
//            }
//        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun <T> bind(item: T, bindingInterface: BaseRecyclerViewBindingInterface<T>) =
            bindingInterface.bindData(item, view)
    }

}