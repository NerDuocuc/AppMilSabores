package com.example.appmilsabores.utils

object Logger {
    fun d(tag: String, message: String) {
        try {
            android.util.Log.d(tag, message)
        } catch (t: Throwable) {
            println("D/$tag: $message")
        }
    }

    fun w(tag: String, message: String, t: Throwable? = null) {
        try {
            if (t != null) android.util.Log.w(tag, message, t) else android.util.Log.w(tag, message)
        } catch (e: Throwable) {
            println("W/$tag: $message - ${t?.message}")
        }
    }

    fun e(tag: String, message: String, t: Throwable? = null) {
        try {
            if (t != null) android.util.Log.e(tag, message, t) else android.util.Log.e(tag, message)
        } catch (e: Throwable) {
            System.err.println("E/$tag: $message - ${t?.message}")
        }
    }
}
