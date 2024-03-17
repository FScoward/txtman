package com.github.fscoward.txtman.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.github.fscoward.txtman.cli.cmd.AddJournalCommand
import com.github.fscoward.txtman.cli.cmd.AddTask
import com.github.fscoward.txtman.cli.cmd.ShowTaskList
import com.github.fscoward.txtman.cli.cmd.UpdateTask

class TaskCommand : CliktCommand() {
    override fun run() {
//        echo("add task")
    }
}

fun main(args: Array<String>) =
    TaskCommand().subcommands(AddTask(), UpdateTask(), ShowTaskList(), AddJournalCommand()).main(args)

