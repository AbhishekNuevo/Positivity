package com.abhishekjoshi158.postivity.datamodels

data class PositivityData(val id: String,val image_url :String,
        val positivity_text : String, var total_likes : Int
)

data class MyLikes(val documentId: String,var liked : Boolean )
