package com.k92.khronos

import com.k92.khronos.service.GeoService
import com.k92.khronos.task.ClockTask
import jakarta.annotation.Resource
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

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
}
