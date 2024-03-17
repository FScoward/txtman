package com.github.fscoward.txtman.cli.model

import kotlinx.collections.immutable.ImmutableSet
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class DailyJournal(val date: LocalDate, val tasks: ImmutableSet<TaskID>)
