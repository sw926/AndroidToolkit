package com.sw926.toolkit.date

import com.google.gson.*
import com.sw926.toolkit.safeGetValue
import org.threeten.bp.*
import java.lang.reflect.Type


@Suppress("unused")
class LocalDateGsonAdapter : JsonDeserializer<LocalDate>, JsonSerializer<LocalDate> {

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement?, typeOfT: Type, context: JsonDeserializationContext): LocalDate? {
        if (json != null) {
            if (json.isJsonPrimitive) {
                val jsonPrimitive = json.asJsonPrimitive
                if (jsonPrimitive.isNumber) {
                    val value = json.asJsonPrimitive.asLong
                    return LocalDateTime.ofInstant(Instant.ofEpochMilli(value), ZoneId.of("UTC")).toLocalDate()
                } else if (jsonPrimitive.isString) {
                    return parseDateFromServer(jsonPrimitive.asString)
                }
            }
        }
        return null
    }

    override fun serialize(src: LocalDate?, typeOfSrc: Type, context: JsonSerializationContext): JsonElement? {
        return if (src != null) {
            context.serialize(src.toString())
        } else null
    }

    private fun parseDateFromServer(string: String?): LocalDate? {
        if (string == null) {
            return null
        }

        var date = safeGetValue { LocalDate.parse(string) }
        if (date == null) {
            date = safeGetValue { ZonedDateTime.parse(string).toLocalDate() }
        }
        return date
    }
}
