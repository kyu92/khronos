package com.k92.khronos.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.k92.khronos.config.KhronosConfig
import com.k92.khronos.standard.AttendanceException
import com.k92.khronos.standard.BaiduEncodeResult
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class GeoService(val khronosConfig: KhronosConfig, val restTemplate: RestTemplate, val objectMapper: ObjectMapper) {

    fun getLocation(address: String): BaiduEncodeResult {
        val url = "https://api.map.baidu.com/geocoding/v3/?address=$address&output=json&ak=${khronosConfig.baiduAk}";
        val response = restTemplate.getForEntity(url, String::class.java)
        if (response.statusCode == HttpStatus.OK) {
            val resultJson = response.body!!
            return objectMapper.readValue(resultJson, BaiduEncodeResult::class.java)
        }
        throw AttendanceException("获取坐标信息失败");
    }
}