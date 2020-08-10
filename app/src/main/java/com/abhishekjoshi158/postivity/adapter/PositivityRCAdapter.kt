package com.abhishekjoshi158.postivity.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.abhishekjoshi158.postivity.MainActivity
import com.abhishekjoshi158.postivity.R
import com.abhishekjoshi158.postivity.datamodels.PositivityData
import com.abhishekjoshi158.postivity.repository.GlideApp
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.abhishekjoshi158.postivity.utilities.LIKE
import com.abhishekjoshi158.postivity.utilities.SAVE
import com.abhishekjoshi158.postivity.utilities.SHARE

class PositivityRCViewHolder(v: View,private val context : Context, private val userClicks: (String, String) -> Unit) : RecyclerView.ViewHolder(v), View.OnClickListener  {
  private var positivityImage: ImageView
  private var positivityText: TextView
  private val storageReference : StorageReference
  private val ll_like : LinearLayout
  private val ll_save : LinearLayout
  private val ll_share : LinearLayout
  private val tv_like_no: TextView
  private var likes = 0
  private var documentId = ""
  init {
    storageReference = Firebase.storage.reference
    positivityImage = v.findViewById(R.id.iv_positivity)
    positivityText = v.findViewById(R.id.tv_positivity)
    tv_like_no = v.findViewById(R.id.tv_like_no)
    ll_like = v.findViewById(R.id.ll_like)
    ll_save = v.findViewById(R.id.ll_save)
    ll_share = v.findViewById(R.id.ll_share)

    ll_like.setOnClickListener(this)
    ll_save.setOnClickListener(this)
    ll_share.setOnClickListener(this)

    positivityImage.layoutParams.width = MainActivity.SCREEN_WIDTH
    val height = (4 * MainActivity.SCREEN_WIDTH) / 4
    positivityImage.layoutParams.height = height
    positivityImage.requestLayout()
//    Log.i(PositivityRCViewHolder::class.java.simpleName,"width ${MainActivity.SCREEN_WIDTH} and height $height")
  }

  fun bind(quote: PositivityData) {
    positivityText.text = quote.positivity_text
    val path = "english/${quote.image_url}.png"
    GlideApp.with(context).load(storageReference.child(path)).into(positivityImage)
    likes = quote.total_likes
    tv_like_no.text = quote.total_likes.toString()
    documentId = quote.id

  }

  override fun onClick(v: View?) {
    if (v != null) {
      when(v.id){
         R.id.ll_like -> {
           userClicks(LIKE,documentId)
         }
        R.id.ll_save -> {
          userClicks(SAVE,documentId)
        }
       R.id.ll_share -> {
         userClicks(SHARE,documentId)
       }

      }
    }
  }

}

class PositivityRCAdapter(private val quotes: List<PositivityData>, private val userClicks : (String,String)->Unit) :
  RecyclerView.Adapter<PositivityRCViewHolder>() {

  override fun getItemCount(): Int {
    return quotes.size
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PositivityRCViewHolder {
    val inflater = LayoutInflater.from(parent.context)
    val view = inflater.inflate(R.layout.positivity_item_layout, parent, false)
    return PositivityRCViewHolder(view,parent.context,userClicks)
  }

  override fun onBindViewHolder(holder: PositivityRCViewHolder, position: Int) {

     holder.bind( quotes.get(position))

  }

}
