package com.abhishekjoshi158.postivity.fragments

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abhishekjoshi158.postivity.MainActivity
import com.abhishekjoshi158.postivity.R
import com.abhishekjoshi158.postivity.adapter.FavouriteRCAdapter
import com.abhishekjoshi158.postivity.datamodels.FavouriteData
import com.abhishekjoshi158.postivity.interfaces.FavouriteInterface
import com.abhishekjoshi158.postivity.utilities.*
import com.abhishekjoshi158.postivity.viewmodels.HomeScreenViewModel


class FavouriteFragment : Fragment(),FavouriteInterface {

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
    adapter = FavouriteRCAdapter(myFavouriteItem,this)
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


  override fun removeItem(documentId: String) {
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

  override fun downloadItem(view: View) {
    val permission = ContextCompat.checkSelfPermission(requireContext(),android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
    if(permission == PackageManager.PERMISSION_GRANTED){
      val bitmap =  getBitmapOFView(view)
      if(bitmap!=null){
       val path = saveImageToExternalStorage(bitmap,requireContext())
        Toast.makeText(context, getString( R.string.image_saved_msg) + path, Toast.LENGTH_LONG).show()
      } else {
        Toast.makeText(context, R.string.issue_bitmap, Toast.LENGTH_SHORT).show()
      }

    }else {
      (activity as MainActivity).setUpPermission()
      (activity as MainActivity).permissionGranted.observe(viewLifecycleOwner, Observer<Int> {
        if (it == STORAGE_REQUEST_CODE) {
          //start download
        }
      })
    }
  }


}
