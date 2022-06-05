package com.alexanderdudnik.stackexchangedemo.ui.user_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.alexanderdudnik.stackexchangedemo.R
import com.alexanderdudnik.stackexchangedemo.data.StackUserEntity
import com.alexanderdudnik.stackexchangedemo.databinding.UserListItemBinding
import com.alexanderdudnik.stackexchangedemo.ui.user_list.UserListAdapter.ViewHolder

class UserListAdapter(
    private val selectItemCallback: (id: Int) -> Unit
) : RecyclerView.Adapter<ViewHolder>() {
    private val data = mutableListOf<StackUserEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.user_list_item, parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position], selectItemCallback)
    }

    override fun getItemCount(): Int = data.size

    fun updateList(newData: List<StackUserEntity>) {
        DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize(): Int = data.size

            override fun getNewListSize(): Int = newData.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return data[oldItemPosition].accountId == newData[newItemPosition].accountId
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return data[oldItemPosition].accountId == newData[newItemPosition].accountId &&
                        data[oldItemPosition].displayName == newData[newItemPosition].displayName
            }

        }).dispatchUpdatesTo(this)
        data.clear()
        data.addAll(newData)
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binder = UserListItemBinding.bind(view)

        fun bind(item: StackUserEntity, callback: (id: Int) -> Unit) {
            with(binder) {
                userIdTxt.text = item.accountId.toString()
                userNameTv.text = item.displayName
                userItemHolder.setOnClickListener {
                    callback.invoke(item.accountId)
                }
            }
        }
    }

}