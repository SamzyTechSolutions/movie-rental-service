package com.hextremelabs.util

import java.time.Instant
import java.time.format.DateTimeFormatter
import javax.xml.bind.annotation.adapters.XmlAdapter

class InstantAdapter : XmlAdapter<String, Instant>() {

  override fun unmarshal(dateString: String): Instant {
    return Instant.parse(dateString)
  }

  @Throws(Exception::class)
  override fun marshal(localDate: Instant): String {
    return DateTimeFormatter.ISO_INSTANT.format(localDate)
  }
}
