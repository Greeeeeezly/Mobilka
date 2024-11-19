package com.example.lab3

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SeekBar
import android.widget.Toast
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.fragment.app.Fragment
import com.example.lab3.databinding.FragmentSettingsBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import java.io.File
import android.os.Environment
import android.util.Log
import com.example.lab3.FileUtils.isFileExists

val Context.dataStore: androidx.datastore.core.DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding

    // DataStore keys
    private val NOTIFICATIONS_KEY = booleanPreferencesKey("notifications")

    // Путь к директории
    private val directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)

        val sharedPreferences = requireContext().getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)

        // Темная тема
        binding.darkModeSwitch.isChecked = sharedPreferences.getBoolean("dark_mode", false)
        binding.darkModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean("dark_mode", isChecked).apply()
        }

        // Язык
        val languages = resources.getStringArray(R.array.language_options)
        val selectedLanguage = sharedPreferences.getString("language", languages[0])
        val languageAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, languages)
        binding.languageSpinner.adapter = languageAdapter
        binding.languageSpinner.setSelection(languages.indexOf(selectedLanguage))
        binding.languageSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                sharedPreferences.edit().putString("language", languages[position]).apply()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Уведомления (с использованием DataStore)
        lifecycleScope.launch {
            val notificationsState = getNotificationsState()
            binding.notificationsSwitch.isChecked = notificationsState
        }

        binding.notificationsSwitch.setOnCheckedChangeListener { _, isChecked ->
            lifecycleScope.launch {
                saveNotificationsState(isChecked)
            }
        }

        // Размер шрифта
        binding.fontSizeSeekBar.progress = sharedPreferences.getInt("font_size", 16)
        binding.fontSizeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                sharedPreferences.edit().putInt("font_size", progress).apply()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Электронная почта
        binding.emailInput.setText(sharedPreferences.getString("email", ""))
        binding.saveButton.setOnClickListener {
            val email = binding.emailInput.text.toString()
            sharedPreferences.edit().putString("email", email).apply()
            Toast.makeText(requireContext(), "Настройки сохранены", Toast.LENGTH_SHORT).show()
        }

        // Проверка существования файла с героями
        // Проверка существования файла с героями
        val heroesFileName = "variant_8.txt" // Например, номер по списку - 12345
        val fileExists = isFileExists(requireContext(), heroesFileName)

        val sharedPreferencesDir = File(requireContext().applicationInfo.dataDir, "shared_prefs")

        Log.d("SharedPreferences", "SharedPreferences directory: ${sharedPreferencesDir.absolutePath}")
        // Отображаем статус файла в UI
        lifecycleScope.launch(Dispatchers.Main) {
            if (fileExists) {
                Log.d("SettingsFragment", "Файл существует")
                binding.fileStatusTextView.text = "Файл с героями существует"
            } else {
                Log.d("SettingsFragment", "Файл не найден")
                binding.fileStatusTextView.text = "Файл с героями не найден"
            }
        }




        return binding.root
    }

    // Получить состояние уведомлений из DataStore
    private suspend fun getNotificationsState(): Boolean {
        val preferences = requireContext().dataStore.data.first()
        return preferences[NOTIFICATIONS_KEY] ?: true // По умолчанию включено
    }

    // Сохранить состояние уведомлений в DataStore
    private suspend fun saveNotificationsState(isChecked: Boolean) {
        requireContext().dataStore.edit { preferences ->
            preferences[NOTIFICATIONS_KEY] = isChecked
        }
    }
}
