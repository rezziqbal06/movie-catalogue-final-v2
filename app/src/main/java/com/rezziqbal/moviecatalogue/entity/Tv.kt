package com.rezziqbal.moviecatalogue.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Tv(
    var id: Long? = null,
    var nama: String? = null,
    var rdate: String? = null,
    var vote: Double? = null,
    var deskripsi: String? = null,
    var poster: String? = null,
    var backdrop: String? = null,
    var is_favorite: String? = null
): Parcelable