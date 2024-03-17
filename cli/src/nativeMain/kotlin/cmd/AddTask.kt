package com.github.fscoward.txtman.cli.cmd

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.option
import com.github.fscoward.txtman.cli.model.*

class AddTask : CliktCommand(name = "add", help = "Add a new task") {
    val name by argument()
    val parentTaskId by option("-p", "--parent", help = "ID of the parent task under which the new task will be added")

    override fun run() {
        val taskList = TaskList.load()

        val newTask = Task(name)
        // 親タスクIDが指定されている場合、そのタスクを探して子タスクリストに追加
        if (parentTaskId != null) {
            val updatedTasks = taskList.tasks.addSubTask(TaskID(parentTaskId!!), newTask)
            TaskList(updatedTasks).save()
            echo("Subtask added successfully under parent task ID: $parentTaskId")
        } else {
            // 親タスクが指定されていない場合、トップレベルのタスクとして追加
            val updatedTasks = taskList.tasks + newTask
            TaskList(updatedTasks).save()
            echo("Task added successfully: ${newTask.id}")
        }

        ActivityLog(actionType = ActionType.ADD, log = newTask.toJson()).write()

        echo("add task ${newTask.toJson()}")
    }
    private fun List<Task>.addSubTask(parentId: TaskID, newTask: Task): List<Task> {
        return map { task ->
            if (task.id == parentId) {
                task.copy(children = task.children + newTask)
            } else {
                task.copy(children = task.children.addSubTask(parentId, newTask))
            }
        }
    }
}