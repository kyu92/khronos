package com.k92.khronos.task

import com.k92.khronos.config.KhronosConfig
import com.k92.khronos.service.AttendanceService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.io.File
import java.lang.Exception
import java.util.*

@Component
class ClockTask(val khronosConfig: KhronosConfig, val attendanceService: AttendanceService) {

    private fun doClock(path: String) {
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
            val res = attendanceService.getLocation(randomLocation);
            attendanceService.requestAttendance(System.currentTimeMillis(), res.result.location.lat, res.result.location.lng, randomLocation, randomPic, 0)
        } catch (e: Exception) {
            // todo 报警
            e.printStackTrace()
        }
        // 打卡完成后删除照片
        randomPic.delete()
    }

    @Scheduled(cron = "#{khronosConfig.scheduled.morningStartCron}")
    fun runMorningStartClock() {
       doClock(khronosConfig.morningStartFileDir)
    }

    @Scheduled(cron = "#{khronosConfig.scheduled.morningEndCron}")
    fun runMorningEndClock() {
        doClock(khronosConfig.morningEndFileDir)
    }

    @Scheduled(cron = "#{khronosConfig.scheduled.afternoonStartCron}")
    fun runAfternoonStartClock() {
        doClock(khronosConfig.afternoonStartFileDir)
    }

    @Scheduled(cron = "#{khronosConfig.scheduled.afternoonEndCron}")
    fun runAfternoonEndClock() {
        doClock(khronosConfig.afternoonEndFileDir)
    }

    @Scheduled(cron = "#{khronosConfig.scheduled.nightAddCron}")
    fun runNightAddClock() {
        doClock(khronosConfig.nightAddFileDir)
    }
}