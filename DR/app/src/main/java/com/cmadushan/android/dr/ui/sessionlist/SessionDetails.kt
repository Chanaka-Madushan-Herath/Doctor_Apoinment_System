package com.cmadushan.android.dr.ui.sessionlist

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SessionDetails(
    val doctorId: String,
    val timestamp: Timestamp
) : Parcelable
