package com.mallet.echotiles.domain.usecase

class CheckEchoTapUseCase {

    operator fun invoke(phrase: List<Int>, filled: Int, tappedBar: Int): Boolean {
        if (filled !in phrase.indices) return false
        return phrase[filled] == tappedBar
    }
}
