package at.stefangeyer.challonge.serialization.implementation

import at.stefangeyer.challonge.serialization.Serializer
import com.google.gson.Gson
import java.lang.reflect.Type

class GsonSerializer(val gson: Gson) : Serializer {

    override fun serialize(any: Any): String = this.gson.toJson(any)

    override fun <T> deserialize(string: String, type: Type): T = this.gson.fromJson<T>(string, type)
}