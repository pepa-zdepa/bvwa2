package cz.upce.database

import java.io.FileInputStream
import java.security.KeyStore
import java.util.*
import javax.crypto.Cipher
import javax.crypto.SecretKey

class Encryption {
    fun encrypt(data: String, secretKey: SecretKey): String {
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        val encrypted = cipher.doFinal(data.toByteArray())
        return Base64.getEncoder().encodeToString(encrypted)
    }

    fun decrypt(data: String, secretKey: SecretKey): String {
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.DECRYPT_MODE, secretKey)
        val decodedData = Base64.getDecoder().decode(data)
        val decryptedByteValue = cipher.doFinal(decodedData)
        return String(decryptedByteValue)
    }

    //alias - my-key-alias
    fun getSecretKey(keystorePath: String, keystorePassword: String, alias: String, keyPassword: String): SecretKey {
        val keyStore = KeyStore.getInstance("JKS")
        val keystoreFile = FileInputStream(keystorePath)

        try {
            keyStore.load(keystoreFile, keystorePassword.toCharArray())

            val key = keyStore.getKey(alias, keyPassword.toCharArray()) as SecretKey

            return key
        } finally {
            keystoreFile.close()
        }
    }
}