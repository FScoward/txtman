package com.github.fscoward.txtman.cli.model

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import ulid.ULID

data class Task(
    val name: String,
    val priority: Int = 0,
    val createdDateTime: LocalDateTime = Clock.System.now().toLocalDateTime(
        TimeZone.UTC
    )
) {
    val id: String = ULID.randomULID()
}
