package com.example.tdah

import com.squareup.moshi.JsonClass
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@JsonClass(generateAdapter = true)
data class Donnees(
    val timestamp: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd:HH:mm:ss.SSS")),
    val montre_id: String = "",
    val accX: Float? = null,
    val accY: Float? = null,
    val accZ: Float? = null,
    val gyrX: Float? = null,
    val gyrY: Float? = null,
    val gyrZ: Float? = null,
    val bpm: String? = null,
    val token: String? = null,
    val label: String? = null
)