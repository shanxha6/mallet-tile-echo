package com.mallet.echotiles.data.sample

import com.mallet.echotiles.domain.model.Bar
import com.mallet.echotiles.domain.model.Hat
import com.mallet.echotiles.domain.model.Song

object SampleData {

    val BARS: List<Bar> = listOf(
        Bar(0, 261.63),
        Bar(1, 293.66),
        Bar(2, 329.63),
        Bar(3, 349.23),
        Bar(4, 392.00),
        Bar(5, 440.00)
    )

    val SONGS: List<Song> = listOf(
        listOf(0, 1),
        listOf(2, 3),
        listOf(4, 5),
        listOf(0, 4),
        listOf(5, 2),
        listOf(3, 1),
        listOf(0, 2, 4),
        listOf(5, 3, 1),
        listOf(0, 0, 2),
        listOf(4, 2, 0),
        listOf(1, 3, 5),
        listOf(2, 4, 2),
        listOf(0, 1, 2, 3),
        listOf(5, 4, 3, 2),
        listOf(0, 2, 0, 4),
        listOf(3, 3, 1, 1),
        listOf(4, 5, 4, 2),
        listOf(1, 0, 1, 3),
        listOf(0, 1, 2, 3, 4),
        listOf(5, 3, 1, 3, 5),
        listOf(2, 2, 4, 4, 0),
        listOf(0, 4, 2, 5, 1),
        listOf(3, 1, 0, 1, 3),
        listOf(4, 4, 2, 0, 2),
        listOf(0, 1, 2, 3, 4, 5),
        listOf(5, 4, 3, 2, 1, 0),
        listOf(0, 2, 4, 2, 0, 5),
        listOf(1, 1, 3, 3, 5, 5),
        listOf(2, 0, 2, 4, 2, 0),
        listOf(4, 3, 4, 5, 4, 3),
        listOf(0, 1, 2, 3, 4, 5, 4),
        listOf(5, 5, 3, 1, 3, 5, 0),
        listOf(2, 4, 0, 4, 2, 1, 3),
        listOf(1, 2, 1, 0, 2, 3, 2),
        listOf(3, 5, 3, 1, 0, 1, 3),
        listOf(4, 2, 0, 2, 4, 5, 4),
        listOf(0, 1, 2, 3, 4, 5, 3, 1),
        listOf(5, 4, 2, 0, 2, 4, 5, 3),
        listOf(1, 3, 5, 3, 1, 0, 2, 4),
        listOf(2, 0, 4, 2, 5, 3, 1, 0)
    ).mapIndexed { index, notes -> Song(id = index + 1, notes = notes) }

    val HATS: List<Hat> = listOf(
        Hat(id = 0, requiredSongs = 0, argb = 0xFFE8DDC4),
        Hat(id = 1, requiredSongs = 5, argb = 0xFFE2574C),
        Hat(id = 2, requiredSongs = 10, argb = 0xFFFFD98E),
        Hat(id = 3, requiredSongs = 15, argb = 0xFF3DA5D9),
        Hat(id = 4, requiredSongs = 20, argb = 0xFF7FC26B),
        Hat(id = 5, requiredSongs = 25, argb = 0xFF9B6FD4),
        Hat(id = 6, requiredSongs = 30, argb = 0xFFF2843C),
        Hat(id = 7, requiredSongs = 35, argb = 0xFFFFC94A),
        Hat(id = 8, requiredSongs = 40, argb = 0xFF3A2A1E)
    )
}
