package com.k92.khronos.standard

import java.math.BigDecimal
import kotlin.properties.Delegates

class BaiduEncodeResult {

    var status by Delegates.notNull<Int>();
    lateinit var result: Result;

    companion object {
        class Result {
            lateinit var location: Location;
            lateinit var level: String;
            var precise by Delegates.notNull<Int>();
            var confidence by Delegates.notNull<Int>();
            var comprehension by Delegates.notNull<Int>();

            override fun toString(): String {
                return "Result(location=$location, level='$level', precise=$precise, confidence=$confidence, comprehension=$comprehension)"
            }
        }

        class Location {
            lateinit var lng: BigDecimal;
            lateinit var lat: BigDecimal;
            override fun toString(): String {
                return "Location(lng=$lng, lat=$lat)"
            }
        }
    }

    override fun toString(): String {
        return "BaiduEncodeResult(status=$status, result=$result)"
    }
}