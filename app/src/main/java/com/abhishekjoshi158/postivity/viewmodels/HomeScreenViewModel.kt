package com.abhishekjoshi158.postivity.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.abhishekjoshi158.postivity.datamodels.PositivityData
import com.abhishekjoshi158.postivity.repository.HomeScreenRepository


class HomeScreenViewModel : ViewModel() {
  private val TAG = HomeScreenViewModel::class.java.simpleName
  private val _positivities = MutableLiveData<List<PositivityData>>()
  val positivity: LiveData<List<PositivityData>> = _positivities
  private var homeScreenRepository: HomeScreenRepository = HomeScreenRepository()
  private var observer : Observer<List<PositivityData>>

  private val _myLikes = MutableLiveData<Map<String,Boolean>>()
  val myLike : LiveData<Map<String,Boolean>> = _myLikes
  init {

    observer  = Observer<List<PositivityData>> {
      _positivities.value = it
    }
   var likeObserver = Observer<Map<String,Boolean>>{
     _myLikes.value = it
   }
    homeScreenRepository.myLikes?.observeForever(likeObserver)
    homeScreenRepository.quotations.observeForever(observer)
    homeScreenRepository.loadPositivity()
  }

  fun updateLike(documentId:String){
    homeScreenRepository.updateLike(documentId)
  }

  override fun onCleared() {
    Log.i(TAG,"onCleared")
    homeScreenRepository.quotations.removeObserver(observer)
    super.onCleared()
  }

}
