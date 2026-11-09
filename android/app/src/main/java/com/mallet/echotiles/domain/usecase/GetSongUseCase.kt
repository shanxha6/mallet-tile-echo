package com.mallet.echotiles.domain.usecase

import com.mallet.echotiles.domain.model.Song

class GetSongUseCase(private val songs: List<Song>) {

    operator fun invoke(index: Int): Song {
        if (songs.isEmpty()) return Song(id = 0, notes = listOf(0, 1))
        val safe = ((index % songs.size) + songs.size) % songs.size
        return songs[safe]
    }
}
