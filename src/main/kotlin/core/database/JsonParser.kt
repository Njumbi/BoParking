package core.database

import com.google.gson.Gson
import java.lang.reflect.Type


//This is an interface that defines a class that can parse JSON data.
interface JsonParser {
    fun <T> parseToString(data: T): String
    fun <T> parseToObject(string: String, type: Type): T
    fun <T> parseToObject(string: String, classOfT: Class<T>): T
}

class JsonParserImpl : JsonParser {
    // Creating a new instance of the Gson class.
    private val gson = Gson()
    override fun <T> parseToString(data: T): String = gson.toJson(data)

    override fun <T> parseToObject(string: String, type: Type): T {
        return gson.fromJson(string, type)
    }

    override fun <T> parseToObject(string: String, classOfT: Class<T>): T {
        return gson.fromJson(string, classOfT)
    }
}
