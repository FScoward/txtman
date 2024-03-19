package com.github.fscoward.txtman.cli.model

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.*
import ulid.ULID
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive


@Serializable
data class Task(
    val name: String,
    val priority: Int = 0,
    val status: TaskStatus = TaskStatus.TODO,
    val createdDateTime: LocalDateTime = Clock.System.now().toLocalDateTime(
        TimeZone.UTC
    ),
    val id: TaskID = TaskID(ULID.randomULID()),
    val children: List<Task> = listOf()
) {
    companion object {
        private val jsonFormatter = Json { encodeDefaults = true }
    }

    fun toJson(): String {
        return jsonFormatter.encodeToString(this)
    }
}


// TaskIDの定義
@Serializable(with = TaskIDSerializer::class)
value class TaskID(val value: String)


object TaskIDSerializer : KSerializer<TaskID> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("TaskID") {
        element<String>("value")
    }

    override fun serialize(encoder: Encoder, value: TaskID) {
        // JSONオブジェクトとしてTaskIDをシリアライズ
        val jsonObject = JsonObject(mapOf("value" to JsonPrimitive(value.value)))
        encoder.encodeSerializableValue(JsonObject.serializer(), jsonObject)
    }

    override fun deserialize(decoder: Decoder): TaskID {
        require(decoder is JsonDecoder)
        val element = decoder.decodeJsonElement()

        // JsonPrimitive（プリミティブな文字列）としてelementを処理
        return if (element is JsonPrimitive && element.isString) {
            TaskID(element.content)
        } else if (element is JsonObject) {
            // "value" キーから文字列を取得し TaskID を生成
            val value = element["value"]?.jsonPrimitive?.content ?: throw SerializationException("Missing 'value'")
            TaskID(value)
        } else {
            throw SerializationException("Expected JsonObject or JsonPrimitive")
        }
    }
}