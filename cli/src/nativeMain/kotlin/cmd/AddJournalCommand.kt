package com.github.fscoward.txtman.cli.cmd

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.fscoward.txtman.cli.Journal
import com.github.fscoward.txtman.cli.model.*
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.readString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class AddJournalCommand : CliktCommand(name = "addJournal") {
    val id by argument()
    override fun run() {
        val today = Clock.System.now().toLocalDateTime(TimeZone.UTC)
        val localDate: LocalDate = today.date
        // taskの存在確認
        val targetTaskId = TaskID(id)
        val target: Task? = TaskList.findTaskById(TaskList.load().tasks, targetTaskId)
        if (target == null) {
            throw IllegalArgumentException("target is not exists. task id = $id")
        }

        val path = Path("./journal.json")
        val journalFileExists = SystemFileSystem.exists(path)

        // journalに今日日付のDailyJournalを追加する
        val journal: Journal = if (journalFileExists) {
            val journalJson: String = SystemFileSystem.source(path).buffered().readString()
            Json.decodeFromString<Journal>(journalJson)
        } else {
            Journal(DailyJournalsMap(emptyMap()))
        }

        // タスクを追加する
        val existingTaskID: ImmutableSet<TaskID> = journal.dailyJournalsMap.dailyJournals.get(localDate) ?: run {
            persistentSetOf()
        }
        val x: Map<LocalDate, ImmutableSet<TaskID>> = journal.dailyJournalsMap.dailyJournals.plus((localDate to existingTaskID.plus(targetTaskId).toImmutableSet()))
        Journal(DailyJournalsMap(x))

        // 処理の完了をユーザーに通知します。
        echo("Journal updated for $today with task ID: $targetTaskId")
    }
}