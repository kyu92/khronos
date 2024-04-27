package com.k92.khronos.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties("khronos")
class KhronosConfig {

    companion object {

        class Scheduled {
            var morningStartCron = ""
            var morningEndCron = ""
            var afternoonStartCron = ""
            var afternoonEndCron = ""
            var nightAddCron = ""
            var offset: Int = 0
            override fun toString(): String {
                return "Scheduled(morningStartCron='$morningStartCron', morningEndCron='$morningEndCron', afternoonStartCron='$afternoonStartCron', afternoonEndCron='$afternoonEndCron', nightAddCron='$nightAddCron', offset=$offset)"
            }

        }
    }

    var baseUrl = ""
    var morningAddFileDir = ""
    var morningStartFileDir = ""
    var morningEndFileDir = ""
    var afternoonStartFileDir = ""
    var afternoonEndFileDir = ""
    var nightAddFileDir = ""
    var secret = ""
    var phoneNumber = ""

    var locationList: List<String> = ArrayList()

    var scheduled: Scheduled = Scheduled()

    var baiduAk = "";
    override fun toString(): String {
        return "KhronosConfig(baseUrl='$baseUrl', morningStartFileDir='$morningStartFileDir', morningEndFileDir='$morningEndFileDir', afternoonStartFileDir='$afternoonStartFileDir', afternoonEndFileDir='$afternoonEndFileDir', nightAddFileDir='$nightAddFileDir', secret='$secret', phoneNumber='$phoneNumber', locationList=$locationList, scheduled=$scheduled, baiduAk='$baiduAk')"
    }
}