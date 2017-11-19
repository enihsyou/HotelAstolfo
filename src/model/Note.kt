package model

import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

data class Note(val body: String, val author: User, val createTime: LocalDateTime) {
  fun formatTime(): String =//    var between = Duration.between(LocalDateTime.now(), createTime)
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(createTime)
}

data class User(val username: String)
