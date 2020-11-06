package com.gebeya.parkingspot

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue
import java.io.Serializable

data class Nearest1(
    val company: Company,
    val floor: Int,
    val full_status: Boolean,
    val _id: String,
    val parking_slots: Int,
    val parking_lot_rank: Int,
    val price: Int
) : Serializable

data class Company(val  name:String):Serializable

