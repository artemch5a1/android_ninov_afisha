package com.example.ninovafisha.Domain

import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.status.SessionSource
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage


object Constant {
    val supabase = createSupabaseClient(
        supabaseUrl = "https://hkhkixcrdnlxjrslbbod.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImhraGtpeGNyZG5seGpyc2xiYm9kIiwicm9sZSI6ImFub24iLCJpYXQiOjE3Mzk3ODg3MjQsImV4cCI6MjA1NTM2NDcyNH0.M2q0DDenKPFWQK3NiyOQoGtwbKa9QOOkPBMbBnEUMVE"
    ) {
        install(Postgrest)
        install(Auth){
            alwaysAutoRefresh = false // Отключаем авто-обновление сессии
            autoLoadFromStorage = false
        }
        install(Storage)
    }
}