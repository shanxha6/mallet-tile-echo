package com.mallet.echotiles.domain.model

enum class Instrument(
    val harmonicMix: Float,
    val decayMs: Long
) {
    Xylophone(0.35f, 140L),
    Bells(0.55f, 520L),
    Marimba(0.22f, 260L)
}
