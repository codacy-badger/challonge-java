package at.stefangeyer.challonge.serialization

import java.lang.reflect.Type

interface Serializer {

    fun serialize(any: Any): String

    fun <T> deserialize(string: String, type: Type): T
}