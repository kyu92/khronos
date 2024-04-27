package com.k92.khronos

import com.k92.khronos.service.GeoService
import com.k92.khronos.task.ClockTask
import com.k92.khronos.util.ImageUtil
import jakarta.annotation.Resource
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.io.InputStreamResource
import java.io.ByteArrayInputStream
import java.io.File

@SpringBootTest
class KhronosApplicationTests {

    @Resource
    lateinit var geoService: GeoService;

    @Resource
    lateinit var clockTask: ClockTask;

    @Test
    fun contextLoads() {
    }

    @Test
    fun testGeoEncode() {
        val location = geoService.getLocation("浙江省杭州市萧山区鸿发路若森杭州绿强食品有限公司内,浩宇建材(杭州)运营中心北91米")
        println(location)
    }

    @Test
    fun testAttendance() {
        clockTask.runAfternoonEndClock()
    }

    @Test
    fun imageCompress() {
        val attachment = File("C:\\Users\\92849\\Desktop\\IMG_20240427_181025.jpg")
        val formatName = attachment.name.substring(attachment.name.lastIndexOf(".") + 1)
        var src = attachment.readBytes()
        println(src.size)
        src = ImageUtil.compressImage(src, 0.8F, formatName)
        src = ImageUtil.resize(src, 1080, 1920, formatName)
        println(src.size)
    }
}
