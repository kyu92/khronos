package com.k92.khronos.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.k92.khronos.config.KhronosConfig
import com.k92.khronos.standard.AttendanceException
import com.k92.khronos.standard.BaiduEncodeResult
import com.k92.khronos.standard.ResultMap
import jakarta.annotation.PostConstruct
import org.springframework.core.io.FileSystemResource
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate
import java.io.File
import java.math.BigDecimal
import javax.management.OperationsException

@Service
class AttendanceService(val khronosConfig: KhronosConfig, val redisTemplate: RedisTemplate<String, Any>, val restTemplate: RestTemplate) {

    lateinit var objectMapper: ObjectMapper;

    companion object {

        fun getTokenKey(phone: String): String {
            return "qiantu:token:$phone"
        }
    }

    @PostConstruct
    fun init() {
        objectMapper = ObjectMapper();
        val tokenKey = getTokenKey(khronosConfig.phoneNumber);
        if (!redisTemplate.hasKey(tokenKey)) {
            val token = createToken();
            redisTemplate.opsForValue().set(tokenKey, token);
        }
    }

    fun requestAttendance(timestamp: Long, lon: BigDecimal, lat: BigDecimal, address: String, attachment: File, retryCount: Int) {
        val tokenKey = "qiantu:token:${khronosConfig.phoneNumber}";
        try {
            val token: String;
            if (redisTemplate.hasKey(tokenKey)) {
                token = redisTemplate.opsForValue().get(tokenKey).toString()
            } else {
                token = createToken();
                redisTemplate.opsForValue().set(tokenKey, token);
            }
            val attachmentId = uploadPic(attachment, token);
            attendance(timestamp, lon, lat, address, attachmentId, token)
        } catch (e: AttendanceException) {
            e.printStackTrace()
            if (retryCount < 3) {
                requestAttendance(timestamp, lon, lat, address, attachment, retryCount + 1)
            }
            throw OperationsException("打卡失败，且重复3次后依然失败");
        }
    }

    fun createToken(): String {
        val response = restTemplate.getForEntity("${khronosConfig.baseUrl}/auth/token/${khronosConfig.phoneNumber}?secret=${khronosConfig.secret}", String::class.java)
        if (response.statusCode == HttpStatus.OK) {
            return response.body!!
        }
        throw AttendanceException("获取token失败");
    }

    fun uploadPic(attachment: File, token: String): Int {
        val url = "${khronosConfig.baseUrl}/attachment/attendance";
        val fileSystemResource = FileSystemResource(attachment)

        val params: MultiValueMap<String, Any> = LinkedMultiValueMap()
        val headers = HttpHeaders()
        params.add("file", fileSystemResource)
        headers.contentType = MediaType.MULTIPART_FORM_DATA
        headers["Authorization"]= token
        val requestEntity = HttpEntity(params, headers)
        val response = restTemplate.postForEntity(url, requestEntity, ResultMap::class.java)
        if (response.statusCode != HttpStatus.OK) {
            throw AttendanceException("上传打卡照片失败, 响应错误：" + response.statusCode)
        }
        val body = response.body!!
        if (!body.success) {
            throw AttendanceException("上传打卡照片失败, 接口响应失败，原因：" + body.message)
        }
        return (body.data as Int)

    }

    private fun attendance(timestamp: Long, lon: BigDecimal, lat: BigDecimal, address: String, attachmentId: Int, token: String) {
        val url = "${khronosConfig.baseUrl}/rule/clock"
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        headers["Authorization"]= token
        val params: MutableMap<String, Any> = HashMap()
        params["timestamp"] = timestamp
        params["lon"] = lon
        params["lat"] = lat
        params["clockAddress"] = address
        params["attachmentId"] = attachmentId
        val paramJson = objectMapper.writeValueAsString(params);
        val requestEntity = HttpEntity(paramJson, headers)
        val response = restTemplate.postForEntity(url, requestEntity, ResultMap::class.java)
        if (response.statusCode != HttpStatus.OK) {
            throw AttendanceException("打卡失败");
        }
        if (!response.body!!.success) {
            throw AttendanceException("打卡失败");
        }
    }
}