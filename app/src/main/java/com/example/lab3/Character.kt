package com.example.lab3

import kotlinx.serialization.Serializable

@Serializable
data class Character(
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
}
