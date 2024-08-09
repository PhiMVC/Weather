package com.mvc.realtimeweather

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvc.realtimeweather.ui.theme.api.Constant
import com.mvc.realtimeweather.ui.theme.api.NetworkResponse
import com.mvc.realtimeweather.ui.theme.api.RetrofitInstance
import com.mvc.realtimeweather.ui.theme.api.WeatherModel
import kotlinx.coroutines.launch
import java.lang.Exception

class WeatherViewModel : ViewModel() {
    private  val weatherApi = RetrofitInstance.weatherApi
    private  val _weatherResult = MutableLiveData<NetworkResponse<WeatherModel>>()
    val weatherResult : LiveData<NetworkResponse<WeatherModel>> = _weatherResult

    fun getData(city :String){
        viewModelScope.launch{
            try {
                val response =  weatherApi.getWeather(Constant.apiKey, city)
                if(response.isSuccessful){
                    response.body()?.let {
                        _weatherResult.value = NetworkResponse.Success(it)
                    }
                }else{
                    _weatherResult.value = NetworkResponse.Error("Fail to load data")            }
            }
            catch (e:Exception){
                _weatherResult.value = NetworkResponse.Error("Fail to load data")
            }

        }
    }
}