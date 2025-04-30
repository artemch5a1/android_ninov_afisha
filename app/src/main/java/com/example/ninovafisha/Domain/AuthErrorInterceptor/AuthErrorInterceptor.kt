package com.example.ninovafisha.Domain.AuthErrorInterceptor

import android.content.Context
import android.content.Intent
import androidx.navigation.NavHostController
import coil.intercept.Interceptor
import com.example.ninovafisha.Domain.Constant
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.mfa.FactorType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.Response


suspend fun AuthErrorInterceptor(){
    if(!isTokenValid()){
        Constant.supabase.auth.signOut()
    }
}


fun isTokenValid(): Boolean {
    val session = Constant.supabase.auth.currentSessionOrNull()
    return session?.accessToken != null && session.expiresAt.epochSeconds != 0L
}