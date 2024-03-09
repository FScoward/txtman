package com.github.fscoward.txtman.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.option
import kotlinx.serialization.json.Json
import com.github.fscoward.txtman.cli.model.*

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
        val json = """ {"name":"aaa","priority":0,"status":"TODO","createdDateTime":"2024-03-09T15:57:15.000030","id":"01HRHYXHVRM9C92HY9CA39GWAD"}"""
        val task = Json.decodeFromString<Task>(json)
        val updated = task.copy(status = TaskStatus.IN_PROGRESS)

        echo("updated: $updated")
    }
}

class TaskCommand: CliktCommand() {
    override fun run() {
//        echo("add task")
    }
}

fun main(args: Array<String>) = TaskCommand().subcommands(AddTask(), UpdateTask()).main(args)