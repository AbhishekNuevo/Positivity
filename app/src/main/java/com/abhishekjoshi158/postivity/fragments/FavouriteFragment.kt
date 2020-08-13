package com.abhishekjoshi158.postivity.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abhishekjoshi158.postivity.R
import com.abhishekjoshi158.postivity.adapter.FavouriteRCAdapter
import com.abhishekjoshi158.postivity.datamodels.FavouriteData
import com.abhishekjoshi158.postivity.utilities.showBasicDialog
import com.abhishekjoshi158.postivity.viewmodels.HomeScreenViewModel


class FavouriteFragment : Fragment() {

  private var viewModel: HomeScreenViewModel? = null
  private lateinit var adapter : FavouriteRCAdapter
  private var myFavouriteItem : MutableList<FavouriteData> = mutableListOf()
  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    val view =  inflater.inflate(R.layout.fragment_favourite, container, false)

   val  rc_favourite = view.findViewById<RecyclerView>(R.id.rc_favourite)
    val tv_fvt_msg = view.findViewById<TextView>(R.id.tv_fvt_msg)
    rc_favourite.layoutManager = LinearLayoutManager(requireContext())
    adapter = FavouriteRCAdapter(myFavouriteItem,::userClick)
    rc_favourite.adapter = adapter
    viewModel = ViewModelProvider(requireActivity()).get(HomeScreenViewModel::class.java)
    viewModel?.favourite?.observe(viewLifecycleOwner,Observer<List<FavouriteData>>{
      myFavouriteItem.clear()
      myFavouriteItem.addAll(it)
      adapter.notifyDataSetChanged()
      tv_fvt_msg.visibility =   if(myFavouriteItem.size > 0 ) View.GONE else View.VISIBLE

    })

    return view
  }

  fun userClick(documentId : String){
    val buttonCLicked =  showBasicDialog(requireContext(),getString(R.string.msg_remove_item))
    buttonCLicked.observe(viewLifecycleOwner,Observer<Int>{
      when(it){
        R.string.yes -> {
          viewModel?.updateFavourite(documentId)
        }
        R.string.no -> {

        }
        R.string.cancel -> {

        }
      }
    })
  }
}
