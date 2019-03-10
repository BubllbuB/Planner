package com.example.planner.task

import android.os.Parcel
import android.os.Parcelable

data class Task(var title: String?, var description: String?, var id: Int = 0, var favorite: Boolean = false, var done: Boolean = false) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        favorite = parcel.readInt() ==1,
        done = parcel.readInt() ==1)

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(title)
        dest.writeString(description)
        dest.writeInt(id)
        dest.writeInt((if (favorite) 1 else 0).toInt())
        dest.writeInt((if (done) 1 else 0).toInt())
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Task> {
        override fun createFromParcel(parcel: Parcel): Task {
            return Task(parcel)
        }

        override fun newArray(size: Int): Array<Task?> {
            return arrayOfNulls(size)
        }
    }
}