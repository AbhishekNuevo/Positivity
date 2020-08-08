package com.abhishekjoshi158.postivity.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.abhishekjoshi158.postivity.MainActivity
import com.abhishekjoshi158.postivity.R
import com.abhishekjoshi158.postivity.datamodels.PositivityData
import java.util.zip.Inflater

class PositivityRCViewHolder(v: View) : RecyclerView.ViewHolder(v) {
  private var positivityImage: ImageView
  private var positivityText: TextView

  init {
    positivityImage = v.findViewById(R.id.iv_positivity)
    positivityText = v.findViewById(R.id.tv_positivity)
    positivityImage.layoutParams.width = MainActivity.SCREEN_WIDTH
    val height = (5 * MainActivity.SCREEN_WIDTH) / 4
    positivityImage.layoutParams.height = height
    positivityImage.requestLayout()
  }

  fun bind(quote: PositivityData) {
    positivityText.text = quote.positivity_text
    positivityImage.load(quote.image_url)

  }

}

class PositivityRCAdapter(private val quotes: List<PositivityData>) :
  RecyclerView.Adapter<PositivityRCViewHolder>() {

  override fun getItemCount(): Int {
    return quotes.size
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PositivityRCViewHolder {
    val inflater = LayoutInflater.from(parent.context)
    val view = inflater.inflate(R.layout.positivity_item_layout, parent, false)
    return PositivityRCViewHolder(view)
  }

  override fun onBindViewHolder(holder: PositivityRCViewHolder, position: Int) {

     holder.bind( quotes.get(position))
  }

}
