package main.utils

import java.lang.StringBuilder

/**
 * goodJob--->good_job
 */
fun String.toBuildGradleModuleName(): String {
    val stringBuilder = StringBuilder()
    this.toCharArray().forEach {
        if (it.isUpperCase()) {
            stringBuilder.append("_" + it.toLowerCase())
        } else {
            stringBuilder.append(it)
        }
    }

    return stringBuilder.toString()
}

/**
 * goodJob--->good.job
 */
fun String.toBuildGradleApplicationId(): String {
    val stringBuilder = StringBuilder()
    this.toCharArray().forEach {
        if (it.isUpperCase()) {
            stringBuilder.append("." + it.toLowerCase())
        } else {
            stringBuilder.append(it)
        }
    }

    return stringBuilder.toString()
}

/**
 * goodJob--->good
 */
fun String.toPackageName(): String {
    val stringBuilder = StringBuilder()
    this.toCharArray().forEach {
        if (it.isUpperCase()) {
            return stringBuilder.toString()
        } else {
            stringBuilder.append(it)
        }
    }

    return stringBuilder.toString()
}

/**
 * goodJob--->GoodJob
 */
fun String.toCustomUpCase(): String {
    val stringBuilder = StringBuilder()
    var index = 0
    this.toCharArray().forEach {
        if (index == 0) {
            stringBuilder.append(it.toUpperCase())
        } else {
            stringBuilder.append(it)
        }
        index++
    }
    return stringBuilder.toString()
}