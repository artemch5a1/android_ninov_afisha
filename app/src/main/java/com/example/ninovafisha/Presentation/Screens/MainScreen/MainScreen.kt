package com.example.ninovafisha.Presentation.Screens.MainScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.zIndex
import androidx.room.util.copy
import coil.compose.AsyncImage
import com.example.ninovafisha.Domain.States.ActualState
import com.example.ninovafisha.Presentation.Screens.Components.EventCard
import com.example.ninovafisha.Presentation.Screens.Components.myField
import com.example.ninovafisha.Presentation.Screens.Components.myFieldSearch

// Или комбинированный импорт:

@Composable
fun MainScreen(controlNav: NavHostController, viewModelMainScreen: ViewModelMainScreen = viewModel()) {
    val textSearch = remember { mutableStateOf("") }

    val actualState by viewModelMainScreen.actualState.collectAsState()
    val events = viewModelMainScreen.events.observeAsState(emptyList())
    val types = viewModelMainScreen.types.observeAsState(emptyList())

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopStart
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White) // Фиксированная высота
                .padding(top = 40.dp)
                .zIndex(1f), // Гарантирует, что будет поверх других элементов
            contentAlignment = Alignment.TopEnd
        ) {
            Row(){
                myFieldSearch(myText = "Найти", text = textSearch.value, onValueChange = {it -> textSearch.value = it; viewModelMainScreen.filtevent(textSearch.value)})
                Button(
                    onClick = { viewModelMainScreen.refresh() },
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .zIndex(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Gray,
                        contentColor = Color.White
                    )
                ) {
                    Text("Обновить", fontSize = 18.sp)
                }
            }

        }
        Box(
            modifier = Modifier.padding(top = 34.dp)
        ){
            when(actualState){
                is ActualState.Initialized ->{

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 60.dp)
                    ) {
                        items(events.value.indices.count()) { index ->
                            EventCard(event = events.value[index].copy())
                        }
                    }
                }
                is ActualState.Error ->{
                    Text(
                        text = (actualState as ActualState.Error).message,
                        fontSize = 32.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.W800,
                        modifier = Modifier
                            .padding(bottom = 16.dp)
                    )
                }
                is ActualState.Loading ->{
                    CircularProgressIndicator(
                        modifier = Modifier.size(48.dp), // Размер индикатора
                        color = Color.Blue, // Цвет индикатора
                        strokeWidth = 4.dp // Толщина линии
                    )
                }
                is ActualState.Success ->{
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(events.value.indices.count()) { index ->
                            EventCard(event = events.value[index].copy())
                        }
                    }
                }
            }
        }
}



























    /*var greetingText by remember { mutableStateOf<String?>(null) }



    LaunchedEffect(Unit) {
        val part1 = viewModelMainScreen.GetName() ?: ""
        greetingText = part1
    }*/

    /*Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text(
                text = "Привет, " + greetingText,
                fontSize = 32.sp,
                color = Color.Black,
                fontWeight = FontWeight.W800,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .align(alignment = Alignment.CenterHorizontally)
            )

            Button(
                onClick = {
                    controlNav.navigate("sigin")
                    viewModelMainScreen.GetOut()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Gray,
                    contentColor = Color.White
                ),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {

                Text(text = "Назад", fontSize = 18.sp)
            }
        }
    }*/
}