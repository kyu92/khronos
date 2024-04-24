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
        }

    }

    var baseUrl = ""
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
}