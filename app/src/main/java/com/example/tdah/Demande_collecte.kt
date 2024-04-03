package com.example.tdah

import android.content.Context
import android.preference.PreferenceManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import com.example.tdah.ui.theme.green
import com.example.tdah.ui.theme.red
import com.example.tdah.ui.theme.theme
import java.io.File

@Composable
fun Demande_collecte(navController: NavController, idFile : File) {
    //val context = LocalContext.current
    //val preferencesManager = remember { PreferencesManager(context) }
    //val sharedPreferences = context.getSharedPreferences("Pref", Context.MODE_PRIVATE);
    //val editor = sharedPreferences.edit()

    Surface() {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
            )
            {
                Box(
                    modifier = Modifier
                        .size(400.dp, 60.dp)
                        .background(color = theme),

                    ) {
                }
            }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(
                        text = "Autorisez vous la collecte des données?",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 20.dp, bottom = 20.dp)
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column {
                        Button(
                            onClick = {
                                //editor.putString("autorisation", "1")
                                //editor.apply()
                                navController.navigate("Accord_collecte");
                                //preferencesManager.saveData("autorisation", "1")
                                File(idFile, "autorisation.txt").writeText("1")
                            },
                            colors = buttonColors(green)
                        ) {
                            Text(
                                text = "Oui",
                                fontSize = 10.sp
                            )
                        }
                    }
                    Column {
                        Text(
                            text = "",
                            modifier = Modifier.padding(start = 10.dp)
                        )
                    }
                    Column {
                        Button(
                            onClick = {
                                navController.navigate("Refus_collecte");
                                File(idFile, "autorisation.txt").writeText("0")
                            },
                            colors = buttonColors(red)
                        ) {
                            Text(
                                text = "Non",
                                fontSize = 10.sp
                            )
                        }
                    }

                }
            }
        }
    }

