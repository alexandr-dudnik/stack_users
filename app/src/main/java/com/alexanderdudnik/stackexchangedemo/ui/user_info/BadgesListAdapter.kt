package com.alexanderdudnik.stackexchangedemo.ui.user_info

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alexanderdudnik.stackexchangedemo.R
import com.alexanderdudnik.stackexchangedemo.databinding.BadgesListItemBinding

/**
 * Badges list adapter
 *
 * Describes static adapter for user badges using list as constructor parameter
 */
class BadgesListAdapter(private val data:List<String>) : RecyclerView.Adapter<BadgesListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.badges_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size


    /**
     * Internal class for View holder
     */
    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private val binder = BadgesListItemBinding.bind(view)

        fun bind(badge: String){
            binder.badgeDescriptionTv.text = badge
        }
    }

}