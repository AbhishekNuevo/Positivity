package com.abhishekjoshi158.postivity.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.abhishekjoshi158.postivity.datamodels.PositivityData
import com.abhishekjoshi158.postivity.utilities.FIRESTORE_DB
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.firebase.ui.storage.images.FirebaseImageLoader
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import java.io.InputStream


@GlideModule
class MyAppGlideModule : AppGlideModule() {
  override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
    super.registerComponents(context, glide, registry)
    registry.append(
      StorageReference::class.java, InputStream::class.java,
      FirebaseImageLoader.Factory()
    )
  }

}

class HomeScreenRepository {
  private val TAG = HomeScreenRepository::class.java.simpleName
  private val _quotations = MutableLiveData<List<PositivityData>>()
  val quotations: LiveData<List<PositivityData>> = _quotations
  private val db = Firebase.firestore
  private  var quotes  : MutableList<PositivityData> = mutableListOf()
  fun loadPositivity() {

    db.collection(FIRESTORE_DB)
      .get()
      .addOnSuccessListener {result ->
        quotes.clear()
        for(document in result){

         val quote =  document.data.getOrElse("quote",{""}).toString()
         val likes =  ( document.data.getOrElse("likes",{4}).toString()).toInt()
         val imgUrl =  document.data.getOrElse("image_url",{ "2" }).toString()
         val positivityData =  PositivityData(document.id,imgUrl,quote,likes)
          quotes.add(positivityData)
          Log.e("firebase db ", "${document.id} => $positivityData")
        }
        _quotations.value = quotes
      }
      .addOnFailureListener{

      }

  }

  fun updateLike(documentId : String){
    db.collection(FIRESTORE_DB).document(documentId).get().addOnSuccessListener { result ->
      val data = result.data
      if(data != null){
       val likes =   ( data.getOrElse("likes",{4}).toString()).toInt() + 1

        db.collection(FIRESTORE_DB).document(documentId)

          .update("likes", likes  )
          .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!");  }
          .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }
      }

    }
    quotes.forEachIndexed { index, quote ->
      if(quote.id == documentId){
        val like = quote.total_likes + 1
        quote.total_likes = like;
        quotes.removeAt(index)
        quotes.add(index,quote)
        _quotations.value = quotes
        return
      }

    }


  }


}
