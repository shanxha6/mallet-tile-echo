package com.mallet.echotiles.domain.usecase

import com.mallet.echotiles.core.config.GameConfig

class UnlockHatUseCase {

    operator fun invoke(bloomedCount: Int): Boolean =
        bloomedCount > 0 && bloomedCount % GameConfig.HAT_EVERY == 0

    fun unlockedHatCount(bloomedCount: Int): Int = bloomedCount / GameConfig.HAT_EVERY
}
