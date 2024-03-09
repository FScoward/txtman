package com.github.fscoward.txtman.cli.model

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ulid.ULID

@Serializable
data class Task(
    val name: String,
    val priority: Int = 0,
    val status: TaskStatus = TaskStatus.TODO,
    val createdDateTime: LocalDateTime = Clock.System.now().toLocalDateTime(
        TimeZone.UTC
    )
) {
    companion object

    val id: String = ULID.randomULID()
    fun toJson(): String {
        val fmt = Json { encodeDefaults = true }
        return fmt.encodeToString(this)
    }
}