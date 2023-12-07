package cz.upce.bvwa2.database.encryption

import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import javax.crypto.SecretKey
import java.util.Base64

//object EncryptionFactory {
//
//    val encryption = Encryption()
//    lateinit var secretKey: ByteArray
//
//    fun init() {
//        val encryptKeyString = config.encryptKey.key
//
//        secretKey = Base64.getDecoder().decode(encryptKeyString)
//    }
//}