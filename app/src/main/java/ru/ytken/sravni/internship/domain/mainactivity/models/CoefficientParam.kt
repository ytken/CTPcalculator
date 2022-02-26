package ru.ytken.sravni.internship.domain.mainactivity.models

import android.os.Parcel
import android.os.Parcelable

data class CoefficientParam (
    val title: String,
    val headerValue: String,
    val name: String,
    val detailText: String,
    val value: String
    ) : Parcelable {
    constructor(parcel: Parcel) : this (
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(headerValue)
        parcel.writeString(name)
        parcel.writeString(detailText)
        parcel.writeString(value)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CoefficientParam> {
        override fun createFromParcel(parcel: Parcel): CoefficientParam {
            return CoefficientParam(parcel)
        }

        override fun newArray(size: Int): Array<CoefficientParam?> {
            return arrayOfNulls(size)
        }
    }
}