package com.github.fscoward.txtman.cli.cmd

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.fscoward.txtman.cli.model.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class AddTask : CliktCommand(name = "add", help = "Add a new task") {
    val name by argument()
    override fun run() {
        val taskList = TaskList.load()

        val newTask = Task(name)
        val updatedTasks = taskList.tasks + newTask
        val updatedJson = Json.encodeToString(TaskList(updatedTasks))

        ActivityLog(actionType = ActionType.ADD, log = newTask.toJson()).write()
        TaskList(updatedTasks).save()

        echo("add task ${newTask.id}, $newTask")
        echo("add task ${newTask.toJson()}")
        echo("updatedJson $updatedJson")
    }
}