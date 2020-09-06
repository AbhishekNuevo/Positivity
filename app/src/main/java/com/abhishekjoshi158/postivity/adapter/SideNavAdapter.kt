package com.abhishekjoshi158.postivity.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.abhishekjoshi158.postivity.R
import com.abhishekjoshi158.postivity.fragments.SideNavItem
import kotlinx.android.synthetic.main.item_side_nav.view.*

class SideNavAdapter(private val onItemClick: ((position: Int, item: SideNavItem) -> Unit)) :
    RecyclerView.Adapter<SideNavVH>() {
    var menuItemsList = ArrayList<SideNavItem>()

    fun setNavItemsData( list: List<SideNavItem>){
        menuItemsList.addAll(list)
        notifyDataSetChanged()
    }




    override fun getItemCount(): Int {
        return menuItemsList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SideNavVH {
        return SideNavVH(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_side_nav, parent, false)
        )
    }

    override fun onBindViewHolder(holder: SideNavVH, position: Int) {

        holder.setData(menuItemsList[position],onItemClick)
    }



}

class SideNavVH(inflate: View) : RecyclerView.ViewHolder(inflate) {
    fun setData(sideNavItem: SideNavItem,itemClick:((position: Int, item: SideNavItem)->Unit)) {

        itemView.setOnClickListener {
            sideNavItem.let {
                itemClick.invoke(adapterPosition, sideNavItem)
            }

        }
       //itemView.iv_nav_option?.setImageResource(sideNavItem.resourceId)
        itemView.tv_nav_text?.text = sideNavItem.itemName
    }
}

