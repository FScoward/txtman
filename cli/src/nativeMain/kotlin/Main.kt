package com.github.fscoward.txtman.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.fscoward.txtman.cli.model.*

class AddTask: CliktCommand(name = "add", help = "Add a new task") {
    val name by argument()
    override fun run() {
        val newTask = Task(name)
        echo("add task ${newTask.id}, $newTask")
    }
}

class Task: CliktCommand() {
    override fun run() {
//        echo("add task")
    }
}

fun main(args: Array<String>) = Task().subcommands(AddTask()).main(args)