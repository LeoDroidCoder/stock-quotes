package com.leodroidcoder.stockqoutes

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun gsonPossibilityTest() {

        val rawJson = "{\n" +
                "        \"firstName\": \"Name\",\n" +
                "        \"lastName\": \"Test\"\n" +
                "      }"

        val user: User = Gson().fromJson<User>(rawJson)

        println(user)
    }
}


//data class User(
//        @SerializedName("firstName") val fname: String,
//        @SerializedName("lastName") val lname: String
//)
class User(
        @SerializedName("firstName") val fname: String,
        @SerializedName("lastName") val lname: String
) {
    override fun toString() = "$fname $lname"
}

