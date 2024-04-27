package com.k92.khronos.controller

import com.k92.khronos.pojo.PicResponse
import com.k92.khronos.standard.ResultMap
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/control")
class SystemController {

    @PutMapping("/pic")
    fun addPic(@RequestPart("file") file: MultipartFile): ResultMap<Void> {

        return ResultMap.success();
    }

    @DeleteMapping("/pic")
    fun delPic(path: String): ResultMap<Void> {

        return ResultMap.success();
    }

    @GetMapping("/pic")
    fun listPic(@RequestParam("picType") picType: Int): ResultMap<List<PicResponse>> {

        return ResultMap.success();
    }

    @PostMapping("/attendance")
    fun customAttendance() {

    }
}