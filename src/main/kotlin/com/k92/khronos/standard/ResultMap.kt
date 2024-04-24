package com.k92.khronos.standard

class ResultMap<E>(val statusCode: Int, val success: Boolean, val message: String?, val data: E?): Cloneable {

    companion object {
        fun <E> success(): ResultMap<E> {
            return ResultMap(0, true)
        }

        fun <E> success(statusCode: Int): ResultMap<E> {
            return ResultMap(statusCode, true)
        }

        fun <E> success(message: String?): ResultMap<E> {
            return ResultMap(0, true, message, null)
        }

        fun <E> success(statusCode: Int, message: String?): ResultMap<E> {
            return ResultMap(statusCode, true, message, null)
        }

        fun <E> success(data: E): ResultMap<E> {
            return ResultMap(0, true, null, data)
        }

        fun <E> success(statusCode: Int, data: E): ResultMap<E> {
            return ResultMap(statusCode, true, null, data)
        }

        fun <E> success(statusCode: Int, message: String?, data: E): ResultMap<E> {
            return ResultMap(statusCode, true, message, data)
        }

        fun <E> failed(statusCode: Int): ResultMap<E> {
            return ResultMap(statusCode, false)
        }

        fun <E> failed(statusCode: Int, message: String?): ResultMap<E> {
            return ResultMap(statusCode, false, message, null)
        }

        fun <E> failed(statusCode: Int, data: E): ResultMap<E> {
            return ResultMap(statusCode, false, null, data)
        }

        fun <E> failed(statusCode: Int, message: String?, data: E): ResultMap<E> {
            return ResultMap(statusCode, false, message, data)
        }

        inline fun <reified E> cast(resultMap: Any, clazz: Class<E>): ResultMap<E> {
            if (resultMap is ResultMap<*> && resultMap.data is E) {
                return ResultMap(resultMap.statusCode, resultMap.success, resultMap.message, resultMap.data)
            }
            throw ClassCastException("参数resultMap不是ResultMap类的实例")
        }
    }

    constructor(statusCode: Int, success: Boolean) : this(statusCode, success, null, null)

    @Throws(CloneNotSupportedException::class)
    override fun clone(): ResultMap<*> {
        return super.clone() as ResultMap<*>
    }
}