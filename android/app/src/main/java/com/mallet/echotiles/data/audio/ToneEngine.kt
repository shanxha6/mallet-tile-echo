package com.mallet.echotiles.data.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import com.mallet.echotiles.data.sample.SampleData
import com.mallet.echotiles.domain.model.Instrument
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.sin

class ToneEngine {

    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private val active = mutableListOf<AudioTrack>()

    fun playBar(barIndex: Int, instrument: Instrument, volumePercent: Int) {
        val bar = SampleData.BARS.getOrNull(barIndex) ?: return
        playFrequency(bar.frequency, instrument, volumePercent)
    }

    fun playFrequency(frequency: Double, instrument: Instrument, volumePercent: Int) {
        scope.launch {
            val durationMs = instrument.decayMs + TAIL_MS
            val samples = synthesise(frequency, instrument, volumePercent, durationMs)
            val track = buildTrack(samples) ?: return@launch
            synchronized(active) { active.add(track) }
            runCatching { track.play() }
            delay(durationMs + RELEASE_PADDING_MS)
            val stillActive = synchronized(active) { active.remove(track) }
            if (stillActive) {
                runCatching {
                    track.stop()
                    track.release()
                }
            }
        }
    }

    fun stopAll() {
        val tracks = synchronized(active) {
            val copy = active.toList()
            active.clear()
            copy
        }
        tracks.forEach { track ->
            runCatching {
                track.pause()
                track.flush()
                track.release()
            }
        }
    }

    private fun synthesise(
        frequency: Double,
        instrument: Instrument,
        volumePercent: Int,
        durationMs: Long
    ): ShortArray {
        val count = (SAMPLE_RATE * durationMs / 1000L).toInt().coerceAtLeast(MIN_SAMPLES)
        val gain = volumePercent.coerceIn(0, 100) / 100.0 * HEADROOM
        val decay = instrument.decayMs.coerceAtLeast(1L) / 1000.0
        val mix = instrument.harmonicMix.toDouble()
        val normaliser = 1.0 + mix + mix * 0.4
        val out = ShortArray(count)
        for (i in 0 until count) {
            val t = i.toDouble() / SAMPLE_RATE
            val envelope = exp(-t / decay) * attack(t)
            val base = sin(2.0 * PI * frequency * t)
            val second = sin(4.0 * PI * frequency * t) * mix
            val third = sin(6.0 * PI * frequency * t) * mix * 0.4
            val value = (base + second + third) / normaliser
            out[i] = (value * envelope * gain * Short.MAX_VALUE).toInt()
                .coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt())
                .toShort()
        }
        return out
    }

    private fun attack(t: Double): Double =
        if (t >= ATTACK_SECONDS) 1.0 else t / ATTACK_SECONDS

    private fun buildTrack(samples: ShortArray): AudioTrack? = runCatching {
        val track = AudioTrack.Builder()
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()
            )
            .setAudioFormat(
                AudioFormat.Builder()
                    .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                    .setSampleRate(SAMPLE_RATE)
                    .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                    .build()
            )
            .setBufferSizeInBytes(samples.size * 2)
            .setTransferMode(AudioTrack.MODE_STATIC)
            .build()
        track.write(samples, 0, samples.size)
        track
    }.getOrNull()

    private companion object {
        const val SAMPLE_RATE = 44100
        const val TAIL_MS = 180L
        const val RELEASE_PADDING_MS = 120L
        const val MIN_SAMPLES = 1024
        const val HEADROOM = 0.55
        const val ATTACK_SECONDS = 0.006
    }
}
