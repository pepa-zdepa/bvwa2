package cz.upce.bvwa2.database

import org.mindrot.jbcrypt.BCrypt
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

class Converter {
    fun byteArrayToBufferedImage(byteArray: ByteArray): BufferedImage {
        return ImageIO.read(ByteArrayInputStream(byteArray))
    }

     fun convertBufferedImageToByteArray(img: BufferedImage): ByteArray {
        val byteArrayOutputStream = ByteArrayOutputStream()

        try {
            ImageIO.write(img, "jpg", byteArrayOutputStream)

            return byteArrayOutputStream.toByteArray()
        } finally {
            byteArrayOutputStream.close()
        }
    }

    fun hashPassword(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }

    fun checkPassword(candidate: String, hashed: String): Boolean {
        return BCrypt.checkpw(candidate, hashed)
    }
}
