package com.abhishekjoshi158.postivity.utilities

import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.abhishekjoshi158.postivity.R


fun showBasicDialog(context: Context, message: String): LiveData<Int>{
  val _buttonClicked = MutableLiveData<Int>()
  val buttonClicked : LiveData<Int> = _buttonClicked
  val builder = AlertDialog.Builder(context)
 // builder.setTitle(R.string.basic_dialog)

  builder.setMessage(message)
 // builder.setIcon(android.R.drawable.ic_dialog_alert)

  //performing positive action
  builder.setPositiveButton(R.string.yes){dialogInterface, which ->
    _buttonClicked.value =  R.string.yes
  }
  //performing cancel action
  builder.setNeutralButton(R.string.no){dialogInterface , which ->
    _buttonClicked.value =  R.string.no
  }
  //performing negative action
//  builder.setNegativeButton(R.string.cancel){dialogInterface, which ->
//    _buttonClicked.value =  R.string.cancel
//  }
  // Create the AlertDialog
  val alertDialog: AlertDialog = builder.create()
  // Set other dialog properties
  alertDialog.setCancelable(false)
  alertDialog.show()
   return buttonClicked
}
