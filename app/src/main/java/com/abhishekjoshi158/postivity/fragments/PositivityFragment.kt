package com.abhishekjoshi158.postivity.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abhishekjoshi158.postivity.R
import com.abhishekjoshi158.postivity.adapter.PositivityRCAdapter
import com.abhishekjoshi158.postivity.datamodels.PositivityData
import com.abhishekjoshi158.postivity.utilities.LIKE
import com.abhishekjoshi158.postivity.utilities.SAVE
import com.abhishekjoshi158.postivity.utilities.SHARE
import com.abhishekjoshi158.postivity.viewmodels.HomeScreenViewModel


class PositivityFragment : Fragment() {

   private lateinit var adapter : PositivityRCAdapter
   private  var viewModel : HomeScreenViewModel ? = null
  private var items : MutableList<PositivityData> = mutableListOf()
  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {

    val view = inflater.inflate(R.layout.fragment_postivity, container, false)
    val rc_view_positivity = view.findViewById<RecyclerView>(R.id.rc_view_positivity)

    rc_view_positivity.layoutManager = LinearLayoutManager(requireContext())
   viewModel = ViewModelProvider(requireActivity()).get(HomeScreenViewModel::class.java)
    adapter = PositivityRCAdapter(items,::userClickEvent)
    rc_view_positivity.adapter = adapter
   viewModel?.positivity?.observe(viewLifecycleOwner,Observer<List<PositivityData>>{
     items.clear()
     items.addAll(it)
     adapter.notifyDataSetChanged()
  })

    return view
  }

  fun userClickEvent(type : String,documentId : String ){
    when(type){
      LIKE -> {
          viewModel?.updateLike(documentId)
      }
      SHARE -> {

      }
      SAVE -> {

      }
    }
  }

}
