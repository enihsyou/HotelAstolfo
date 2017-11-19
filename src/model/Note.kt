package model

import java.time.LocalDateTime

data class Note(val body: String, val auther: User, val create_time: LocalDateTime)

data class User(val username: String)
