package com.github.fscoward.txtman.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.github.fscoward.txtman.cli.cmd.AddTask
import com.github.fscoward.txtman.cli.cmd.ShowTaskList
import com.github.fscoward.txtman.cli.cmd.UpdateTask
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.readString


class TaskCommand : CliktCommand() {
    override fun run() {
//        echo("add task")
    }
}

fun main(args: Array<String>) = TaskCommand().subcommands(AddTask(), UpdateTask(), ShowTaskList()).main(args)

fun load(path: String): String {
    return SystemFileSystem.source(Path(path)).buffered().readString()
}