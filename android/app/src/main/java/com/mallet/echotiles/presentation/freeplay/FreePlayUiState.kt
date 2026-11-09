package com.mallet.echotiles.presentation.freeplay

import com.mallet.echotiles.domain.model.Instrument

data class FreePlayUiState(
    val instrument: Instrument = Instrument.Xylophone,
    val litBar: Int? = null,
    val playedNotes: Int = 0
)
