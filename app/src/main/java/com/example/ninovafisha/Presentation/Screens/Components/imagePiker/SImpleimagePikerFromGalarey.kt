package com.example.ninovafisha.Presentation.Screens.CardEventScreen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun SimpleImagePicker(
    onImagePicked: (Uri?) -> Unit // Колбэк для возврата Uri
) {

        val context = LocalContext.current
        var imageUri by remember { mutableStateOf<Uri?>(null) }

        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            imageUri = uri
            onImagePicked(uri) // Передаём выбранный Uri наружу
        }


            Button(modifier = Modifier,onClick = { launcher.launch("image/*") }) {
                Text("Выбрать из галереи")
            }

            imageUri?.let { uri ->
                AsyncImage(
                    model = uri,
                    contentDescription = "Выбранное изображение",
                    modifier = Modifier.size(200.dp)
                )
            }


}