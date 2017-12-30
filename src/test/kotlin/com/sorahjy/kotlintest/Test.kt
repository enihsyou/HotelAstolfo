package com.sorahjy.kotlintest

class Test internal constructor() {

    internal var phoneNumber: String

    val randomPhoneNumber: String
        get() {
            var phoneNumber = "1"
            for (i in 0..9) {
                phoneNumber += Math.random().toInt() * 10
            }
            return phoneNumber
        }

    init {
        this.phoneNumber = randomPhoneNumber
    }
}
