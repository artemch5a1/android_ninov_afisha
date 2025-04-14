package com.example.ninovafisha.Domain.Models

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Profile (
    val name:String,
    val surname: String,
    @SerialName("datebirth")
    val datebith:String?,
    val id:String?,
    val role:Int = 1
)