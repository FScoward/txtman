package  com.github.fscoward.txtman.cli.model
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.datetime.LocalDate
import kotlin.test.Test
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
}
