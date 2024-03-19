package com.github.fscoward.txtman.cli.cmd

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.convert
import com.github.ajalt.clikt.parameters.options.option
import com.github.fscoward.txtman.cli.Journal
import com.github.fscoward.txtman.cli.model.TaskList
import com.github.fscoward.txtman.cli.filterBy
import kotlinx.datetime.LocalDate

class ShowJournal : CliktCommand(name = "journal") {
    val date: LocalDate? by option().convert {
        runCatching {
            LocalDate.parse(it)
        }.fold(
            onSuccess = { it },
            onFailure = { e: Throwable -> fail(e.message ?: "Invalid date format") }
        )
    }

    override fun run() {
        // load journal
        val journal = Journal.load()

        // load task list
        val taskList = TaskList.load()

        // オプションで指定された日付のJournalを取得
        val targetJournal: Journal =
            date?.let { journal.filterBy(it) } ?: journal

        // journalオブジェクトからbullet list形式の文字列を生成
        val bulletListString = toBullet(targetJournal, taskList)

        // 生成した文字列を出力
        echo(bulletListString)
    }

    fun toBullet(journal: Journal, taskList: TaskList): String {
        return journal.dailyJournalsMap.dailyJournals.entries.joinToString(separator = "\n") { entry ->
            val date = entry.key
            val tasks = entry.value
            // 日付を表示
            "${date.toString()}:\n" + tasks.joinToString(separator = "\n") { taskId ->
                // タスクIDをもとにタスクの詳細を取得
                val task = TaskList.findTaskById(taskList.tasks, taskId)
                // タスクの詳細が取得できればその情報を、できなければタスクIDのみを表示
                task?.let {
                    "  - ${it.name}, ${taskId.value}, ${it.status}"
                } ?: "  - ${taskId.value}"
            }
        }
    }
}