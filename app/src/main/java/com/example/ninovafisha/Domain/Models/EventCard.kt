package com.example.ninovafisha.Domain.Models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class EventCard (
    val id:String,
    val title:String = "",
    val desc:String = "",
    val image:String? = null,
    @SerialName("type_event")
    val typeEvent:Int = -1,
    @SerialName("date_start")
    val date:String? = null,
    val cost:Float? = null,
    @SerialName("age_const")
    val ageConst:Int = 14,
    val rating:Float = 7f,
    val author:String? = null
)