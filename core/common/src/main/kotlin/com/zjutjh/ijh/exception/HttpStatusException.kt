package com.zjutjh.ijh.exception

import java.io.IOException

class HttpStatusException(val code: Int) : IOException() {
    override fun toString(): String {
        return "HttpStatusException(code=$code)"
    }
}