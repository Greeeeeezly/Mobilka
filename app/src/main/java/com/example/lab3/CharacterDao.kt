package com.example.lab3

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {
    @Query("SELECT * FROM characters LIMIT :pageSize OFFSET (:page - 1) * :pageSize")
    suspend fun getCharactersByPage(page: Int, pageSize: Int): List<CharacterEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(characters: List<CharacterEntity>)

    @Query("SELECT * FROM characters")
    fun getAllCharacters(): Flow<List<CharacterEntity>>

    @Query("DELETE FROM characters")
    suspend fun deleteAllCharacters(): Int
}
