package com.example.tdah

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.graphics.Color
import com.example.tdah.ui.theme.theme
import java.io.File

@Composable
fun UsernameInputField(
    onUsernameChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var username by remember { mutableStateOf("") }

    OutlinedTextField(
        value = username,
        onValueChange = {
            username = it
            onUsernameChanged(it)
        },
        label = { Text("Identifiant") },
        singleLine = true,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                // You can perform any action here when user presses "Done" on the keyboard
            }
        ),
        isError = username.isEmpty(),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = theme, // Utilisation de la valeur hexadécimale de la couleur définie dans Color.kt
            unfocusedBorderColor = theme
        )
    )
}

@Composable
fun SaisieIdentifiant(navController: NavController, idFilePatient : File) {
    var enteredUsername by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        UsernameInputField(onUsernameChanged = { enteredUsername = it })

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val autorisation = idFilePatient.parentFile
                if (!autorisation.exists()) {
                    autorisation.mkdirs() // Crée le répertoire et tous les répertoires parents s'ils n'existent pas
                }
                println("SaisieIdentifiant idPatient = " + enteredUsername)
                idFilePatient.writeText(enteredUsername)
                navController.navigate("Demande_collecte")
            },
            enabled = enteredUsername.isNotEmpty()
        ) {
            Text(text = "Valider")
        }
    }
}
