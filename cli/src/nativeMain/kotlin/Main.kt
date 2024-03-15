package com.github.fscoward.txtman.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.option
import com.github.fscoward.txtman.cli.cmd.AddTask
import kotlinx.serialization.json.Json
import com.github.fscoward.txtman.cli.model.*
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.readString
import kotlinx.serialization.encodeToString


class UpdateTask : CliktCommand(name = "update") {
    val id by argument()
    val status by option()

    fun updateTaskStatus(tasks: List<Task>, id: String, newStatus: TaskStatus): List<Task> {
        return tasks.map { task ->
            if (task.id == id) {
                // IDが一致するタスクのステータスを更新
                task.copy(status = newStatus)
            } else if (task.children.isNotEmpty()) {
                // 子タスクが存在する場合、再帰的に処理
                task.copy(children = updateTaskStatus(task.children, id, newStatus))
            } else {
                // 一致しないタスクはそのまま
                task
            }
        }
    }

    override fun run() {
        val json = load("./sample.json")
        val taskList = Json.decodeFromString<TaskList>(json)
        val updatedTasks = updateTaskStatus(taskList.tasks, id, TaskStatus.IN_PROGRESS)

        // 変更のログを記録する
        ActivityLog(
            actionType = ActionType.UPDATE, log = Json.encodeToString(
                TaskList(updatedTasks)
            )
        ).write()

        echo("updated: $updatedTasks")
    }
}

class ShowTaskList : CliktCommand(name = "list") {
    override fun run() {
        val json = load("./sample.json")
        val taskList = Json.decodeFromString<TaskList>(json)
        echo(taskList)
    }
}

class TaskCommand : CliktCommand() {
    override fun run() {
//        echo("add task")
    }
}

fun main(args: Array<String>) = TaskCommand().subcommands(AddTask(), UpdateTask(), ShowTaskList()).main(args)

fun load(path: String): String {
    return SystemFileSystem.source(Path(path)).buffered().readString()
}