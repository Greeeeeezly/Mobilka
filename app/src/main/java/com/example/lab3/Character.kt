package com.example.lab3

import kotlinx.serialization.Serializable

@Serializable
data class Character(
    //val id: Int,
    val name: String,
    val culture: String,
    val born: String,
    val titles: List<String>,
    val aliases: List<String>,
    val playedBy: List<String>


) {
    override fun toString(): String {
        return "Character(name='$name', culture='$culture', born='$born', titles=$titles, aliases=$aliases, playedBy=$playedBy)"
    }

    fun toEntity(): CharacterEntity {
        return CharacterEntity(
            name = this.name,
            culture = this.culture,
            born = this.born,
            titles = this.titles,
            aliases = this.aliases,
            playedBy = this.playedBy
        )
    }
}
