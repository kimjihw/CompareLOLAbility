package kr.co.simplebestapp.LolApiTest.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import kr.co.simplebestapp.LolApiTest.common.AppPreferences

class ClickBaseRecyclerView<T>(
    private val dataSet: ArrayList<T>, @LayoutRes val layoutId: Int,
    private val bindingInterface: ClickBaseRecyclerViewBindingInterface<T>,
) : RecyclerView.Adapter<ClickBaseRecyclerView.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(v: View, pos: Int)
    }

    private lateinit var mListener: OnItemClickListener

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.mListener = listener
    }

    // 바인딩 인터페이스를 사용하여 데이터 바인딩
    interface ClickBaseRecyclerViewBindingInterface<T> {
        fun bindData(item: T, view: View, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataSet[position]
        holder.bind(item, bindingInterface)

        holder.itemView.setOnClickListener {
            if (position != RecyclerView.NO_POSITION) {
                mListener.onItemClick(holder.itemView, position)
            }
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun <T> bind(item: T, bindingInterface: ClickBaseRecyclerViewBindingInterface<T>) =
            bindingInterface.bindData(item, view, adapterPosition)
    }

    fun removeItem(pos: Int){
        dataSet.removeAt(pos)
        AppPreferences.removeHistoryInfo(pos)
    }

    fun addItem(pos: Int, data: T){
        dataSet.add(data)
        AppPreferences.setHistoryInfo(AppPreferences.getHistoryInfo()[pos])
    }
}