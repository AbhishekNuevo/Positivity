package com.abhishekjoshi158.postivity.adapter

import android.content.Context
import android.util.Log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.abhishekjoshi158.postivity.MainActivity
import com.abhishekjoshi158.postivity.R
import com.abhishekjoshi158.postivity.datamodels.PositivityData
import com.abhishekjoshi158.postivity.repository.GlideApp
import com.abhishekjoshi158.postivity.utilities.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

class PositivityRCViewHolder(v: View,private val context : Context, private val userClicks: (String, String) -> Unit) : RecyclerView.ViewHolder(v), View.OnClickListener  {
  private val TAG = PositivityRCViewHolder::class.java.simpleName
  private var positivityImage: ImageView
  private var positivityText: TextView
  private val storageReference : StorageReference
  private val ll_like : LinearLayout
  private val ll_save : LinearLayout
  private val ll_share : LinearLayout
  private val rl_image_container : RelativeLayout
  private val tv_like_no: TextView
  private val im_heart : ImageView
  private val im_save : ImageView
  private val cb_like_icon : CheckBox
  private var likes = 0
  private var documentId = ""
  private var favourite = false
  init {
    storageReference = Firebase.storage.reference
    positivityImage = v.findViewById(R.id.iv_positivity)
    positivityText = v.findViewById(R.id.tv_positivity)
    tv_like_no = v.findViewById(R.id.tv_like_no)
    ll_like = v.findViewById(R.id.ll_like)
    ll_save = v.findViewById(R.id.ll_save)
    ll_share = v.findViewById(R.id.ll_share)
    rl_image_container = v.findViewById(R.id.rl_image_container)
    im_heart = v.findViewById(R.id.im_heart)
    im_save = v.findViewById(R.id.im_save)
    cb_like_icon = v.findViewById(R.id.cb_like_icon)
    ll_like.setOnClickListener(this)
    ll_save.setOnClickListener(this)
    ll_share.setOnClickListener(this)

    positivityImage.layoutParams.width = MainActivity.SCREEN_WIDTH
    val height = (4 * MainActivity.SCREEN_WIDTH) / 4
    positivityImage.layoutParams.height = height
    positivityImage.requestLayout()

  }

  fun bind(quote: PositivityData,liked:Boolean) {
    positivityText.text = quote.positivity_text
    val path = "english/${quote.image_url}.png"
    GlideApp.with(context).load(storageReference.child(path)).into(positivityImage)
    likes = quote.total_likes
    documentId = quote.id
    if(liked && likes > 0){
      val likeText = "You & " + --likes + " Others"
      tv_like_no.text = likeText
      im_heart.setImageResource(R.drawable.ic_baseline_favorite_24)
    }else{
      tv_like_no.text = likes.toString()
      im_heart.setImageResource(R.drawable.ic_baseline_favorite_border_24)
    }
    favourite = quote.favourite
     Log.d(TAG,"favourite ${quote.favourite}")
    if(quote.favourite){
      im_save.setImageResource(R.drawable.ic_baseline_library_add_check_24)

    }else{
      im_save.setImageResource(R.drawable.ic_baseline_library_add_24)
    }


  }

  override fun onClick(v: View?) {
    if (v != null) {
      when(v.id){
         R.id.ll_like -> {
           userClicks(LIKE,documentId)
         }
        R.id.ll_save -> {
          if(favourite){
            Toast.makeText(context,"Remove from favourite tab",Toast.LENGTH_LONG).show()
          }else {
            userClicks(FAVOURITE, documentId)
          }
        }
       R.id.ll_share -> {
        val bitmap =  getBitmapOFView(rl_image_container)
         if(bitmap!=null)
         getURI(context,bitmap) else
         Toast.makeText(context,R.string.issue_bitmap, Toast.LENGTH_SHORT).show()

       }

      }
    }
  }

}

class PositivityRCAdapter(private val quotes: List<PositivityData>,private val myLike : Map<String,Boolean>,private val userClicks : (String,String)->Unit) :
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
   val documentId = quotes.get(position).id
     holder.bind( quotes.get(position), myLike[documentId]?:false)

  }

}
