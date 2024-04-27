package com.k92.khronos.task

import com.k92.khronos.config.KhronosConfig
import com.k92.khronos.service.AttendanceService
import com.k92.khronos.service.GeoService
import jakarta.annotation.PostConstruct
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.io.File
import java.lang.Exception
import java.util.*

@Component
class ClockTask(val khronosConfig: KhronosConfig, val attendanceService: AttendanceService, val geoService: GeoService) {

    private fun doClock(path: String, offsetUpper: Boolean) {
//        val dir = File(khronosConfig.morningStartFileDir)
        val dir = File(path)
        if (dir.isFile) {
            println("打卡照片目录${path}异常，请检查它是否是一个文件夹")
            // todo 报警
            return
        }
        val files = dir.listFiles { _, name ->
            val fileExtension = name.substring(name.lastIndexOf(".") + 1).lowercase(Locale.getDefault());
            val imageExtensions = listOf("jpg", "jpeg", "png", "gif")
            imageExtensions.stream().anyMatch { it.equals(fileExtension, true) }
        }
        if (files == null || files.isEmpty()) {
            println("打卡照片目录${path}为空, 无法自动打卡")
            // todo 报警
            return
        }
        val randomPic = files.random()
        val randomLocation = khronosConfig.locationList.random();
        try {
            val res = geoService.getLocation(randomLocation);
            var timestamp = System.currentTimeMillis()
            val offset = Random().nextInt(0, khronosConfig.scheduled.offset) * if (offsetUpper) 1 else -1
            timestamp += offset
            attendanceService.requestAttendance(timestamp, res.result.location.lat, res.result.location.lng, randomLocation, randomPic, 0)
            randomPic.delete()
        } catch (e: Exception) {
            // todo 报警
            e.printStackTrace()
        }
        // 打卡完成后删除照片
    }

    fun runMorningStartClock() {
       doClock(khronosConfig.morningStartFileDir, false)
    }

    fun runMorningEndClock() {
        doClock(khronosConfig.morningEndFileDir, true)
    }

    fun runAfternoonStartClock() {
        doClock(khronosConfig.afternoonStartFileDir, false)
    }

    fun runAfternoonEndClock() {
//        println("测试晚加班打卡")
        doClock(khronosConfig.afternoonEndFileDir, true)
    }

    fun runNightAddClock() {
        doClock(khronosConfig.nightAddFileDir, true)
    }
}