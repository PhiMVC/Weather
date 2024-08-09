package com.mvc.realtimeweather

import android.graphics.Paint.Align
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.mvc.realtimeweather.ui.theme.api.NetworkResponse
import com.mvc.realtimeweather.ui.theme.api.WeatherModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherPage(viewModel :WeatherViewModel) {
var city by remember {
    mutableStateOf("")
}
    val weatherResult = viewModel.weatherResult.observeAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            OutlinedTextField(
                value = city,
                onValueChange ={
                city = it;
            },
                label = {
                    Text(text = "Search for any location")
                }

                )
            IconButton(onClick = {
                viewModel.getData(city)
            }) {
                Icon(imageVector = Icons.Default.Search, contentDescription ="Search for any location")
            }
        }

        when(val result = weatherResult.value){
            is NetworkResponse.Error -> {

              Text(text = result.message)
            }
            is NetworkResponse.Loading ->{

              CircularProgressIndicator()
            }
            is NetworkResponse.Success -> {

              WeatherDetails(result.data)
            }
            null -> {
            }
        }
    }
}

@Composable
fun WeatherDetails(data:WeatherModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Bottom
    ) {
        Row() {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription ="Location icon",
                modifier = Modifier.size(40.dp)
            )
            Column() {
                Text(text = data.location.name, fontSize = 30.sp)

                Text(text = data.location.country, fontSize = 18.sp, color = Color.Gray)
            }

        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = "${data.current.temp_c} * C",
            fontSize =  56.sp,
            fontWeight = FontWeight.Black,
            textAlign = TextAlign.Center)

        AsyncImage(
            modifier = Modifier
                .size(160.dp)
                .align(Alignment.CenterHorizontally),
            model = "https:${data.current.condition.icon}".replace(oldValue = "64x64", newValue ="128x128"),
            contentDescription = "Condition icon"

        )
        Text(modifier = Modifier

            .align(Alignment.CenterHorizontally),text = data.current.condition.text, fontSize = 18.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(8.dp))

        Card(modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.CenterHorizontally)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement=Arrangement.SpaceBetween) {
               WeatherKeyVal("Humidity",data.current.humidity)
               WeatherKeyVal("Wind Speed",data.current.wind_kph)
          }
         Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement=Arrangement.SpaceBetween) {
                WeatherKeyVal("UV",data.current.uv)
                WeatherKeyVal("Participation",data.current.precip_mm)
         }
        }

    }


}

@Composable
fun WeatherKeyVal(key:String, value:String) {

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = key,
            textAlign = TextAlign.Center,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
        )
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = value,
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Gray
        )

    }

}
