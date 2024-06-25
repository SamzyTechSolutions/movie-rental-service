package com.hextremelabs.util

import java.time.Instant
import java.time.temporal.ChronoUnit

class DateTimeHelper {

  fun currentTime() = Instant.now()

  fun getTimeDiff(instant: Instant): Long {
    return ChronoUnit.MINUTES.between(currentTime(), instant)
  }
}
