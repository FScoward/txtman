package com.github.fscoward.txtman.cli.model

import kotlinx.collections.immutable.toImmutableMap
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.datetime.LocalDate
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

    /**
     * 指定された日付のJournalを返す
     * */
    fun filterBy(date: LocalDate): Journal {
        return Journal(DailyJournalsMap(dailyJournalsMap.dailyJournals.filterKeys { it == date }))
    }

    /**
     * 指定された日のタスクをJournalから削除する
     * @param date 削除対象のタスク日付
     * @param taskID 削除対象のタスクID
     * */
    fun deleteTask(date: LocalDate, taskID: TaskID): Journal {
        // 指定された日付でのタスクセットから、指定されたタスクIDを削除
        val updatedDailyJournals = dailyJournalsMap.dailyJournals
            .mapValues { (dateKey, taskSet) ->
                if (dateKey == date) (taskSet - taskID).toImmutableSet() else taskSet
            }
//            .filterValues { it.isNotEmpty() }  // 空のタスクセットを持つ日付を削除

        // 更新されたマップで新しいJournalオブジェクトを作成
        return Journal(DailyJournalsMap(updatedDailyJournals))
    }
}
