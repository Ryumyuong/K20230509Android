package com.example.test_pdtest.Model

import com.google.gson.annotations.SerializedName

data class UserListModel (
    //var data: List<ItemModel>?
    var getFestivalKr: GetFestivalKr
)
data class GetFestivalKr (
    var item : List<UserModel>
)

data class UserModel (
    @SerializedName("MAIN_IMG_NORMAL")
    var mainImgNormal :String,
    @SerializedName("SUBTITLE")
    var subTitle:String,
    @SerializedName("TITLE")
    var title:String
)