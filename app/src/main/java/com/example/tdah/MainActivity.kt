package com.example.tdah

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
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
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.example.tdah.ui.theme.TDAHTheme
import com.google.common.util.concurrent.ListenableFuture
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.util.UUID
import java.util.concurrent.ExecutionException


class MainActivity : ComponentActivity() {

    var autorisation: String? = null
    // TODO : vérifier que c'est le bon type

//SINGER CODE
    /*
    private val readSensorRequest: WorkRequest =
        OneTimeWorkRequestBuilder<WorkerCapteurs>()
            .build()

    private fun startWork() {
        WorkManager
            .getInstance(baseContext)
            .enqueue(readSensorRequest)

    }

    private fun stopWork() {
        WorkManager.getInstance(baseContext).cancelWorkById(readSensorRequest.id)
    }
*/
    private lateinit var idFileWorker: File

    private var readSensorRequest: WorkRequest? = null

    private fun createWorkRequest() {
        readSensorRequest = OneTimeWorkRequestBuilder<WorkerCapteurs>().addTag("workerTag").build()
        // readSensorRequest = OneTimeWorkRequestBuilder<WorkerCapteurs>().build()
    }

    private fun startWork() {
        createWorkRequest()
        readSensorRequest?.let { request ->
            WorkManager.getInstance(baseContext).enqueue(request)
            /*
            val texte = request.toString()
            idFileWorker.writeText(texte)
             */
        }
        readSensorRequest?.id?.let { requestId ->
            idFileWorker = File(getDir("autorisation", 0), "workerId.txt")
            val id = requestId.toString()
            idFileWorker.writeText(id)
            println("Etape 0 ok / workerId =" + requestId)
        }
    }

    private fun stopWork() {
        println("SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSStop")
        try {
            var workerId : String = idFileWorker.readText() // Lire l'ID du travail à partir du fichier
            println("Etape 1 ok / workerId =" + workerId)
            val uuid2: UUID = UUID.nameUUIDFromBytes(workerId.toByteArray())
            val uuid: UUID = UUID.fromString(workerId)
            println("Etape 2 ok / uuid =" + uuid)
            WorkManager.getInstance(baseContext).cancelWorkById(uuid)
            File(getDir("autorisation", 0), "workerId.txt").delete()
            println("Etape 3 ok")
        } catch (e: FileNotFoundException) {
            println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& Aucun worker n'a été arrêté : Fichier non trouvé")
        } catch (e: IOException) {
            println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& Aucun worker n'a été arrêté : Erreur d'entrée/sortie")
        }

        /*
        readSensorRequest?.id?.let { requestId ->
            WorkManager.getInstance(baseContext).cancelWorkById(requestId)
        }

         */

        //File(idFileWorker, "workerId.txt").delete()

            /*

        readSensorRequest?.id?.let { requestId ->
            //WorkManager.getInstance(baseContext).cancelWorkById(requestId)
            WorkManager.getInstance().cancelAllWork()
        }

             */
    }


    override fun onResume() {
        super.onResume()
    }

    // Ibrahim

    /*
    private fun isWorkScheduled(tag: String): Boolean {
        var context : Context = baseContext
        val instance = WorkManager.getInstance(context)
        val statuses = instance.getWorkInfosByTag(tag)
        return try {
            var running = false
            val workInfoList = statuses.get()
            for (workInfo in workInfoList) {
                val state: WorkInfo.State = workInfo.state
                running = (state == WorkInfo.State.RUNNING) or (state == WorkInfo.State.ENQUEUED)
            }
            running
        } catch (e: ExecutionException) {
            e.printStackTrace()
            false
        } catch (e: InterruptedException) {
            e.printStackTrace()
            false
        }
    }

private fun isWorkScheduled2(tag: String, context: Context): Boolean {
    val instance = WorkManager.getInstance()
    val statuses: ListenableFuture<List<WorkInfo>> = instance.getWorkInfosByTag(tag)

    var running = false
    var workInfoList: List<WorkInfo> = emptyList() // Singleton, no performance penalty

    try {
        workInfoList = statuses.get()
    } catch (e: ExecutionException) {
        Log.d(TAG, "ExecutionException in isWorkScheduled: $e")
    } catch (e: InterruptedException) {
        Log.d(TAG, "InterruptedException in isWorkScheduled: $e")
    }

    for (workInfo in workInfoList) {
        val state = workInfo.state
        running = running || (state == WorkInfo.State.RUNNING || state == WorkInfo.State.ENQUEUED)
    }
    return running
}


     */
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        idFileWorker = File(getDir("autorisation", 0), "workerId.txt")
        val start: String = "Bienvenue"
        val idFile = getDir("autorisation", 0)

        setContent {
            //SensorDataCollector()
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
                            /*
                                if (!isWorkScheduled2("workerTag", baseContext)) {
                                    //if (!File(idFileWorker, "workerId.txt").exists()) {
                                    println("***********************************************************")
                                    startWork()
                                }

                                 */
                                //navController.navigate("Accord_collecte")
                                // startGyroscopeListener()
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
                            // Utilisez LaunchedEffect pour effectuer des actions au premier lancement du composable
                            // startGyroscopeListener()
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
}
