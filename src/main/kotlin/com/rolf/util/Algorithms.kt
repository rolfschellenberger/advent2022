package com.rolf.util

import java.security.MessageDigest
import java.util.*
import javax.xml.bind.DatatypeConverter

private val MD5 = MessageDigest.getInstance("MD5")

fun md5(input: String, uppercase: Boolean = true): String {
    MD5.update(input.toByteArray())
    val digest = MD5.digest()
    val hexBinary = DatatypeConverter.printHexBinary(digest)
    return if (uppercase) {
        hexBinary.uppercase(Locale.getDefault())
    } else {
        hexBinary.lowercase(Locale.getDefault())
    }
}
