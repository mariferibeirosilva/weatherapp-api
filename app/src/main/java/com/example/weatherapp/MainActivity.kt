package com.example.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private val API_KEY = "0fb1e2afa0cd97bb072f8abb7e5b432f" // Substitua pela sua chave válida

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val editCidade = findViewById<EditText>(R.id.editCidade)
        val editEstado = findViewById<EditText>(R.id.editEstado)
        val btnBuscar = findViewById<Button>(R.id.btnBuscar)
        val textResultado = findViewById<TextView>(R.id.textResultado)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(WeatherApi::class.java)

        btnBuscar.setOnClickListener {
            val cidade = editCidade.text.toString().trim()
            val estado = editEstado.text.toString().trim()

            if (cidade.isEmpty()) {
                textResultado.text = "Por favor, digite a cidade."
                return@setOnClickListener
            }

            // Adiciona o estado e o código do país (BR) na query
            val cidadeCompleta = if (estado.isNotEmpty()) "$cidade,$estado,BR" else "$cidade,BR"

            val call = api.getWeatherByCity(cidadeCompleta, API_KEY)

            call.enqueue(object : Callback<WeatherResponse> {
                override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                    if (response.isSuccessful) {
                        val clima = response.body()
                        val temperatura = clima?.main?.temp
                        val descricao = clima?.weather?.firstOrNull()?.description
                        val cidadeNome = clima?.name

                        textResultado.text = "Cidade: $cidadeNome\nTemperatura: ${temperatura}°C\nDescrição: $descricao"
                    } else {
                        textResultado.text = "Cidade não encontrada."
                    }
                }

                override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                    textResultado.text = "Erro: ${t.message}"
                }
            })
        }

    }
}
