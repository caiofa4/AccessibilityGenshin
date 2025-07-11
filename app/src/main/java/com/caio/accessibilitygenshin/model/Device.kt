package com.caio.accessibilitygenshin.model

class Device(
    val height: Int,
    val width: Int
) {
    fun isOpenFoldable(): Boolean {
        val ratio = minOf(width, height).toDouble() / maxOf(width, height)
        return ratio > 0.8
    }
}