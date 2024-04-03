package com.example.tdah

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.example.tdah.ui.theme.TDAHTheme
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.util.UUID


class MainActivity : ComponentActivity() {

    var autorisation: String? = null
    // TODO : vérifier que c'est le bon type

    private lateinit var idFileWorker: File
    private lateinit var idFilePatient: File

    //var sharedpreferences: SharedPreferences? = null
    //val editor = null

    //val preferencesManager =  PreferencesManager(this@MainActivity)

    private var readSensorRequest: WorkRequest? = null

    /*
    private fun createWorkRequest() {
        readSensorRequest = OneTimeWorkRequestBuilder<WorkerCapteurs>().addTag("workerTag").build()
    }

    private fun startWork() {
        createWorkRequest()
        readSensorRequest?.let { request ->
            WorkManager.getInstance(baseContext).enqueue(request)
        }
        readSensorRequest?.id?.let { requestId ->
            idFileWorker = File(getDir("autorisation", 0), "workerId.txt")
            val id = requestId.toString()
            idFileWorker.writeText(id)
        }
    }
     */

    private fun startWork() {
        val enteredUsername = try {
            idFilePatient.readText()
            println("MainActivity ID =" + idFilePatient.readText())
        } catch (e: FileNotFoundException) {
            "L'identifiant du patient n'a pas été lu : FileNotFound"
        }

        createWorkRequest(idFilePatient.absolutePath, enteredUsername)
        readSensorRequest?.let { request ->
            WorkManager.getInstance(baseContext).enqueue(request)
        }
        readSensorRequest?.id?.let { requestId ->
            idFileWorker = File(getDir("autorisation", 0), "workerId.txt")
            val id = requestId.toString()
            idFileWorker.writeText(id)
        }
    }

    private fun createWorkRequest(idFilePath: String, enteredUsername: Any) {
        val inputData = Data.Builder()
            .putString("enteredUsername", enteredUsername.toString())
            .putString("idFilePatient", idFilePath)
            .build()
        readSensorRequest = OneTimeWorkRequestBuilder<WorkerCapteurs>()
            .addTag("workerTag")
            .setInputData(inputData)
            .build()
    }



    private fun stopWork() {
        try {
            var workerId : String = idFileWorker.readText() // Lire l'ID du travail à partir du fichier
            val uuid: UUID = UUID.fromString(workerId)
            WorkManager.getInstance(baseContext).cancelWorkById(uuid)
            File(getDir("autorisation", 0), "workerId.txt").delete()
        } catch (e: FileNotFoundException) {
            println("Aucun worker n'a été arrêté : Fichier non trouvé")
        } catch (e: IOException) {
            println("Aucun worker n'a été arrêté : Erreur d'entrée/sortie")
        }

    }


    override fun onResume() {
        super.onResume()
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        idFileWorker = File(getDir("autorisation", 0), "workerId.txt")
        idFilePatient = File(getDir("autorisation", 0), "IdPatient.txt")
        val start: String = "Bienvenue"
        val idFile = getDir("autorisation", 0)
        val sharedPreferences = applicationContext.getSharedPreferences("Pref", Context.MODE_PRIVATE);
        autorisation = sharedPreferences.getString("autorisation", "0")

        setContent {
            TDAHTheme {
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

                            if (!File(idFile, "autorisation.txt").exists()) {
                                stopWork()
                            }

                        }
                        Bienvenue(navController)
                    }
                    composable("Demande_collecte") {
                        // gestion de l'écran d'acceuil
                        if (!File(idFile, "autorisation.txt").exists()) {
                            Demande_collecte(navController, idFile)
                        } else {
                            autorisation =
                                File(getDir("autorisation", 0), "autorisation.txt").readText();
                            if (autorisation == "1"){
                                if (!File(getDir("autorisation", 0), "workerId.txt").exists()) {
                                    println("***********************************************************")
                                    startWork()
                                }
                                Accord_collecte(navController)
                            } else {
                                Demande_collecte(navController, idFile)
                            }
                        }
                    }
                    composable("Refus_collecte") {
                        Refus_collecte(navController, idFile)
                    }
                    composable("Accord_collecte") {
                        LaunchedEffect(Unit) {
                        }
                        Accord_collecte(navController)
                    }
                    composable("Annuler") {
                        Annuler(navController, idFile)
                    }
                    composable("SaisieIdentifiant") {
                        SaisieIdentifiant(navController, idFilePatient)
                    }
                }
            }
        }
    }
}
