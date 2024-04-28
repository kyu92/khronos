package com.k92.khronos

import com.fasterxml.jackson.databind.ObjectMapper
import com.k92.khronos.config.KhronosConfig
import com.k92.khronos.service.AttendanceService
import com.k92.khronos.service.GeoService
import com.k92.khronos.task.ClockTask
import com.k92.khronos.util.ImageUtil
import jakarta.annotation.Resource
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.io.InputStreamResource
import org.springframework.data.redis.core.RedisTemplate
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileInputStream
import java.util.concurrent.TimeUnit

@SpringBootTest
class KhronosApplicationTests {

    @Resource
    lateinit var geoService: GeoService

    @Resource
    lateinit var clockTask: ClockTask

    @Resource
    lateinit var attendanceService: AttendanceService

    @Resource
    lateinit var khronosConfig: KhronosConfig

    @Resource
    lateinit var redisTemplate: RedisTemplate<String, Any>

    lateinit var token: String

    @BeforeEach
    fun init() {
        val tokenKey = AttendanceService.getTokenKey(khronosConfig.phoneNumber);
        if (!redisTemplate.hasKey(tokenKey)) {
            val token = attendanceService.createToken()
            this.token = token
            redisTemplate.opsForValue().set(tokenKey, token, 30, TimeUnit.DAYS);
        } else {
            val token = redisTemplate.opsForValue().get(tokenKey) as String
            this.token = token
        }
    }

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

    @Test
    fun upload() {
        val attachment = File("./fit_4096.jpeg");
        attendanceService.uploadPic(FileInputStream(attachment), attachment.name, token)
    }
}
