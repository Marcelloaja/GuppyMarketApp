package com.stiki.marcelloilham.guppymarketidn.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.stiki.marcelloilham.guppymarketidn.R
import com.stiki.marcelloilham.guppymarketidn.menuHome.DetailActivity
import com.stiki.marcelloilham.guppymarketidn.menuHome.Item

class GridAdapter(
    private val context: Context,
    private var items: List<Item>
) : RecyclerView.Adapter<GridAdapter.GridViewHolder>() {

    private var listener: OnItemClickListener? = null
    private var originalItems: List<Item> = items

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    fun filterItems(filter: String) {
        items = if (filter == "All") {
            originalItems
        } else {
            originalItems.filter { it.type == filter }
        }
        notifyDataSetChanged()
    }

    fun getItem(position: Int): Item {
        return items[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.grid_item, parent, false)
        return GridViewHolder(view)
    }

    override fun onBindViewHolder(holder: GridViewHolder, position: Int) {
        val item = items[position]
        holder.nameProductGrid.text = item.name
        holder.showTypeGrid.text = item.type
        holder.showPriceGrid.text = item.price
        holder.itemImage.setImageResource(item.image)

        holder.buttonViewDetail.setOnClickListener {
            listener?.onItemClick(position)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class GridViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemImage: ImageView = itemView.findViewById(R.id.item_image)
        val nameProductGrid: TextView = itemView.findViewById(R.id.nameProductGrid)
        val showTypeGrid: TextView = itemView.findViewById(R.id.showTypeGrid)
        val showPriceGrid: TextView = itemView.findViewById(R.id.showPriceGrid)
        val buttonViewDetail: LinearLayout = itemView.findViewById(R.id.button_view_detail)

        init {
            itemView.setOnClickListener {
                listener?.onItemClick(adapterPosition)
            }
        }
    }
}
