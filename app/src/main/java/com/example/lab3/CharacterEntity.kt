package com.example.lab3

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "characters")
data class CharacterEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val culture: String,
    val born: String,
    val titles: List<String>,
    val aliases: List<String>,
    val playedBy: List<String>
) {
    // Конструктор без data ключевого слова, но с необходимыми полями

    // Метод для конвертации в Character
    fun toCharacter(): Character {
        return Character(
            name = this.name,
            culture = this.culture,
            born = this.born,
            titles = this.titles,
            aliases = this.aliases,
            playedBy = this.playedBy
        )
    }

}
