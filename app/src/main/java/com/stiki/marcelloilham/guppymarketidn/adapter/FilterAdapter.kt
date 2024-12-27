// FilterAdapter.kt
package com.stiki.marcelloilham.guppymarketidn.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.stiki.marcelloilham.guppymarketidn.R

class FilterAdapter(
    private val context: Context,
    private val filters: List<String>,
    private val onFilterSelected: (String) -> Unit
) : RecyclerView.Adapter<FilterAdapter.FilterViewHolder>() {

    private var selectedPosition = 0 // Default to "All" selected

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.filter_item, parent, false)
        return FilterViewHolder(view)
    }

    override fun onBindViewHolder(holder: FilterViewHolder, position: Int) {
        val filter = filters[position]
        holder.filterText.text = filter

        if (position == selectedPosition) {
            holder.filterText.setBackgroundResource(R.drawable.filter_selected)
            holder.filterText.setTextColor(ContextCompat.getColor(context, R.color.black))
        } else {
            holder.filterText.setBackgroundResource(R.drawable.filter_unselected)
            holder.filterText.setTextColor(ContextCompat.getColor(context, R.color.gray))
        }

        holder.filterText.setOnClickListener {
            val previousPosition = selectedPosition
            selectedPosition = holder.adapterPosition

            notifyItemChanged(previousPosition)
            notifyItemChanged(selectedPosition)

            onFilterSelected(filter)
        }
    }

    override fun getItemCount(): Int {
        return filters.size
    }

    inner class FilterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val filterText: TextView = itemView.findViewById(R.id.filterText)
    }
}
