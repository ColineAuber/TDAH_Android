package com.example.tdah

import android.os.Build
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class Donnees (
        val timestamp: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd:HH:mm:ss.SSS")),
        val user: String? = null,
        val acceX: Float? = null,
        val acceY: Float? = null,
        val acceZ: Float? = null,
        val gyroX: Float? = null,
        val gyroY: Float? = null,
        val gyroZ: Float? = null,
        val bpm: String? = null,
        val token: String? = null,
        val label: String? = null
)