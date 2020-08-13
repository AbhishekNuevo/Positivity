package com.abhishekjoshi158.postivity.utilities

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Environment
import android.os.SystemClock
import android.provider.MediaStore
import android.view.View
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*


const val LIKE = "likes"
const val SHARE = "share"
const val FAVOURITE = "favourite"
const val DOWNLOAD = "download"
const val REMOVE = "remove"
const val FIRESTORE_DB_QUOTES = "quotations"
const val FIRESTORE_DB_LIKES = "user_likes"
const val FIRESTORE_DB_FAVOURITE = "user_favourite"
const val STORAGE_REQUEST_CODE = 1001

  fun getURI(context: Context?,bitmap: Bitmap){
    var contentUri : Uri? = null
     if(context == null){
       return
     }
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
    if (contentUri != null ) {
      val shareIntent = Intent()
      shareIntent.action = Intent.ACTION_SEND
      shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // temp permission for receiving app to read this file
      shareIntent.setDataAndType(contentUri,context.getContentResolver().getType(contentUri))
      shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri)
      context.startActivity(Intent.createChooser(shareIntent, "Choose an app"))
    }

  }

fun getBitmapOFView(view: View): Bitmap? {
  val bitmap = Bitmap.createBitmap(
    view.getWidth(),
    view.getHeight(), Bitmap.Config.ARGB_8888
  )
  val canvas = Canvas(bitmap)
  view.draw(canvas)
  return bitmap
}

 fun saveImageToExternalStorage(bitmap:Bitmap,context: Context?):Uri{


  val name = "Image_${System.currentTimeMillis()}.jpg"
    val savedImageURL = MediaStore.Images.Media.insertImage(
      context?.contentResolver,
      bitmap,
      name,
      "image from Zero State"
    )

  // Return the saved image path to uri
  return Uri.parse(savedImageURL)
}

