package com.example.tdah

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.lifecycleScope
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.lifecycleScope
import com.example.tdah.ui.theme.theme
import com.example.tdah.ui.theme.white
import kotlinx.coroutines.launch


@Composable
fun Accord_collecte(navController: NavController) {
    Surface() {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly) {
                Box( modifier = Modifier
                    .size(400.dp, 150.dp)
                    .background(color = theme),

                    ) {
                    Image(
                        painterResource(id = com.example.tdah.R.drawable.logo),
                        contentDescription = "Logo TDAH Connect",
                        modifier = Modifier
                            .size(250.dp)
                            .clip(CircleShape)
                    )
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = "Tout est en ordre",
                    textAlign = TextAlign.Center,
                    color = theme,
                    modifier = Modifier.padding(10.dp)

                    )
            }
            Row(){
                Button(
                    onClick = { navController.navigate("Annuler") },
                    colors = ButtonDefaults.buttonColors(white)
                ) {
                    Image(
                        painterResource(id = R.drawable.effacer),
                        contentDescription = "Annuler",
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                    )
                }
            }

        }
    }
}