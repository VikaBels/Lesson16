package com.example.lesson16.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.lesson16.R
import com.example.lesson16.databinding.NameItemMenuBinding
import com.example.lesson16.interfaces.MenuNavigationListener
import com.example.lesson16.models.ItemMenu

class MenuItemAdapter(
    private val context: Context,
    private val menuItemList: List<ItemMenu>,
    private val listenerForFragment: MenuNavigationListener?,
) : RecyclerView.Adapter<MenuItemAdapter.MenuItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuItemViewHolder {
        val binding = NameItemMenuBinding.inflate(LayoutInflater.from(context), parent, false)
        return MenuItemViewHolder(binding, listenerForFragment,context)
    }

    override fun onBindViewHolder(holder: MenuItemViewHolder, position: Int) {
        val fileItem = menuItemList[position]
        holder.bind(fileItem)
    }

    override fun getItemCount(): Int {
        return menuItemList.size
    }

    class MenuItemViewHolder(
        private val binding: NameItemMenuBinding,
        private val listenerForFragment: MenuNavigationListener?,
        private val context: Context,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(menuItem: ItemMenu) {
            binding.name.setTextColor(ContextCompat.getColor(context, R.color.black))

            binding.name.text = menuItem.name
            binding.image.setImageResource(menuItem.drawableId)

            binding.nameItemMenu.setOnClickListener {
                menuItem.name?.let { it1 -> listenerForFragment?.switchingNewActivity(it1) }
                binding.name.setTextColor(ContextCompat.getColor(context, R.color.teal_700))
            }
        }
    }
}