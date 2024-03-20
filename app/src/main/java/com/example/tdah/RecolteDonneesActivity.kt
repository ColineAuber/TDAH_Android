package com.example.tdah

import android.app.Activity
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.WindowManager
import android.widget.ImageView
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.findNavController
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.concurrent.fixedRateTimer
import com.example.tdah.databinding.RecolteDonneesActivityBinding

class RecolteDonneesActivity: Activity() , SensorEventListener{

    private lateinit var navController: NavController

    private lateinit var binding: RecolteDonneesActivityBinding

    val stopButton : ImageView = findViewById(R.id.cancelImageView)

    //Initialize sensors
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
    private var stop: Boolean = false
    private val transmit = Transmission_donnees()


    //Set samplingPeriod and send interval
    private val samplingPeriod: Long = 20    //Sensor sampling period in ms
    private val sendInterval: Int = 6000      //Interval between batch of samples send to the server in miliseconds
    //(sendInterval)/samplingPeriod) = fréquence pour l'instant 30Hz*10s

    override fun onCreate(savedInstanceState: Bundle?) {

        // Je veux passer le navController d'une Activity à une autre : MainActivity -> RecolteDonneesActivity
        stopButton.setOnClickListener{
            navController.navigate("Annuler")
        }

        super.onCreate(savedInstanceState)

        binding = RecolteDonneesActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Setup sensors
        sensorSetup()

        //Keep the screen on when transmitting data
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        // token is the time at the start of the recording, using this it is possible to distinguish recording sessions
        val token = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd:HH:mm:ss.SSS"))

        //Find current activity
        val activity = intent.getStringExtra("activity").toString()

        val dataSet = mutableListOf<Donnees>()
        var i=0
        var j = 0

        println("On est rentré dans le onCreate")

        val autorisationDir = File(getDir("autorisation", 0), "autorisation.txt")

        // Vérifiez si le fichier existe
        if (autorisationDir.exists()) {
            // Lisez le contenu du fichier
            val autorisation = autorisationDir.readText()
            fixedRateTimer("timer", true, 100, samplingPeriod) {

                if (autorisation == "1") {
                    val data = Donnees(
                        user = "15",
                        acceX = xaccel,
                        acceY = yaccel,
                        acceZ = zaccel,
                        gyroX = xgyro,
                        gyroY = ygyro,
                        gyroZ = zgyro,
                        bpm = hvalue,
                        token = token,
                        label = activity
                    )
                    //dataSet is empty at first, dataSet.add adds new object to the list.
                    //The next sendInterval the data needs to be overwritten, so dataSet[i] = data is used.
                    if(j == 0){
                        dataSet.add(data)
                    } else {
                        dataSet[i] = data
                    }

                    //Transmit the data every sendInterval
                    if (i == (((sendInterval)/samplingPeriod)-1).toInt()){
                        //Padding the array to 50 samples for testing the battery life
//                    if(j == 0) {
//                        while (i < 49) {
//                            dataSet.add(data)
//                            i++
//                        }
//                    }
                        println("---------------------------------------------------------------------------->")
                        println(dataSet)
                        //transmit.transmitData(dataSet)

                        i=0
                        j=1
                    }else{
                        i++
                    }
                }
            }
        }
    }

    //Sensor setup
    private fun sensorSetup(){

        println("On est rentré dans le sensorSetup()")

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        gyro = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        bpm = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE)

        accel?.also { accelerometer ->
            sensorManager.registerListener(this, accelerometer, (samplingPeriod*1000).toInt())
        }
        gyro?.also { light ->
            sensorManager.registerListener(this, light, (samplingPeriod*1000).toInt())
        }
        bpm?.also { light ->
            sensorManager.registerListener(this, light, (samplingPeriod*1000).toInt())
        }
    }

    // Receive sensor data
    override fun onSensorChanged(event: SensorEvent) {

        // Accelerometer
        if((event.sensor.type == Sensor.TYPE_ACCELEROMETER)) {
            xaccel = event.values[0].toString().toFloat()
            yaccel = event.values[1].toString().toFloat()
            zaccel = event.values[2].toString().toFloat()
        }

        // Gyroscope
        if(event.sensor.type == Sensor.TYPE_GYROSCOPE) {
            xgyro = event.values[0].toString().toFloat()
            ygyro = event.values[1].toString().toFloat()
            zgyro = event.values[2].toString().toFloat()
        }

        // Heartrate
        if(event.sensor.type == Sensor.TYPE_HEART_RATE) {
            hvalue = event.values[0].toString()
        }
    }

    // Detect accuracy change of sensor
    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
    }

    override fun onBackPressed() {}
}

