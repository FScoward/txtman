package com.github.fscoward.txtman.cli.model

import kotlinx.serialization.Serializable

@Serializable
data class TaskList(val tasks: List<Task>) {
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
