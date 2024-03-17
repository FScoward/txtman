package com.github.fscoward.txtman.cli.model

import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.datetime.LocalDate
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.*


@Serializable
data class DailyJournalsMap(
    val dailyJournals: Map<LocalDate, ImmutableSet<TaskID>>
)

object DailyJournalsMapSerializer : KSerializer<DailyJournalsMap> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("DailyJournalsMap")

    override fun serialize(encoder: Encoder, value: DailyJournalsMap) {
        val jsonEncoder = encoder as? JsonEncoder ?: throw SerializationException("This serializer can be used with JSON format only.")

        val jsonMap = value.dailyJournals.entries.associate { (date, taskIDs) ->
            date.toString() to JsonArray(taskIDs.map { taskId ->
                JsonObject(mapOf("value" to JsonPrimitive(taskId.value)))
            })
        }

        val jsonObject = JsonObject(jsonMap)
        jsonEncoder.encodeJsonElement(jsonObject)
    }


    override fun deserialize(decoder: Decoder): DailyJournalsMap {
        val jsonDecoder = decoder as JsonDecoder
        val jsonObject = jsonDecoder.decodeJsonElement().jsonObject
        val map = jsonObject.map { (date, tasksJsonElement) ->
            val parsedDate = LocalDate.parse(date)
            // tasksJsonElementから直接List<TaskID>へデシリアライズします。
            val tasksList = when (tasksJsonElement) {
                is JsonArray -> tasksJsonElement.map { taskElement ->
                    Json.decodeFromJsonElement<TaskID>(taskElement)
                }

                else -> listOf()
            }
            val tasksSet = persistentSetOf<TaskID>().addAll(tasksList)
            parsedDate to tasksSet
        }.toMap()
        return DailyJournalsMap(map)
    }
}
