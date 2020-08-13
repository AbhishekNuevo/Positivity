package com.abhishekjoshi158.postivity.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.abhishekjoshi158.postivity.datamodels.FavouriteData
import com.abhishekjoshi158.postivity.datamodels.PositivityData
import com.abhishekjoshi158.postivity.repository.HomeScreenRepository


class HomeScreenViewModel : ViewModel() {
  private val TAG = HomeScreenViewModel::class.java.simpleName
  private val _positivities = MutableLiveData<List<PositivityData>>()
  val positivity: LiveData<List<PositivityData>> = _positivities
  private val _favourite = MutableLiveData<List<FavouriteData>>()
  val favourite : LiveData<List<FavouriteData>> = _favourite
  private var homeScreenRepository: HomeScreenRepository = HomeScreenRepository()
  private var observer : Observer<List<PositivityData>>
  private var favouriteObserver : Observer<List<FavouriteData>>
  private val _myLikes = MutableLiveData<Map<String,Boolean>>()
  val myLike : LiveData<Map<String,Boolean>> = _myLikes
  init {

    observer  = Observer<List<PositivityData>> {
      _positivities.value = it
    }

   var likeObserver = Observer<Map<String,Boolean>>{
     _myLikes.value = it
   }
    favouriteObserver = Observer {
      _favourite.value = it
    }
    homeScreenRepository.favourite.observeForever(favouriteObserver)
    homeScreenRepository.myLikes?.observeForever(likeObserver)
    homeScreenRepository.quotations.observeForever(observer)
    homeScreenRepository.loadPositivity()
  }

  fun updateLike(documentId:String){
    homeScreenRepository.updateLike(documentId)
  }

  fun updateFavourite(documentId:String){
    homeScreenRepository.addOrUpdateFavourite(documentId)
  }


  override fun onCleared() {
    Log.i(TAG,"onCleared")
    homeScreenRepository.quotations.removeObserver(observer)
    homeScreenRepository.favourite.removeObserver(favouriteObserver)
    super.onCleared()
  }

}
