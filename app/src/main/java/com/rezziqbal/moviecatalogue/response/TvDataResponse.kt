package com.rezziqbal.moviecatalogue.response

import com.google.gson.annotations.SerializedName

data class TvDataResponse(
    @SerializedName("page")
    var page: Int? = null,
    @SerializedName("total_results")
    var total_results: Int? = null,
    @SerializedName("total_pages")
    var total_pages: Int? = null,
    @SerializedName("results")
    val result: ArrayList<TvResponse>
)