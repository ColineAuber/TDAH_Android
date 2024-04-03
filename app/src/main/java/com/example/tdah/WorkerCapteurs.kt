package com.example.tdah

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.SystemClock
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.concurrent.fixedRateTimer

class WorkerCapteurs(context: Context, params: WorkerParameters) : CoroutineWorker(context, params),
    SensorEventListener {

    // Initialize sensors
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
    private var hvalue = "0"

    private val transmit = Transmission_donnees()
    private val samplingPeriod: Long = 20
    private val sendInterval: Int = 6000

    val preferencesManager =  PreferencesManager(applicationContext)

    init {
        // Code exécuté lors de la création du Worker
        sensorManager = applicationContext.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        gyro = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        bpm = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE)

        accel?.also { accelerometer ->
            sensorManager.registerListener(this, accelerometer, (samplingPeriod * 1000).toInt())
        }
        gyro?.also { light ->
            sensorManager.registerListener(this, light, (samplingPeriod * 1000).toInt())
        }
        bpm?.also { light ->
            sensorManager.registerListener(this, light, (samplingPeriod * 1000).toInt())
        }
    }


    override suspend fun doWork(): Result {
        // Token is the time at the start of the recording
        val token =
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd:HH:mm:ss.SSS"))

        // Find current activity
        val activity = inputData.getString("activity") ?: ""

        val idFilePath = inputData.getString("idFilePatient")
        val idFilePatient = if (!idFilePath.isNullOrEmpty()) File(idFilePath) else null
        val enteredUsername = inputData.getString("enteredUsername") ?: ""

        val fileContent = idFilePatient?.takeIf { it.exists() }?.readText() ?: ""

        // Utilisez le contenu du fichier ici
        println("Contenu du fichier idFilePatient dans WorkerCapteurs : $fileContent")

        val dataSet = mutableListOf<Donnees>()
        var i = 0
        var j = 0

        fixedRateTimer("timer", true, 100, samplingPeriod) {
            val data = Donnees(
                montre_id = fileContent,
                accX = xaccel,
                accY = yaccel,
                accZ = zaccel,
                gyrX = xgyro,
                gyrY = ygyro,
                gyrZ = zgyro,
                bpm = hvalue,
                token = token,
                label = activity
            )

            if (isStopped) {
                this.cancel()
            }

            if (j == 0) {
                dataSet.add(data)
            } else {
                dataSet[i] = data
            }

            if (i == (((sendInterval) / samplingPeriod) - 1).toInt()) {
                println("-------------------------------------------->$this")
                println(dataSet)
                transmit.transmitData(dataSet)

                i = 0
                j = 1
            } else {
                i++
            }
        }

        while (true) SystemClock.sleep(10000)
        return Result.success()

    }

    override fun onSensorChanged(event: SensorEvent) {

        if (isStopped) sensorManager.unregisterListener(this)

        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            xaccel = event.values[0]
            yaccel = event.values[1]
            zaccel = event.values[2]
        }

        if (event.sensor.type == Sensor.TYPE_GYROSCOPE) {
            xgyro = event.values[0]
            ygyro = event.values[1]
            zgyro = event.values[2]
        }

        if (event.sensor.type == Sensor.TYPE_HEART_RATE) {
            hvalue = event.values[0].toString()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Not used in this example
    }

}
