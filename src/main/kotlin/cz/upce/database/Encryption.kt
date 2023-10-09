package cz.upce.database

import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.security.KeyStore
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.security.UnrecoverableEntryException
import java.security.cert.CertificateException
import java.util.*
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

class Encryption {

    fun generateSecretKey(): SecretKey {
        val keyGenerator = KeyGenerator.getInstance("AES")
        keyGenerator.init(256)

        return keyGenerator.generateKey()
    }

    fun storeSecretKey(alias: String, secretKey: SecretKey, keystorePassword: CharArray, keyPassword: CharArray, keystorePath: String) {
        try {
            val keyStore = KeyStore.getInstance("JCEKS")
            val keyStoreFile = java.io.File(keystorePath)
            if (keyStoreFile.exists()) {
                keyStore.load(FileInputStream(keyStoreFile), keystorePassword)
            } else {
                keyStore.load(null, null)
            }

            val secretKeyEntry = KeyStore.SecretKeyEntry(secretKey)
            keyStore.setEntry(alias, secretKeyEntry, KeyStore.PasswordProtection(keyPassword))

            // Save the keystore to a file
            keyStore.store(FileOutputStream(keyStoreFile), keystorePassword)
        } catch (e: KeyStoreException) {
            e.printStackTrace()
        } catch (e: CertificateException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun loadSecretKey(alias: String, keystorePassword: CharArray, keyPassword: CharArray, keystorePath: String): SecretKey? {
        try {
            val keyStore = KeyStore.getInstance("JCEKS")
            keyStore.load(FileInputStream(keystorePath), keystorePassword)

            val secretKeyEntry = keyStore.getEntry(alias, KeyStore.PasswordProtection(keyPassword)) as KeyStore.SecretKeyEntry?
            return secretKeyEntry?.secretKey
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: CertificateException) {
            e.printStackTrace()
        } catch (e: UnrecoverableEntryException) {
            e.printStackTrace()
        } catch (e: KeyStoreException) {
            e.printStackTrace()
        }
        return null
    }


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