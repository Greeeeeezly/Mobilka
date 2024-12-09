package com.example.lab3

import com.example.lab3.network.retrofit.RetrofitNetworkApi
import kotlinx.coroutines.flow.Flow

class CharacterRepository(
    private val dao: CharacterDao,
    private val retrofitApi: RetrofitNetworkApi
) {
    fun getCharactersFromDatabase(): Flow<List<CharacterEntity>> = dao.getAllCharacters()

    suspend fun refreshCharacters(page: Int, pageSize: Int) {
        val apiCharacters = retrofitApi.getCharacters(page, pageSize)
        val entities = apiCharacters.map {
            CharacterEntity(
                name = it.name,
                culture = it.culture,
                born = it.born,
                titles = it.titles,
                aliases = it.aliases,
                playedBy = it.playedBy
            )
        }
        dao.insertAll(entities)
    }

    suspend fun getCharacterById(id: Int): CharacterEntity? {
        return dao.getCharacterById(id)
    }

    suspend fun insertCharacter(character: CharacterEntity) {
        dao.insertCharacter(character)
    }

    suspend fun updateCharacter(character: CharacterEntity) {
        dao.updateCharacter(character)
    }

    suspend fun deleteCharacterById(id: Int): Int {
        return dao.deleteCharacterById(id)
    }
    suspend fun getCharacters(page: Int, pageSize: Int): List<CharacterEntity> {
        // Сначала пробуем получить данные из базы
        val charactersFromDb = dao.getCharactersByPage(page, pageSize)
        if (charactersFromDb.isEmpty()) {
            // Если нет в базе, получаем данные из API
            val characters = retrofitApi.getCharacters(page, pageSize)
            // Сохраняем данные в базу
            dao.insertAll(characters.map { it.toEntity() })
            return characters.map { it.toEntity() }
        }
        return charactersFromDb
    }
}
