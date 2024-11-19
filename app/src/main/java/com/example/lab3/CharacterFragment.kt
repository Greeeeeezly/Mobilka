package com.example.lab3

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.lab3.databinding.FragmentCharacterBinding
import kotlinx.coroutines.launch
import com.example.lab3.network.ktor.KtorNetwork
import com.example.lab3.network.ktor.KtorNetworkApi
import com.example.lab3.network.retrofit.RetrofitNetwork
import com.example.lab3.network.retrofit.RetrofitNetworkApi
//Надо закрывать ktor и retrofit

class CharacterFragment : Fragment() {
    private lateinit var binding: FragmentCharacterBinding

    private var _retrofitApi: RetrofitNetworkApi? = null
    private val retrofitApi get() = _retrofitApi!!

    private var _ktorApi: KtorNetworkApi? = null
    private val ktorApi get() = _ktorApi!!

    // Добавляем переменные для сетевых клиентов
    private var ktorNetwork: KtorNetwork? = null
    private var retrofitNetwork: RetrofitNetwork? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCharacterBinding.inflate(inflater, container, false)

        // Инициализация Ktor и Retrofit
        ktorNetwork = KtorNetwork()
        retrofitNetwork = RetrofitNetwork()

        _ktorApi = ktorNetwork
        _retrofitApi = retrofitNetwork

        fetchCharacters()  // Инициализируем данные при создании фрагмента

        binding.retrofit.setOnClickListener {
            lifecycleScope.launch {
                try {
                    val characters: List<Character> = retrofitApi.getCharacters(8, 50)  // Используем ваш класс Character
                    Log.d("CharacterFragment", "Characters received: $characters")
                    binding.userList.adapter = ApiResponseAdapter(characters)
                } catch (e: Exception) {
                    Log.e("CharacterFragment", "Error fetching data: ${e.message}")
                }
            }
        }

        binding.ktor.setOnClickListener {
            lifecycleScope.launch {
                try {
                    val characters: List<Character> = ktorApi.getCharacters()  // Используем ваш класс Character
                    Log.d("CharacterFragment", "Characters received: $characters")
                    binding.userList.adapter = ApiResponseAdapter(characters)
                } catch (e: Exception) {
                    Log.e("CharacterFragment", "Error fetching data: ${e.message}")
                }
            }
        }

        binding.archive.setOnClickListener {
            (binding.userList.adapter as ApiResponseAdapter).setData(emptyList())
        }

        return binding.root
    }

    private fun fetchCharacters() {
        lifecycleScope.launch {
            try {
                val characters: List<Character> = retrofitApi.getCharacters(8, 50)  // Используем ваш класс Character
                Log.d("CharacterFragment", "Characters fetched on create: $characters")
                binding.userList.adapter = ApiResponseAdapter(characters)
            } catch (e: Exception) {
                Log.e("CharacterFragment", "Error fetching characters on create: ${e.message}")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        // Закрытие Ktor HttpClient
        ktorNetwork?.closeClient()

        // Закрытие Retrofit OkHttpClient
        retrofitNetwork?.closeClient()
    }
}
