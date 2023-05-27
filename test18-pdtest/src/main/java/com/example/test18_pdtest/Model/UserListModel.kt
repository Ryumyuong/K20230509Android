package com.example.test18_pdtest.Model

import com.example.test18_pdtest.Model.UserModel
import com.google.gson.annotations.SerializedName
data class UserListModel (
    //var data: List<ItemModel>?
    var getFoodKr: GetFoodKr
)
data class GetFoodKr (
    var item : List<UserModel>
)