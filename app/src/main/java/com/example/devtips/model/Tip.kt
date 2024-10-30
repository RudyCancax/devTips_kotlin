package com.example.devtips.model

import androidx.annotation.StringRes

data class Tip(
    @StringRes val iconId: Int,
    @StringRes val titleId: Int,
    @StringRes val descriptionId: Int
)
