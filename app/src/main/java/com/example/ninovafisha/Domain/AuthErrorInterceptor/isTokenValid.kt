package com.example.ninovafisha.Domain.AuthErrorInterceptor

import com.example.ninovafisha.Domain.Constant
import io.github.jan.supabase.auth.auth


fun isTokenValid(): Boolean {
    val session = Constant.supabase.auth.currentSessionOrNull()
    return session?.accessToken != null && session.expiresAt.epochSeconds != 0L
}