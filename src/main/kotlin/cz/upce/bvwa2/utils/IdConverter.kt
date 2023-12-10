package cz.upce.bvwa2.utils

import org.sqids.Sqids

class IdConverter(alphabet: String, minLength: Int = 10) {
    private val sqids = Sqids.builder().alphabet(alphabet).minLength(minLength).build()

    fun encode(id: Long): String = sqids.encode(listOf(id))

    fun encode(id: Long?): String? = id?.let { encode(it) }

    fun decode(id: String): Long = sqids.decode(id).first()
}