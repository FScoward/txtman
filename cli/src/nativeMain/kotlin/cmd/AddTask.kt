package com.github.fscoward.txtman.cli.cmd

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.fscoward.txtman.cli.model.ActionType
import com.github.fscoward.txtman.cli.model.ActivityLog
import com.github.fscoward.txtman.cli.model.Task
import com.github.fscoward.txtman.cli.model.write

class AddTask : CliktCommand(name = "add", help = "Add a new task") {
    val name by argument()
    override fun run() {
        val newTask = Task(name)
        ActivityLog(actionType = ActionType.ADD, log = newTask.toJson()).write()
        echo("add task ${newTask.id}, $newTask")
        echo("add task ${newTask.toJson()}")
    }
}