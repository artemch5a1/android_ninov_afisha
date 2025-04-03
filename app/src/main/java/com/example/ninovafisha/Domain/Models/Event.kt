package com.example.ninovafisha.Domain.Models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Event (
    val title:String,
    val desc:String,
    val image:String?,
    @SerialName("type_event")
    val typeEvent:Int,
    @SerialName("date_start")
    val date:String,
    val cost:Float
)
