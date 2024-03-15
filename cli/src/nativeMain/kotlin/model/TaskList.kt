package com.github.fscoward.txtman.cli.model

import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.readString
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class TaskList(val tasks: List<Task>) {
    companion object {
        fun read(path: String): String {
            return SystemFileSystem.source(Path(path)).buffered().readString()
        }

        fun load(): TaskList {
            val json = TaskList.read("./sample.json")
            return Json.decodeFromString<TaskList>(json)
        }
    }
    fun convertTasksToBulletList(tasks: List<Task>, indentLevel: Int = 0): String {
        val indent = "  ".repeat(indentLevel)
        return tasks.joinToString(separator = "\n") { task ->
            "$indent- ${task.id}, priority: ${task.priority}, status: ${task.status}, createdDateTime: ${task.createdDateTime}" +
                    if (task.children.isNotEmpty()) "\n${convertTasksToBulletList(task.children, indentLevel + 1)}" else ""
        }
    }
    override fun toString(): String {
        return convertTasksToBulletList(tasks)
    }
}
