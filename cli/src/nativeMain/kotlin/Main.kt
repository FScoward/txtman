package com.github.fscoward.txtman.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.github.fscoward.txtman.cli.cmd.*

class TaskCommand : CliktCommand() {
    override fun run() {
//        echo("add task")
    }
}

fun main(args: Array<String>) =
    TaskCommand().subcommands(AddTask(), UpdateTask(), ShowTaskList(), AddJournalCommand(), ShowJournal()).main(args)

