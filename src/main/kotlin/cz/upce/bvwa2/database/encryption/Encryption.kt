package cz.upce.bvwa2.database.encryption

import cz.upce.bvwa2.Config
import org.mindrot.jbcrypt.BCrypt
import java.util.*
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

// Třída Encryption poskytuje nástroje pro šifrování, dešifrování a hashování hesel.
class Encryption(config: Config) {

    // Tajný klíč používaný pro šifrování a dešifrování.
    private val secretKey: SecretKey = getSecretKeyFromEncodedString(config.security.databaseEncryptKey)

    // Generování tajného klíče pro šifrování.
    fun generateSecretKey(): SecretKey {
        val keyGenerator = KeyGenerator.getInstance("AES")
        keyGenerator.init(256)
        return keyGenerator.generateKey()
    }

    // Získání tajného klíče ze zakódovaného řetězce.
    fun getSecretKeyFromEncodedString(encodedKey: String): SecretKey {
        val decodedKey = Base64.getDecoder().decode(encodedKey)
        return SecretKeySpec(decodedKey, "AES")
    }

    // Šifrování dat pomocí AES šifrování.
    fun encrypt(data: String): String {
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        val encrypted = cipher.doFinal(data.toByteArray())
        return Base64.getEncoder().encodeToString(encrypted)
    }

    // Dešifrování dat pomocí AES šifrování.
    fun decrypt(data: String): String {
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.DECRYPT_MODE, secretKey)
        val decodedData = Base64.getDecoder().decode(data)
        val decryptedByteValue = cipher.doFinal(decodedData)
        return String(decryptedByteValue)
    }

    // Hashování hesla pomocí algoritmu BCrypt.
    fun hashPassword(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }

    // Ověření hesla proti jeho hash hodnotě.
    fun checkPassword(candidate: String, hashed: String): Boolean {
        return BCrypt.checkpw(candidate, hashed)
    }
}
