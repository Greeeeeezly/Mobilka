package com.example.lab3

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.lab3.databinding.FragmentCharacterBinding
import com.example.lab3.network.ktor.KtorNetwork
import com.example.lab3.network.ktor.KtorNetworkApi
import com.example.lab3.network.retrofit.RetrofitNetwork
import com.example.lab3.network.retrofit.RetrofitNetworkApi
import com.example.lab3.FileUtils
import kotlinx.coroutines.launch

class CharacterFragment : Fragment() {
    private lateinit var binding: FragmentCharacterBinding

    private var _retrofitApi: RetrofitNetworkApi? = null
    private val retrofitApi get() = _retrofitApi!!

    private var _ktorApi: KtorNetworkApi? = null
    private val ktorApi get() = _ktorApi!!

    // Добавляем переменные для сетевых клиентов
    private var ktorNetwork: KtorNetwork? = null
    private var retrofitNetwork: RetrofitNetwork? = null

    // Имя файла для хранения списка персонажей
    private val fileName = "variant_8.txt"

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
            fetchCharactersWithRetrofit()
        }

        binding.ktor.setOnClickListener {
            fetchCharactersWithKtor()
        }

        binding.archive.setOnClickListener {
            (binding.userList.adapter as ApiResponseAdapter).setData(emptyList())
        }

        // Обработка кнопки сохранения файла
        binding.saveFileButton.setOnClickListener {
            saveCharactersToFile()
        }

        // Обработка кнопки удаления файла
        binding.deleteFileButton.setOnClickListener {
            deleteFile()
        }

        // Обработка кнопки резервного копирования файла
        binding.backupFileButton.setOnClickListener {
            backupFile()
        }

        // Обработка кнопки восстановления файла
        binding.restoreFileButton.setOnClickListener {
            restoreFile()
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

    private fun fetchCharactersWithRetrofit() {
        lifecycleScope.launch {
            try {
                val characters: List<Character> = retrofitApi.getCharacters(8, 50)
                Log.d("CharacterFragment", "Characters received: $characters")
                binding.userList.adapter = ApiResponseAdapter(characters)
            } catch (e: Exception) {
                Log.e("CharacterFragment", "Error fetching data: ${e.message}")
            }
        }
    }

    private fun fetchCharactersWithKtor() {
        lifecycleScope.launch {
            try {
                val characters: List<Character> = ktorApi.getCharacters()
                Log.d("CharacterFragment", "Characters received: $characters")
                binding.userList.adapter = ApiResponseAdapter(characters)
            } catch (e: Exception) {
                Log.e("CharacterFragment", "Error fetching data: ${e.message}")
            }
        }
    }

    private fun saveCharactersToFile() {
        val adapter = binding.userList.adapter as? ApiResponseAdapter ?: return
        val characters = adapter.getData()
        val file = FileUtils.saveHeroesToFile(requireContext(), fileName, characters.map { it.toString() })
        if (file != null) {
            Toast.makeText(requireContext(), "Файл сохранён: ${file.path}", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Ошибка сохранения файла", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteFile() {
        if (FileUtils.deleteFile(requireContext(), fileName)) {
            Toast.makeText(requireContext(), "Файл удалён", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Файл не найден", Toast.LENGTH_SHORT).show()
        }
    }

    private fun backupFile() {
        if (FileUtils.backupFileToInternal(requireContext(), fileName)) {
            Toast.makeText(requireContext(), "Резервная копия создана", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Ошибка резервного копирования", Toast.LENGTH_SHORT).show()
        }
    }

    private fun restoreFile() {
        if (FileUtils.restoreFile(requireContext(), fileName)) {
            Toast.makeText(requireContext(), "Файл восстановлен", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Резервная копия не найдена", Toast.LENGTH_SHORT).show()
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
