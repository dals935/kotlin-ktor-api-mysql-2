import java.math.BigInteger
import java.security.MessageDigest
import java.security.SecureRandom
import java.util.*

fun String.encrypt(): String? {
    try {
        val data = this.toByteArray(charset("UTF-8"))
        return Base64.getEncoder().encodeToString(data)
    } catch (e: Exception) {
        e.message?.let{ println(it) }
    }
    return null
}

// Base64 Encryption/Decryption with no secret key
fun String.decrypt(): String? {
    try {
        val decodeBytes = Base64.getDecoder().decode(this.toString())
        return String(decodeBytes, charset("UTF-8"))

    } catch (e: Exception) {
        e.message?.let { println(it) }
    }
    return null
}

//Hash
var SYMBOLS = "^$*-!@#&,:;_".toCharArray()
var LOWERCASE = "abcdefghijklmnopqrstuvwxyz".toCharArray()
var UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray()
var NUMBERS = "0123456789".toCharArray()
var ALL_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray()
var rand: Random = SecureRandom()

fun String.mnpiHash(): String {
    assert(16 >= 4)
    val password = CharArray(16)

    //get the requirements out of the way
    password[0] = LOWERCASE[rand.nextInt(LOWERCASE.size)]
    password[1] = UPPERCASE[rand.nextInt(UPPERCASE.size)]
    password[2] = NUMBERS[rand.nextInt(NUMBERS.size)]
    password[3] = SYMBOLS[rand.nextInt(SYMBOLS.size)]

    //populate rest of the password with random chars
    for (i in 4 until 16) {
        password[i] = ALL_CHARS[rand.nextInt(ALL_CHARS.size)]
    }

    //shuffle it up
    for (i in password.indices) {
        val randomPosition: Int = rand.nextInt(password.size)
        val temp = password[i]
        password[i] = password[randomPosition]
        password[randomPosition] = temp
    }
    return String(password)
}

fun String.md5(): String {
    val md = MessageDigest.getInstance("MD5")
    return BigInteger(1, md.digest(this.toByteArray())).toString(16).padStart(32, '0')
}
fun String.sha512(): String {
    val md = MessageDigest.getInstance("SHA512")
    return BigInteger(1, md.digest(this.toByteArray())).toString(16).padStart(128, '0')
}