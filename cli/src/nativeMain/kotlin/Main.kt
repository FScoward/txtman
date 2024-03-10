package com.github.fscoward.txtman.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.option
import kotlinx.serialization.json.Json
import com.github.fscoward.txtman.cli.model.*
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.readString


class AddTask: CliktCommand(name = "add", help = "Add a new task") {
    val name by argument()
    override fun run() {
        val newTask = Task(name)
        echo("add task ${newTask.id}, $newTask")
        echo("add task ${newTask.toJson()}")
    }
}

class UpdateTask: CliktCommand(name = "update") {
    val id by argument()
    val status by option()

    override fun run() {
        val json = load("./sample.json")
        val taskList = Json.decodeFromString<TaskList>(json)
        val updated = taskList.tasks.find { t -> t.id == id }?.let { t -> t.copy(status = TaskStatus.IN_PROGRESS) }

        echo("updated: $updated")
    }
}
class TaskCommand: CliktCommand() {
    override fun run() {
//        echo("add task")
    }
}

fun main(args: Array<String>) = TaskCommand().subcommands(AddTask(), UpdateTask()).main(args)

fun load(path: String): String {
    return SystemFileSystem.source(Path(path)).buffered().readString()
}