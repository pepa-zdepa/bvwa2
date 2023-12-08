package cz.upce.bvwa2.utils

import java.io.ByteArrayOutputStream
import java.io.InputStream
import javax.imageio.IIOImage
import javax.imageio.ImageIO
import javax.imageio.ImageWriteParam


object ImageConverter {
    fun convertImage(imageStream: InputStream, quality: Float = 0.9f): ByteArray {
        val image = ImageIO.read(imageStream)
        imageStream.close()

        val jpgWriter = ImageIO.getImageWritersByFormatName("jpg").next()
        val jpgWriteParam = jpgWriter.defaultWriteParam
        jpgWriteParam.compressionMode = ImageWriteParam.MODE_EXPLICIT
        jpgWriteParam.compressionQuality = quality

        ByteArrayOutputStream().use {
            jpgWriter.output = ImageIO.createImageOutputStream(it)
            val outputImage = IIOImage(image, null, null)
            jpgWriter.write(null, outputImage, jpgWriteParam)
            jpgWriter.dispose()

            return it.toByteArray()
        }
    }

    fun convertImage(imageByteArray: ByteArray, quality: Float = 0.9f) = convertImage(imageByteArray.inputStream(), quality)
}