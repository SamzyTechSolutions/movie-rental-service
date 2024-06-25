package com.hextremelabs.endpoint

import javax.ws.rs.client.ClientBuilder
import javax.ws.rs.core.Response

class RestClient {

  fun post(url: String): Response {
    return ClientBuilder.newClient()
      .target(url)
      .request()
      .post(null)
  }

  fun delete(url: String): Response {
    return ClientBuilder.newClient()
      .target(url)
      .request()
      .delete()
  }

  operator fun get(url: String): Response {
    return ClientBuilder.newClient()
      .target(url)
      .request()
      .get()
  }
}
