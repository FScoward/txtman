package com.github.fscoward.txtman.cli.cmd

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.fscoward.txtman.cli.Journal
import com.github.fscoward.txtman.cli.model.DailyJournalsMap
import com.github.fscoward.txtman.cli.model.TaskID
import com.github.fscoward.txtman.cli.model.TaskList
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.readString
import kotlinx.io.writeString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class AddJournalCommand : CliktCommand(name = "addJournal") {
    val id by argument()
    @OptIn(ExperimentalStdlibApi::class)
    override fun run() {
        val today = Clock.System.now().toLocalDateTime(TimeZone.UTC)
        val localDate: LocalDate = today.date
        // taskの存在確認
        val targetTaskId = TaskID(id)
        TaskList.findTaskById(TaskList.load().tasks, targetTaskId)
            ?: throw IllegalArgumentException("target is not exists. task id = $id")

        // journalに今日日付のDailyJournalを追加する
        val journal: Journal = Journal.load()

        // タスクを追加する
        val existingTaskID: ImmutableSet<TaskID> = journal.dailyJournalsMap.dailyJournals.get(localDate) ?: run {
            persistentSetOf()
        }
        val x: Map<LocalDate, ImmutableSet<TaskID>> = journal.dailyJournalsMap.dailyJournals.plus(
            (localDate to existingTaskID.plus(targetTaskId).toImmutableSet())
        )
        val updatedJournal = Journal(DailyJournalsMap(x))

        // JSONに保存する
        val json = Json.encodeToString(updatedJournal)
        val journalPath = Path("./journal.json")
        SystemFileSystem.sink(journalPath, append = false).buffered().use { sink ->
            sink.writeString(json)
        }

        // 処理の完了をユーザーに通知します。
        echo("Journal updated for $today with task ID: $targetTaskId")
    }
}