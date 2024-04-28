package com.k92.khronos.task

import com.k92.khronos.config.KhronosConfig
import com.k92.khronos.pojo.AttendanceResult
import com.k92.khronos.service.AttendanceService
import com.k92.khronos.service.GeoService
import com.k92.khronos.standard.Logger
import com.k92.khronos.standard.Logger.Companion.log
import org.joda.time.DateTime
import org.springframework.stereotype.Component
import java.io.File
import java.lang.Exception
import java.util.*

@Component
@Logger
class ClockTask(val khronosConfig: KhronosConfig, val attendanceService: AttendanceService, val geoService: GeoService) {

    private fun doClock(path: String, offsetUpper: Boolean): AttendanceResult {
//        val dir = File(khronosConfig.morningStartFileDir)
        val attendanceResult = AttendanceResult()
        val dir = File(path)
        if (dir.isFile) {
            println("打卡照片目录${path}异常，请检查它是否是一个文件夹")
            // todo 报警
            return attendanceResult
        }
        val files = dir.listFiles { _, name ->
            val fileExtension = name.substring(name.lastIndexOf(".") + 1).lowercase(Locale.getDefault());
            val imageExtensions = listOf("jpg", "jpeg", "png", "gif")
            imageExtensions.stream().anyMatch { it.equals(fileExtension, true) }
        }
        if (files == null || files.isEmpty()) {
            println("打卡照片目录${path}为空, 无法自动打卡")
            // todo 报警
            return attendanceResult
        }
        val randomPic = files.random()
        val randomLocation = khronosConfig.locationList.random();
        try {
            val res = geoService.getLocation(randomLocation);
            var timestamp = System.currentTimeMillis()
            val offset = Random().nextInt(0, khronosConfig.scheduled.offset) * if (offsetUpper) 1 else -1
            timestamp += offset * 1000
            attendanceService.requestAttendance(timestamp, res.result.location.lat, res.result.location.lng, randomLocation, randomPic, 0)
            // 打卡完成后删除照片
            randomPic.delete()
            attendanceResult.success = true
            attendanceResult.offset = offset
            attendanceResult.timestamp = timestamp
        } catch (e: Exception) {
            // todo 报警
            e.printStackTrace()
        }
        return attendanceResult
    }

    fun runMorningStartClock() {
        val result = doClock(khronosConfig.morningStartFileDir, false)
        log.info("上午上班打卡完成，结果: {}, 随机偏移时间: {}秒，打卡时间: {}", result.success, result.offset, DateTime(result.timestamp).toString("yyyy-MM-dd HH:mm:ss"))
    }

    fun runMorningEndClock() {
        val result = doClock(khronosConfig.morningEndFileDir, true)
        log.info("上午下班打卡完成，结果: {}, 随机偏移时间: {}秒，打卡时间: {}", result.success, result.offset, DateTime(result.timestamp).toString("yyyy-MM-dd HH:mm:ss"))
    }

    fun runAfternoonStartClock() {
        val result = doClock(khronosConfig.afternoonStartFileDir, false)
        log.info("下午上班打卡完成，结果: {}, 随机偏移时间: {}秒，打卡时间: {}", result.success, result.offset, DateTime(result.timestamp).toString("yyyy-MM-dd HH:mm:ss"))
    }

    fun runAfternoonEndClock() {
        val result = doClock(khronosConfig.afternoonEndFileDir, true)
        log.info("下午下班打卡完成，结果: {}, 随机偏移时间: {}秒，打卡时间: {}", result.success, result.offset, DateTime(result.timestamp).toString("yyyy-MM-dd HH:mm:ss"))
    }

    fun runNightAddClock() {
        val result = doClock(khronosConfig.nightAddFileDir, true)
        log.info("晚加班打卡完成，结果: {}, 随机偏移时间: {}秒，打卡时间: {}", result.success, result.offset, DateTime(result.timestamp).toString("yyyy-MM-dd HH:mm:ss"))
    }
}