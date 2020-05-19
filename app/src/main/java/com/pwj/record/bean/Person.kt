package com.pwj.record.bean

import android.os.Parcel
import android.os.Parcelable

/**
 * Author:           pwj
 * Date:             2020/4/14 15:48
 * FileName:         Person
 * description:      description
 */
data class Person(val name: String) : Parcelable {
    constructor(source: Parcel) : this(
        source.readString().toString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(name)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Person> = object : Parcelable.Creator<Person> {
            override fun createFromParcel(source: Parcel): Person = Person(source)
            override fun newArray(size: Int): Array<Person?> = arrayOfNulls(size)
        }
    }
}