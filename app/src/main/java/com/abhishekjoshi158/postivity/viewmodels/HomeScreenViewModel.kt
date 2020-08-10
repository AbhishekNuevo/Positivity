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
  private var homeScreenRepository: HomeScreenRepository
  var observer : Observer<List<PositivityData>>

  init {
    Log.i(TAG,"init 1 ${positivity.value}")
    homeScreenRepository = HomeScreenRepository()
     observer = Observer<List<PositivityData>> {
      _positivities.value = it
    }

    if(positivity.value != null){
      _positivities.value = positivity.value
      Log.i(TAG,"init 2 ${_positivities.value}")
    }else {
      Log.i(TAG,"init 3 ${_positivities.value}")
      homeScreenRepository.quotations.observeForever(observer)
      homeScreenRepository.loadPositivity()
    }
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
