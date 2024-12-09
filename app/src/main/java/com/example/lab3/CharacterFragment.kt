package com.example.lab3

import CharacterViewModel
import CharacterViewModelFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.lab3.databinding.FragmentCharacterBinding
import com.example.lab3.network.ktor.KtorNetwork
import com.example.lab3.network.ktor.KtorNetworkApi
import com.example.lab3.network.retrofit.RetrofitNetwork
import com.example.lab3.network.retrofit.RetrofitNetworkApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CharacterFragment : Fragment() {
    private lateinit var binding: FragmentCharacterBinding

    // Инициализация базы данных
    private val database by lazy { AppDatabase.getDatabase(requireContext()) }
    private val characterDao by lazy { database.characterDao() }

    // Инициализация сетевых клиентов Retrofit и Ktor
    private val retrofitNetwork by lazy { RetrofitNetwork() }
    private val ktorNetwork by lazy { KtorNetwork() }

    private val retrofitApi: RetrofitNetworkApi by lazy { retrofitNetwork }
    private val ktorApi: KtorNetworkApi by lazy { ktorNetwork }

    // Создание репозитория с передачей нужных зависимостей
    private val repository: CharacterRepository by lazy {
        CharacterRepository(characterDao, retrofitApi)
    }

    // Создание ViewModel с передачей репозитория через Factory
    private val viewModel: CharacterViewModel by viewModels {
        CharacterViewModelFactory(repository)
    }

    private val fileName = "variant_8.txt"

    // Состояние текущей страницы
    private var currentPage = 1
    private var pagesCount = 1
    private val pageSize = 50

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        context?.deleteDatabase("character_database")

        // Инициализируем базу данных заново
        val database = Room.databaseBuilder(
            requireContext().applicationContext,
            AppDatabase::class.java,
            "character_database"
        ).build()

        binding = FragmentCharacterBinding.inflate(inflater, container, false)

        fetchCharacters(currentPage)  // Инициализируем данные при создании фрагмента

        // Кнопки для переключения страниц
        binding.nextPageButton.setOnClickListener {
            loadNextPage()
        }

        binding.previousPageButton.setOnClickListener {
            loadPreviousPage()
        }

        binding.retrofit.setOnClickListener {
            fetchCharactersWithRetrofit()
        }

        binding.ktor.setOnClickListener {
            fetchCharactersWithKtor()
        }

        binding.archive.setOnClickListener {
            (binding.userList.adapter as ApiResponseAdapter).setData(emptyList())
        }

        binding.saveFileButton.setOnClickListener {
            saveCharactersToFile()
        }

        binding.deleteFileButton.setOnClickListener {
            deleteFile()
        }

        binding.backupFileButton.setOnClickListener {
            backupFile()
        }

        binding.restoreFileButton.setOnClickListener {
            restoreFile()
        }

        val adapter = ApiResponseAdapter(emptyList())
        binding.userList.adapter = adapter

        lifecycleScope.launch {
            viewModel.characters.collectLatest { entities ->
                val characters = entities.map { it.toCharacter() }
                adapter.setData(characters)
            }
        }

        return binding.root
    }

    private fun fetchCharacters(currentPage: Int) {
        lifecycleScope.launch {
            try {
                val characters = repository.getCharacters(currentPage, pageSize)
                if (characters.isEmpty()) {
                    binding.nextPageButton.isEnabled = false
                } else {
                    binding.userList.adapter = ApiResponseAdapter(characters.map { it.toCharacter() })
                    binding.nextPageButton.isEnabled = true
                }
                binding.previousPageButton.isEnabled = currentPage > 1
            } catch (e: Exception) {
                Log.e("CharacterFragment", "Error fetching characters: ${e.message}")
            }
        }
    }

    private fun loadNextPage() {
        currentPage++
        fetchCharacters(currentPage)
    }

    private fun loadPreviousPage() {
        if (currentPage > 1) {
            currentPage--
            fetchCharacters(currentPage)
        }
        // Кнопка будет включена или отключена в fetchCharacters в зависимости от страницы
    }


    private fun fetchCharactersWithRetrofit() {
        lifecycleScope.launch {
            try {
                val characters: List<Character> = retrofitApi.getCharacters(currentPage, pageSize)
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
        //context?.deleteDatabase("character_database")
        ktorNetwork.closeClient()
        retrofitNetwork.closeClient()
    }
}
