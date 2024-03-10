package com.github.fscoward.txtman.cli.model

import kotlinx.datetime.LocalDateTime
import kotlinx.io.Buffer
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.writeString

/**
 * actionType: create, dateTime: 2024-03-10 20:00, <task_json>
 * actionType: update, dateTime: 2024-03-10 20:02, <task_json>
 * */
data class ActivityLog(val actionType: String, val dateTime: LocalDateTime, val log: String)

fun ActivityLog.write() {
    // https://fzhinkin.github.io/kotlinx-io-dokka-docs-preview/kotlinx-io-core/index.html
    val sink = SystemFileSystem.sink(path = Path("./activity.log"), append = true)
    val buffer = Buffer()
    buffer.writeString(this.toCSV() + "\r\n")
    buffer.transferTo(sink)
    sink.flush()
}

fun ActivityLog.toCSV(): String {
    return "${this.actionType}, ${this.dateTime}, ${this.log}"
}