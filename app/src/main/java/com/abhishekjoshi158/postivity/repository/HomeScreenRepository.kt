package com.abhishekjoshi158.postivity.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.abhishekjoshi158.postivity.MainActivity
import com.abhishekjoshi158.postivity.datamodels.FavouriteData
import com.abhishekjoshi158.postivity.datamodels.PositivityData
import com.abhishekjoshi158.postivity.utilities.FIRESTORE_DB_FAVOURITE
import com.abhishekjoshi158.postivity.utilities.FIRESTORE_DB_LIKES
import com.abhishekjoshi158.postivity.utilities.FIRESTORE_DB_QUOTES
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.firebase.ui.storage.images.FirebaseImageLoader
import com.google.firebase.firestore.FieldValue
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
  private val _favourite = MutableLiveData<List<FavouriteData>>()
  val favourite: LiveData<List<FavouriteData>> = _favourite
  private val db = Firebase.firestore
  private var quotes: MutableList<PositivityData> = mutableListOf()
  private var _myLikes: MutableMap<String, Boolean> = mutableMapOf()
  var myLikes = MutableLiveData<Map<String, Boolean>>()
  val myFavouriteList: MutableList<FavouriteData> = mutableListOf()
  fun loadPositivity() {

    db.collection(FIRESTORE_DB_QUOTES)
      .get()
      .addOnSuccessListener { result ->
        quotes.clear()
        for (document in result) {

          val quote = document.data.getOrElse("quote", { "" }).toString()
          val likes = (document.data.getOrElse("likes", { 4 }).toString()).toInt()
          val imgUrl = document.data.getOrElse("image_url", { "2" }).toString()
          val positivityData = PositivityData(document.id, imgUrl, quote, likes)
          quotes.add(positivityData)
          Log.e("firebase db ", "${document.id} => $positivityData")
        }
        _quotations.value = quotes
        getMyFavourite()
      }
      .addOnFailureListener {

      }
    getMyLikes()

  }

  fun updateLike(documentId: String) {
    var addLike = true
    if (_myLikes.containsKey(documentId)) {
      val value =
        !(_myLikes[documentId] ?: false) // if already false then this time true vice-versa
      addLike = value
      addOrUpdateMyLike(documentId, value)
    } else {

      addOrUpdateMyLike(documentId, true)
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
      if (quote.id == documentId) {
        var like = quote.total_likes
        if (addLike) like += 1 else like -= 1
        quote.total_likes = like;
        quotes.removeAt(index)
        quotes.add(index, quote)
        _quotations.value = quotes
        return
      }

    }


  }


  private fun getMyLikes() {

    db.collection(FIRESTORE_DB_LIKES).document(MainActivity.DEVICE_ID)
      .get()
      .addOnSuccessListener { result ->
        _myLikes.clear()
        Log.d(TAG, "my likes ${result.data}");

        result.data?.forEach { document ->
          val key = document.key.toString()
          val value = document.value.toString().toBoolean()
          _myLikes.put(key, value)
          Log.d(TAG, "my likes key,value $key $value");
        }
        if (_myLikes.size > 0) {
          myLikes?.value = _myLikes
        }

      }
      .addOnFailureListener { error ->
        Log.d(TAG, "my likes error $error ");
      }

  }

  private fun addOrUpdateMyLike(documentId: String, like: Boolean) {
    _myLikes.put(documentId, like)
    db.collection(FIRESTORE_DB_LIKES).document(MainActivity.DEVICE_ID)
      .set(hashMapOf(documentId to like), SetOptions.merge())
      .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
      .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
    myLikes?.value = _myLikes

  }

  private fun getMyFavourite() {
    val mapList: MutableMap<String, Boolean> = mutableMapOf()

    db.collection(FIRESTORE_DB_FAVOURITE).document(MainActivity.DEVICE_ID)
      .get()
      .addOnSuccessListener { result ->
        result.data?.forEach { document ->
//          Log.d(TAG, "my getMyFavourite1  ${document.key} ${document.value} ");
          mapList.put(document.key, document.value.toString().toBoolean())
        }
        myFavouriteList.clear()
        for (it in mapList) {
          for ((index, positivityData) in quotes.withIndex()) {
            if (it.key == positivityData.id) {
              Log.d(TAG, "my getMyFavourite quote1 ${it.key} ${it.value} ");
              quotes[index].favourite = it.value

              if (it.value) myFavouriteList.add(
                FavouriteData(
                  positivityData.id,
                  positivityData.image_url,
                  positivityData.positivity_text
                )
              )
            }

          }
        }

        _quotations.value = quotes
        _favourite.value = myFavouriteList
      }
      .addOnFailureListener { error ->
        Log.d(TAG, "my likes error $error ");
      }
  }

  fun addOrUpdateFavourite(documentId: String) {
    var listIndex = 0;
    Log.d(TAG, "addOrUpdateFavourite")
    quotes.forEachIndexed { index, positivityData ->
      if (documentId == positivityData.id) {
        listIndex = index

      }
    }
    val favourite = quotes[listIndex].favourite

    db.collection(FIRESTORE_DB_FAVOURITE).document(MainActivity.DEVICE_ID)
      .set(hashMapOf(documentId to !favourite), SetOptions.merge())
      .addOnSuccessListener { Log.d(TAG, "addOrUpdateFavourite successfully written!") }
      .addOnFailureListener { e -> Log.w(TAG, "Error writing document addOrUpdateFavourite", e) }
    quotes[listIndex].favourite = !favourite
    _quotations.value = quotes
    var indexMyFavouriteDataChanged = -1
    myFavouriteList.forEachIndexed { index, favouriteData ->
      if (favouriteData.documentId == documentId) {
        indexMyFavouriteDataChanged = index
      }
    }
    if (indexMyFavouriteDataChanged >= 0) {//remove
      myFavouriteList.removeAt(indexMyFavouriteDataChanged)
    } else {
      val id = quotes[listIndex].id;
      val imagUrl = quotes[listIndex].image_url
      val text = quotes[listIndex].positivity_text
      myFavouriteList.add(FavouriteData(id, imagUrl, text))
    }
    _favourite.value = myFavouriteList
  }

   fun downloadInGallery(documentId: String){

   }
//   fun removeFavouriteItem(documentId : String){
//     db.collection(FIRESTORE_DB_FAVOURITE).document(MainActivity.DEVICE_ID)
//       .update(hashMapOf<String, Any>(
//         documentId to FieldValue.delete()
//       ))
//       .addOnSuccessListener { Log.d(TAG, "removeFavouriteItem successfully deleted!") }
//       .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
//   }


}
