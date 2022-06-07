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

/**
 * User list adapter
 *
 * @property selectItemCallback
 */
class UserListAdapter(
    private val selectItemCallback: (id: Int, name:String) -> Unit
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

    /**
     * Updates data for recycler view and animate changes
     *
     * @param newData
     */
    fun updateList(newData: List<StackUserEntity>) {
        DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize(): Int = data.size

            override fun getNewListSize(): Int = newData.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return data[oldItemPosition].accountId == newData[newItemPosition].accountId
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return data[oldItemPosition].accountId == newData[newItemPosition].accountId &&
                        data[oldItemPosition].displayName == newData[newItemPosition].displayName &&
                        data[oldItemPosition].reputation == newData[newItemPosition].reputation
            }

        }).dispatchUpdatesTo(this)
        data.clear()
        data.addAll(newData)
    }


    /**
     * View holder for items
     *
     * @param view
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binder = UserListItemBinding.bind(view)

        fun bind(item: StackUserEntity, callback: (id: Int, name: String) -> Unit) {
            with(binder) {
                userReputationTxt.text = item.reputation.toString()
                userNameTv.text = item.displayName
                userItemHolder.setOnClickListener {
                    callback.invoke(item.userId, item.displayName)
                }
            }
        }
    }

}