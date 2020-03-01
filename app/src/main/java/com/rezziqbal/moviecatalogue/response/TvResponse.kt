package com.rezziqbal.moviecatalogue.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TvResponse(
    @SerializedName("id")
    var id: Long? = null,
    @SerializedName("name")
    var nama: String? = null,
    @SerializedName("first_air_date")
    var rdate: String? = null,
    @SerializedName("vote_average")
    var vote: Double? = null,
    @SerializedName("overview")
    var deskripsi: String? = null,
    @SerializedName("poster_path")
    var poster: String? = null,
    @SerializedName("backdrop_path")
    var backdrop: String? = null
): Parcelable