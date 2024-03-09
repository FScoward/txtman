package com.github.fscoward.txtman.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.options.*
import com.github.ajalt.clikt.parameters.types.int
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.fscoward.txtman.cli.model.*

class Hello : CliktCommand() {
    val count: Int by option().int().default(1).help("Number of greetings")
    val name: String by option().prompt("Your name").help("The person to greet")

    override fun run() {
        repeat(count) {
            echo("Hello $name!")
        }
    }
}

class AddTask: CliktCommand(name = "add", help = "Add a new task") {
    val name by argument()
    override fun run() {
        val newTask = Task(name)
        echo("add task ${newTask.id}, ${newTask.name}, ${newTask.createdDateTime}")
    }
}

class Task: CliktCommand() {
    override fun run() {
//        echo("add task")
    }
}

fun main(args: Array<String>) = Task().subcommands(AddTask()).main(args)