package com.github.fscoward.txtman.cli.cmd

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.switch
import com.github.fscoward.txtman.cli.model.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class UpdateTask : CliktCommand(name = "update") {
    val id by argument()
    val status by option().switch(
        "--todo" to TaskStatus.TODO,
        "--wip" to TaskStatus.IN_PROGRESS,
        "--done" to TaskStatus.DONE
    ).default(TaskStatus.IN_PROGRESS)

    private fun updateTaskStatus(tasks: List<Task>, id: String, newStatus: TaskStatus): List<Task> {
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
        val taskList = TaskList.load()
        val updatedTasks = updateTaskStatus(taskList.tasks, id, status)

        // 変更のログを記録する
        ActivityLog(
            actionType = ActionType.UPDATE, log = Json.encodeToString(
                TaskList(updatedTasks)
            )
        ).write()
        TaskList(updatedTasks).save()
        echo("updated: $updatedTasks")
    }
}