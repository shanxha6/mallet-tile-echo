package com.mallet.echotiles.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.mallet.echotiles.R

@Composable
fun barDescriptions(): List<String> = listOf(
    stringResource(R.string.cd_bar_red),
    stringResource(R.string.cd_bar_orange),
    stringResource(R.string.cd_bar_yellow),
    stringResource(R.string.cd_bar_green),
    stringResource(R.string.cd_bar_blue),
    stringResource(R.string.cd_bar_purple)
)

@Composable
fun noteNames(): List<String> = listOf(
    stringResource(R.string.note_c),
    stringResource(R.string.note_d),
    stringResource(R.string.note_e),
    stringResource(R.string.note_f),
    stringResource(R.string.note_g),
    stringResource(R.string.note_a)
)
