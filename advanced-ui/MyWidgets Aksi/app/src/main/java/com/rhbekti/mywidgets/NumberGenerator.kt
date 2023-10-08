package com.rhbekti.mywidgets

import java.util.Random

internal object NumberGenerator {
    fun generate(max: Int): Int {
        val random = Random()
        return random.nextInt(max)
    }
}