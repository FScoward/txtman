package com.github.fscoward.txtman.cli

import com.github.fscoward.txtman.cli.model.DailyJournalsMap
import com.github.fscoward.txtman.cli.model.DailyJournalsMapSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Journal(
    @Serializable(with = DailyJournalsMapSerializer::class)
    @SerialName("dailyJournals")
    val dailyJournalsMap: DailyJournalsMap
)