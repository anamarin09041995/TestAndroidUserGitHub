package com.anama.androidtest.data.model

import android.annotation.SuppressLint
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
@Entity
class User: Parcelable{

    @PrimaryKey(autoGenerate = true)
    var idUser: Long? = null
    @SerializedName("avatar_url")
    var avatarUrl: String? = null
    var name: String? = null

}