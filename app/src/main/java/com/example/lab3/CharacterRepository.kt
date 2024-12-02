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
        //dao.deleteAllCharacters()
        dao.insertAll(entities)
    }

}
