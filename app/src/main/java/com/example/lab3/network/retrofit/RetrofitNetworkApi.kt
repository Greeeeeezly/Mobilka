package com.example.lab3.network.retrofit

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import com.example.lab3.Character

interface RetrofitNetworkApi {
    @GET(value = "/api/characters")
    suspend fun getCharacters(@Query("page") page: Int, @Query("pageSize") pageSize: Int): List<Character>

    @GET(value = "/api/characters/{id}")
    suspend fun getCharacterById(@Path("id") id: Int): Character?
}

private const val NETWORK_BASE_URL = "https://anapioficeandfire.com"

class RetrofitNetwork : RetrofitNetworkApi {
    private val json = Json {
        isLenient = true
        ignoreUnknownKeys = true
    }

    // Сохраняем ссылку на OkHttpClient
    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder().build()
    }

    private val networkApi: RetrofitNetworkApi by lazy {
        Retrofit.Builder()
            .baseUrl(NETWORK_BASE_URL)
            .client(okHttpClient)  // Указываем свой OkHttpClient
            .addConverterFactory(
                json.asConverterFactory("application/json".toMediaType())
            )
            .build()
            .create(RetrofitNetworkApi::class.java)
    }

    override suspend fun getCharacters(page: Int, pageSize: Int): List<Character> = networkApi.getCharacters(page, pageSize)

    override suspend fun getCharacterById(id: Int): Character? = networkApi.getCharacterById(id)

    // Метод для закрытия клиента
    fun closeClient() {
        // Закрываем соединение
        okHttpClient.dispatcher.executorService.shutdown()
    }
}
