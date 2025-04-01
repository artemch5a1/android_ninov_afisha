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
import androidx.compose.foundation.layout.width
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
import com.example.ninovafisha.Presentation.Screens.Components.TypeBut
import com.example.ninovafisha.Presentation.Screens.Components.myField
import com.example.ninovafisha.Presentation.Screens.Components.myFieldSearch



@Composable
fun MainScreen(controlNav: NavHostController, viewModelMainScreen: ViewModelMainScreen = viewModel()) {
    val textSearch = remember { mutableStateOf("") }
    val actualState by viewModelMainScreen.actualState.collectAsState()
    val events = viewModelMainScreen.events.observeAsState(emptyList())
    val types = viewModelMainScreen.types.observeAsState(emptyList())


    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .zIndex(1f)
        ) {
            Column {
                Row(
                    modifier = Modifier.padding(top = 40.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    myFieldSearch(
                        myText = "Найти",
                        text = textSearch.value,
                        onValueChange = {
                            textSearch.value = it
                            viewModelMainScreen.filtevent(it)
                        },
                        OnClick = { viewModelMainScreen.refresh() }
                    )
                }

                LazyRow(
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    items(types.value.size) { index ->
                        TypeBut(
                            typeEv = types.value[index].copy(),
                            OnClick = {viewModelMainScreen.AddType(types.value[index].id); viewModelMainScreen.filtevent(textSearch.value) },
                            OnUnclick = {viewModelMainScreen.RemoveType(types.value[index].id);viewModelMainScreen.filtevent(textSearch.value)}
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            when(actualState) {
                is ActualState.Initialized, is ActualState.Success -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(events.value.size) { index ->
                            EventCard(event = events.value[index].copy())
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
                is ActualState.Error -> {
                    Text(
                        text = (actualState as ActualState.Error).message,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                is ActualState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}