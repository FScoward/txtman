package com.github.fscoward.txtman.cli.cmd

import com.github.fscoward.txtman.cli.Journal
import com.github.fscoward.txtman.cli.model.*
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.datetime.LocalDate
import kotlin.experimental.ExperimentalNativeApi
import kotlin.test.Test
import kotlin.test.assertEquals

class ShowJournalTest {

    @Test
    fun testToBulletWithMockData() {
        // モックデータの設定
        val taskList = listOf(
            Task("Task 1", priority = 1, status = TaskStatus.TODO, id = TaskID("task-001")),
            Task("Task 2", priority = 2, status = TaskStatus.IN_PROGRESS, id = TaskID("task-002"))
        )
        val dailyJournalsMap = mapOf(
            LocalDate.parse("2024-03-17") to persistentSetOf(TaskID("task-001"), TaskID("task-002"))
        )
        val journal = Journal(DailyJournalsMap(dailyJournalsMap))

        // ShowJournalのインスタンス生成とメソッド呼び出し
        val showJournal = ShowJournal()
        val actualBulletList = showJournal.toBullet(journal)

        // 期待される出力
        val expectedBulletList = """
            2024-03-17:
              - task-001
              - task-002
        """.trimIndent()

        // テストの検証
        assertEquals(expectedBulletList, actualBulletList)
    }
}
