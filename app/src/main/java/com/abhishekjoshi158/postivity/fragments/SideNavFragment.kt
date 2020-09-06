package com.abhishekjoshi158.postivity.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.abhishekjoshi158.postivity.MainActivity
import com.abhishekjoshi158.postivity.R
import com.abhishekjoshi158.postivity.adapter.SideNavAdapter
import kotlinx.android.synthetic.main.fragment_side_nav.*

data class SideNavItem(
    var id:Int,
    var itemName:String,
    var resourceId:Int
)

class SideNavFragment : Fragment(){

    private val sideNavAdapter: SideNavAdapter = SideNavAdapter {position, item ->
        onItemClick(position,item)
    }

    private fun onItemClick(position: Int, item: SideNavItem) {
       // Toast.makeText(requireContext(),""+item.itemName,Toast.LENGTH_LONG).show()
        (activity as MainActivity).closeDrawer(item)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?  = inflater.inflate(R.layout.fragment_side_nav, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
    }

    private fun setUpViews() {
        rv_side_nav_options?.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        rv_side_nav_options?.adapter = sideNavAdapter
        sideNavAdapter.setNavItemsData(prrepareNavItems())
    }

    //Create List of items to be displayed on the sidenav
    private fun prrepareNavItems(): List<SideNavItem> {
        val menuItemsList = ArrayList<SideNavItem>()
        menuItemsList.add(SideNavItem(1,"Positivity",R.id.positivityFragment))
        menuItemsList.add(SideNavItem(2,"Favourite",R.id.favouriteFragment))
        menuItemsList.add(SideNavItem(3,"Setting",R.drawable.ic_baseline_settings_24))
        menuItemsList.add(SideNavItem(4,"Share",R.drawable.ic_baseline_share_24))
        return menuItemsList
    }
}