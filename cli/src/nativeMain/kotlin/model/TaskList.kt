package com.github.fscoward.txtman.cli.model

import kotlinx.serialization.Serializable

@Serializable
data class TaskList(val tasks: List<Task>)
