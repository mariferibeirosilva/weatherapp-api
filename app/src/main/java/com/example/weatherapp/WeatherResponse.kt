package com.example.weatherapp

data class WeatherResponse(
    val name: String, // Nome da cidade retornada
    val main: Main,
    val weather: List<Weather>
)

data class Main(
    val temp: Float
)

data class Weather(
    val description: String
)
