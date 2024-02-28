package com.example.tdah

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tdah.ui.theme.TDAHTheme
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import java.io.File

class MainActivity : ComponentActivity() {

    var autorisation: String? = null
    // TODO : vérifier que c'est le bon type

    private lateinit var sensorManager: SensorManager

    override fun onResume() {
        super.onResume()
        startGyroscopeListener()
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val start: String = "Bienvenue"
        val idFile = getDir("autorisation", 0)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        setContent {
            SensorDataCollector()
            TDAHTheme {
                // A surface container using the 'background' color from the theme
                val navController = rememberNavController()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                }
                NavHost(
                    navController = navController,
                    startDestination = start
                ) {
                    composable("Bienvenue") {
                        LaunchedEffect(Unit) {
                            // Utilisez LaunchedEffect pour effectuer des actions au premier lancement du composable
                            startGyroscopeListener()
                        }
                        Bienvenue(navController)
                    }
                    composable("Demande_collecte") {
                        // gestion de l'écran d'acceuil
                        if (!File(idFile, "autorisation.txt").exists()) {
                            Demande_collecte(navController, idFile)
                        } else {
                            // TODO : quand on a accepté on ne peut pas revenir en arrière (idée : créer une nouvelle page qui repose la question et qui supprime le fichier si on veut arrêter la collecte de données)
                            autorisation = File(getDir("autorisation", 0), "autorisation.txt").readText();
                                if( autorisation == "1") {
                                    //navController.navigate("Accord_collecte")
                                    startGyroscopeListener()
                                    Accord_collecte(navController)
                                }else{
                                    Demande_collecte(navController, idFile)
                                }
                        }
                    }
                    composable("Refus_collecte") {
                        Refus_collecte(navController, idFile)
                    }
                    composable("Accord_collecte") {
                        LaunchedEffect(Unit) {
                            // Utilisez LaunchedEffect pour effectuer des actions au premier lancement du composable
                            startGyroscopeListener()
                        }
                        Accord_collecte(navController)
                    }
                    composable("Annuler") {
                        Annuler(navController, idFile)
                    }
                }
            }
        }
    }

    @Composable
    fun SensorDataCollector() {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            // Your Compose UI goes here

            // Example: Displaying collected sensor data in Compose Text
            Text(text = "Sensor Data: XYZ")
        }
    }

    private fun startGyroscopeListener() {

        if (autorisation == "1") {
            val gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
            val gyroscopeListener = object : SensorEventListener {
                override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                    // Gérer les changements de précision si nécessaire
                }

                override fun onSensorChanged(event: SensorEvent?) {
                    // Traiter les données du gyroscope et les stocker dans la base de données
                    val xAngularVelocity = event?.values?.get(0) ?: 0f
                    val yAngularVelocity = event?.values?.get(1) ?: 0f
                    val zAngularVelocity = event?.values?.get(2) ?: 0f

                    Log.d(
                        "Gyroscope",
                        "x : $xAngularVelocity, y : $yAngularVelocity, z : $zAngularVelocity"
                    )

                    // Stocker les données dans la base de données en utilisant des corroutines
                    lifecycleScope.launch {
                        // database.sensorDataDao().insertSensorData(SensorData(xAngularVelocity, yAngularVelocity, zAngularVelocity))
                    }
                }
            }
            sensorManager.registerListener(
                gyroscopeListener,
                gyroscopeSensor,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }
    }
}
