package com.sorahjy.kotlintest

class KotlinTest {

    val randomPhoneNumber: String
        get() {
            var ans = "1"
            for (i in 0..9) {
                ans += (Math.random() * 10).toInt()
            }
            return ans
        }

}
