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
        }

        class Location {
            lateinit var lng: BigDecimal;
            lateinit var lat: BigDecimal;
        }
    }
}