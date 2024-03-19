package  com.github.fscoward.txtman.cli.model
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class JournalTest {

    @Test
    fun testFilterBy() {
        // モックのJournalオブジェクトを作成
        val mockDailyJournalsMap = mapOf(
            LocalDate.parse("2024-03-17") to persistentSetOf(TaskID("01")),
            LocalDate.parse("2024-03-18") to persistentSetOf(TaskID("02"))
        )
        val mockJournal = Journal(DailyJournalsMap(mockDailyJournalsMap))

        // 特定の日付でフィルタリング
        val filteredJournal = mockJournal.filterBy(LocalDate.parse("2024-03-17"))

        // 結果の検証
        assertTrue {
            filteredJournal.dailyJournalsMap.dailyJournals.size == 1 &&
                    filteredJournal.dailyJournalsMap.dailyJournals.containsKey(LocalDate.parse("2024-03-17"))
        }
    }

    @Test
    fun deleteTask() {
        // 初期のタスクリストを準備
        val initialTasksForDate = persistentSetOf(TaskID("task1"), TaskID("task2"))
        val otherDateTasks = persistentSetOf(TaskID("task3"))
        val initialJournal = Journal(DailyJournalsMap(persistentMapOf(
            LocalDate.parse("2024-01-01") to initialTasksForDate,
            LocalDate.parse("2024-01-02") to otherDateTasks
        )))

        // 特定のタスクを削除
        val updatedJournal = initialJournal.deleteTask(LocalDate.parse("2024-01-01"), TaskID("task1"))

        // 検証: 指定したタスクが削除されていること
        assertFalse { updatedJournal.dailyJournalsMap.dailyJournals[LocalDate.parse("2024-01-01")]!!.contains(TaskID("task1")) }
        // 検証: 他のタスクは影響を受けていないこと
        assertTrue { updatedJournal.dailyJournalsMap.dailyJournals[LocalDate.parse("2024-01-01")]!!.contains(TaskID("task2")) }
        assertTrue { updatedJournal.dailyJournalsMap.dailyJournals[LocalDate.parse("2024-01-02")]!!.contains(TaskID("task3")) }
    }
}
