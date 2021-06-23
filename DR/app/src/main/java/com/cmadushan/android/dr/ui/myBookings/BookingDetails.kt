package com.cmadushan.android.dr.ui.myBookings

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BookingDetails (
    val Name : String,
    val Address:String,
    val Email :String,
    val Tp : String,
    val DocName : String,
    val Specialization :String,
    val Hospital:String,
    val Time : Timestamp
        ) : Parcelable