package com.example.tdah

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import androidx.core.content.ContextCompat.getSystemService

class Recolte_donnees {

    private lateinit var sensorManager: SensorManager

    private var accel: Sensor? = null
    private var gyro: Sensor? = null
    private var bpm: Sensor? = null

    private var xaccel: Float = 0F
    private var yaccel: Float = 0F
    private var zaccel: Float = 0F
    private var xgyro: Float = 0F
    private var ygyro: Float = 0F
    private var zgyro: Float = 0F
    private var valueBpm = "0"


    fun Initialise(){
        // initialise les capteurs

       // sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        gyro = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        bpm = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE)

    }

    fun Donnees_instant(event: SensorEvent) {
        // récupère les données à un instant t

            // Accelerometer
            xaccel = event.values[0].toString().toFloat()
            yaccel = event.values[1].toString().toFloat()
            zaccel = event.values[2].toString().toFloat()

            // Gyroscope
            xgyro = event.values[0].toString().toFloat()
            ygyro = event.values[1].toString().toFloat()
            zgyro = event.values[2].toString().toFloat()


            // Heartrate
            valueBpm = event.values[0].toString()
    }

    fun Recuperation_donnees(freq: Int, boolGyro: Boolean, boolAccel: Boolean, boolBpm: Boolean){
        // freq : fréquence de récupération des données
        // boolGyro : true= on récupère les données du capteur gyroscope
        // récupère les données en fonction fréquence/ capteurs souhaités avec la fonction Données_instant()

    }

}