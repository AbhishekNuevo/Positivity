package com.abhishekjoshi158.postivity.interfaces

import android.view.View

interface FavouriteInterface {
  fun removeItem(documentId: String)
  fun downloadItem(view: View)
}
