package com.example.test18.Model

import com.google.gson.annotations.SerializedName

data class UserListModel (
    var page: String,
    @SerializedName("per_page")
    var perPage: String,
    var total: String,
    @SerializedName("total_pages")
    var totalPages: String,

    // 실제 데이터 모델를 리스트 타입으로 제너릭으로해서 명시한다.
    var data: List<UserModel>?
    )