package com.abhishekjoshi158.postivity.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.abhishekjoshi158.postivity.MainActivity
import com.abhishekjoshi158.postivity.datamodels.PositivityData
import com.abhishekjoshi158.postivity.utilities.FIRESTORE_DB_LIKES
import com.abhishekjoshi158.postivity.utilities.FIRESTORE_DB_QUOTES
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.firebase.ui.storage.images.FirebaseImageLoader
import com.google.firebase.firestore.SetOptions
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
  private var _myLikes : MutableMap<String,Boolean> = mutableMapOf()
  var myLikes = MutableLiveData<Map<String,Boolean>>()
  fun loadPositivity() {

    db.collection(FIRESTORE_DB_QUOTES)
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
    getMyLikes()
  }

   fun updateLike(documentId : String){
    var addLike = true
    if(_myLikes.containsKey(documentId)) {
      val value = !( _myLikes[documentId] ?: false ) // if already false then this time true vice-versa
      addLike = value
      addOrUpdateMyLike(documentId,value)
    }else{

      addOrUpdateMyLike(documentId,true)
    }

      db.collection(FIRESTORE_DB_QUOTES).document(documentId).get().addOnSuccessListener { result ->
        val data = result.data
        if (data != null) {
          var likes = (data.getOrElse("likes", { 4 }).toString()).toInt()
           if (addLike) likes += 1 else likes -= 1
          db.collection(FIRESTORE_DB_QUOTES).document(documentId)

            .update("likes", likes)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!"); }
            .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }
        }


      }

    quotes.forEachIndexed { index, quote ->
      if(quote.id == documentId){
        var like = quote.total_likes
        if (addLike) like += 1 else like -= 1
        quote.total_likes = like;
        quotes.removeAt(index)
        quotes.add(index,quote)
        _quotations.value = quotes
        return
      }

    }


  }


  private fun getMyLikes(){

     db.collection(FIRESTORE_DB_LIKES).document(MainActivity.DEVICE_ID)
       .get()
       .addOnSuccessListener { result ->
         _myLikes.clear()
         Log.d(TAG, "my likes ${result.data}");

         result.data?.forEach { document ->
           val key = document.key.toString()
           val value = document.value.toString().toBoolean()
           _myLikes.put(key,value)
           Log.d(TAG, "my likes key,value $key $value");
         }
         if(_myLikes.size>0){
           myLikes?.value =  _myLikes
         }

       }
       .addOnFailureListener { error ->
         Log.d(TAG, "my likes error $error ");
       }

   }

  private fun addOrUpdateMyLike(documentId: String,like: Boolean){
    _myLikes.put(documentId,like)
    db.collection(FIRESTORE_DB_LIKES).document(MainActivity.DEVICE_ID)
      .set(hashMapOf(documentId to like), SetOptions.merge())
      .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
      .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
    myLikes?.value =  _myLikes

  }

}
