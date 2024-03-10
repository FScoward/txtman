package com.github.fscoward.txtman.cli.model

import kotlinx.serialization.Serializable

@Serializable
data class TaskList(val tasks: List<Task>) {
    override fun toString(): String {
        return tasks.map { t -> "${t.id}, ${t.name}, ${t.priority}, ${t.status}, ${t.createdDateTime}" }
            .joinToString("\r\n")
    }
}
