package com.abhishekjoshi158.postivity.utilities

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.URI


const val LIKE = "likes"
const val SHARE = "share"
const val SAVE = "save"
const val FIRESTORE_DB_QUOTES = "quotations"
const val FIRESTORE_DB_LIKES = "user_likes"


  fun getURI(context: Context,bitmap: Bitmap):Uri?{
    var contentUri : Uri? = null
    try {
      val cachePath = File(context.cacheDir, "images")
      cachePath.mkdirs() // don't forget to make the directory
      val stream =
        FileOutputStream(cachePath.toString() + "/image.png") // overwrites this image every time
      bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
      stream.close()

      val imagePath = File(context.cacheDir, "images")
      val newFile = File(imagePath, "image.png")
       contentUri =
        FileProvider.getUriForFile(context, "com.abhishekjoshi158.postivity.fileprovider", newFile)
    } catch (e: IOException) {
      e.printStackTrace()
    }
    return contentUri
  }

