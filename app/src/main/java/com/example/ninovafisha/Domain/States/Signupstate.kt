package com.example.ninovafisha.Domain.States

import kotlinx.serialization.SerialName

data class Signupstate (
    val email: String = "",
    val password: String = "",
    val confirmPass: String = "",
    val name:String = "",
    val surname: String = "",
    val datebith:String? = null
)