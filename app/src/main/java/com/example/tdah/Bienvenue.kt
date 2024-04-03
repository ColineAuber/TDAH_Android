package com.example.tdah

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.R
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.tdah.ui.theme.green
import com.example.tdah.ui.theme.theme
import com.example.tdah.ui.theme.white

@Composable
fun Bienvenue(navController: NavController) {
    Surface() {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
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
                    text = "Bienvenue",
                    textAlign = TextAlign.Center,
                    color = theme

                    )
                Button(
                    onClick = { navController.navigate("SaisieIdentifiant") },
                    colors = buttonColors(white)
                ) {
                    Image(
                        painterResource(id = com.example.tdah.R.drawable.fleche),
                        contentDescription = "Fleche - suivant",
                        modifier = Modifier
                            .size(15.dp)
                            .padding(start = 5.dp, top = 2.dp)
                    )
                }
            }

        }
    }
}