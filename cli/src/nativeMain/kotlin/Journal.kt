package com.github.fscoward.txtman.cli

import com.github.fscoward.txtman.cli.model.DailyJournalsMap
import com.github.fscoward.txtman.cli.model.DailyJournalsMapSerializer
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.readString
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class Journal(
    @Serializable(with = DailyJournalsMapSerializer::class)
    @SerialName("dailyJournals")
    val dailyJournalsMap: DailyJournalsMap
) {
    companion object {
        fun load(path: Path = Path("./journal.json")): Journal {
            val journalFileExists = SystemFileSystem.exists(path)
            return if (journalFileExists) {
                val journalJson: String = SystemFileSystem.source(path).buffered().readString()
                Json.decodeFromString<Journal>(journalJson)
            } else {
                Journal(DailyJournalsMap(emptyMap()))
            }
        }
    }
}