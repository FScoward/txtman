package com.github.fscoward.txtman.cli.cmd

import com.github.ajalt.clikt.core.CliktCommand
import com.github.fscoward.txtman.cli.model.TaskList
import kotlinx.serialization.json.Json

class ShowTaskList : CliktCommand(name = "list") {
    override fun run() {
        val json = TaskList.read("./sample.json")
        val taskList = Json.decodeFromString<TaskList>(json)
        echo(taskList)
    }
}