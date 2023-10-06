package cz.upce.database

import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import javax.imageio.ImageIO
class Converter {
    fun byteArrayToBufferedImage(byteArray: ByteArray): BufferedImage {
        return ImageIO.read(ByteArrayInputStream(byteArray))
    }
}

val converter = Converter()