package com.sw926.toolkit.date

import com.google.gson.*
import com.sw926.toolkit.safeGetValue
import org.threeten.bp.*
import java.lang.reflect.Type

@Suppress("unused")
class DateTimeTypeAdapter : JsonDeserializer<LocalDateTime>, JsonSerializer<LocalDateTime> {

    override fun serialize(src: LocalDateTime?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        val dateTime = src ?: LocalDateTime.from(Instant.ofEpochMilli(0))
        return JsonPrimitive(dateTime.toInstant(ZoneOffset.UTC).toString())
    }

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): LocalDateTime {
        if (json?.isJsonPrimitive == true) {
            val jsonPrimitive = json as JsonPrimitive
            if (jsonPrimitive.isString) {
                val str = jsonPrimitive.asString

                var value = safeGetValue { ZonedDateTime.parse(str).withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime() }
                if (value == null) {
                    value = safeGetValue { LocalDateTime.parse(str) }
                }
                return value ?: LocalDateTime.from(Instant.ofEpochMilli(0))
            }
        }
        return LocalDateTime.from(Instant.ofEpochMilli(0))
    }
}