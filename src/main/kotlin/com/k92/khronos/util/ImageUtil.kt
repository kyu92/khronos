package com.k92.khronos.util

import java.awt.Image
import java.awt.image.BufferedImage
import java.io.*
import javax.imageio.IIOImage
import javax.imageio.ImageIO
import javax.imageio.ImageWriteParam


class ImageUtil {

    companion object {

        @Throws(IOException::class)
        fun compressImage(src: ByteArray, quality: Float, formatName: String): ByteArray {
            val inputStream = ByteArrayInputStream(src);
            val image: BufferedImage = ImageIO.read(inputStream)
            val writer = ImageIO.getImageWritersByFormatName(formatName).next();
            val param = writer.defaultWriteParam
            param.compressionMode = ImageWriteParam.MODE_EXPLICIT
            param.compressionQuality = quality
            val outputStream = ByteArrayOutputStream()
            writer.output = ImageIO.createImageOutputStream(outputStream)
            writer.write(null, IIOImage(image, null, null), param)
            return outputStream.toByteArray()
        }

        @Throws(IOException::class)
        fun resize(src: ByteArray, maxWidth: Int, maxHeight: Int, formatName: String): ByteArray {
            val `is`: InputStream = ByteArrayInputStream(src)
            val srcImage = ImageIO.read(`is`)
            val srcWidth = srcImage.width
            val srcHeight = srcImage.height
            if (srcHeight <= maxHeight && srcWidth <= maxWidth) {
                return src
            }
            val width: Int
            val height: Int
            if (srcWidth > srcHeight) {
                width = maxWidth
                height = (srcHeight.toDouble() / srcWidth * maxHeight).toInt()
            } else {
                height = maxHeight
                width = (srcWidth.toDouble() / srcHeight * maxWidth).toInt()
            }
            val buffImg: BufferedImage
            var type = srcImage.type
            if (type == 0) {
                type = 5
            }
            buffImg = BufferedImage(width, height, type)
            val graphics = buffImg.graphics
            graphics.drawImage(srcImage.getScaledInstance(width, height, Image.SCALE_FAST), 0, 0, null)
            graphics.dispose()
            val os = ByteArrayOutputStream()
            ImageIO.write(buffImg, formatName, os)
            val bytes = os.toByteArray()
            os.close()
            `is`.close()
            return bytes
        }
    }
}