package com.k92.khronos.standard

import com.fasterxml.jackson.annotation.JsonValue

enum class AttendanceType(@JsonValue val code: Int, val message: String) {

    MORNING_ADD(1, "早加班"),
    MORNING_START(2, "上午上班"),
    MORNING_END(3, "上午下班"),
    AFTERNOON_START(4, "下午上班"),
    AFTERNOON_END(5, "下午下班"),
    NIGHT_ADD(6, "晚加班"),
    ;
}