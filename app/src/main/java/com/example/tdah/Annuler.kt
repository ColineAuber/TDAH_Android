package com.example.tdah

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import com.example.tdah.ui.theme.green
import com.example.tdah.ui.theme.red
import com.example.tdah.ui.theme.theme
import java.io.File

@Composable
fun Annuler(navController: NavController, idFile : File) {
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
                    text = "Souhaitez vous annuler la collecte des données?",
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
                            navController.navigate("Accord_collecte");
                        },
                        colors = buttonColors(green)
                    ) {
                        Text(
                            text = "Non",
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
                            navController.navigate("Bienvenue");
                            File(idFile, "autorisation.txt").delete()
                        },
                        colors = buttonColors(red)
                    ) {
                        Text(
                            text = "Oui",
                            fontSize = 10.sp
                        )
                    }
                }

            }
        }
    }
}

