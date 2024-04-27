package com.k92.khronos.controller

import com.k92.khronos.config.KhronosConfig
import com.k92.khronos.pojo.PicResponse
import com.k92.khronos.service.AttendanceService
import com.k92.khronos.service.GeoService
import com.k92.khronos.standard.AttendanceException
import com.k92.khronos.standard.AttendanceType
import com.k92.khronos.standard.ResultMap
import com.k92.khronos.util.ImageUtil
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.util.Objects
import java.util.UUID

@RestController
@RequestMapping("/control")
class SystemController(val khronosConfig: KhronosConfig, val geoService: GeoService, val attendanceService: AttendanceService) {

    private fun getDir(type: AttendanceType) : File {
        return when (type) {
            AttendanceType.MORNING_ADD -> File(khronosConfig.morningAddFileDir)
            AttendanceType.MORNING_START -> File(khronosConfig.morningStartFileDir)
            AttendanceType.MORNING_END -> File(khronosConfig.morningEndFileDir)
            AttendanceType.AFTERNOON_START -> File(khronosConfig.afternoonStartFileDir)
            AttendanceType.AFTERNOON_END -> File(khronosConfig.afternoonEndFileDir)
            AttendanceType.NIGHT_ADD -> File(khronosConfig.nightAddFileDir)
        }
    }

    @Throws(IOException::class)
    private fun savePic(bytes: ByteArray, fileName: String?, type: AttendanceType) {
        val dir = getDir(type)
        val name = fileName.let { if (Objects.isNull(fileName)) "${UUID.randomUUID()}.jpg" else fileName }
        val file = File("${dir.absolutePath}${File.pathSeparatorChar}$name")
        val fileInputStream = FileInputStream(file)
        fileInputStream.read(bytes)
        fileInputStream.close()
    }

    @PutMapping("/pic")
    fun addPic(@RequestPart("file") file: MultipartFile, @RequestParam("type") attendanceType: AttendanceType): ResultMap<Void> {
        return try {
            savePic(file.bytes, file.originalFilename, attendanceType)
            ResultMap.success()
        } catch (e: Exception) {
            ResultMap.failed(-1, e.message)
        }
    }

    @DeleteMapping("/pic")
    fun delPic(@RequestParam("path") path: String): ResultMap<Void> {
        val file = File(path)
        if (file.exists()) {
            file.delete()
        }
        return ResultMap.success();
    }

    @GetMapping("/pic")
    fun listPic(@RequestParam("type") attendanceType: AttendanceType): ResultMap<List<PicResponse>> {
        val dir = getDir(attendanceType)
        val files = dir.listFiles()
        val response = mutableListOf<PicResponse>()
        files?.forEach {
            val suffix = it.name.substring(it.name.lastIndexOf(".") + 1)
            var src = ImageUtil.compressImage(it.readBytes(), 0.6F, suffix)
            src = ImageUtil.resize(src, 367, 480, suffix)
            response.add(
                PicResponse(src, it.absolutePath)
            )
        }
        return ResultMap.success();
    }

    @PostMapping("/attendance")
    fun customAttendance(file: MultipartFile, @RequestParam("address") address: String, timestamp: Long): ResultMap<Void> {
        try {
            val location = geoService.getLocation(address)
            attendanceService.requestAttendance(
                timestamp,
                location.result.location.lng,
                location.result.location.lat,
                address,
                file.inputStream,
                file.originalFilename!!,
                0
            )
            return ResultMap.success()
        } catch (e: AttendanceException) {
            e.printStackTrace()
            return ResultMap.failed(-1, e.message)
        }
    }
}