package com.github.fscoward.txtman.cli.model

import kotlinx.io.Buffer
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.readString
import kotlinx.io.writeString
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
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
        // 再帰的に特定のIDを持つタスクを探すヘルパーメソッド
        fun findTaskById(tasks: List<Task>, id: TaskID): Task? {
            tasks.forEach { task ->
                if (task.id == id) return task
                findTaskById(task.children, id)?.let { return it }
            }
            return null
        }
    }
    fun convertTasksToBulletList(tasks: List<Task>, indentLevel: Int = 0): String {
        val indent = "  ".repeat(indentLevel)
        return tasks.joinToString(separator = "\n") { task ->
            "$indent- ${task.id.value}, name: ${task.name}, priority: ${task.priority}, status: ${task.status}, createdDateTime: ${task.createdDateTime}" +
                    if (task.children.isNotEmpty()) "\n${convertTasksToBulletList(task.children, indentLevel + 1)}" else ""
        }
    }
    override fun toString(): String {
        return convertTasksToBulletList(tasks)
    }
}
fun TaskList.save() {
    // https://fzhinkin.github.io/kotlinx-io-dokka-docs-preview/kotlinx-io-core/index.html
    val sink = SystemFileSystem.sink(path = Path("./sample.json"), append = false)
    val buffer = Buffer()
    val json = Json.encodeToString(this)
    buffer.writeString(json)
    buffer.transferTo(sink)
    sink.flush()
}

