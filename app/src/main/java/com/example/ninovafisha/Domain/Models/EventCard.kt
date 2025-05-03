package com.example.ninovafisha.Domain.Models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class EventCard (
    val id:String? = null,
    val title:String = "",
    val desc:String = "",
    val image:String? = null,
    @SerialName("type_event")
    val typeEvent:Int = -1,
    @SerialName("date_start")
    val date:String? = null,
    val cost:Float? = 0f,
    @SerialName("age_const")
    val ageConst:Int = 14,
    val rating:Float? = null,
    val author:String? = null,
    @SerialName("Long_desc")
    val descLong:String? = ""
)