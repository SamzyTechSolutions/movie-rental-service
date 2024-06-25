package com.hextremelabs.exception

import com.hextremelabs.enumeration.ResponseCode
import com.hextremelabs.util.AppResponse
import javax.ws.rs.core.Response
import javax.ws.rs.core.Response.Status.NOT_FOUND
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider

@Provider
class DataNotFoundExceptionMapper : ExceptionMapper<DataNotFoundException> {
  override fun toResponse(exception: DataNotFoundException): Response {
    return Response.status(NOT_FOUND)
      .entity(AppResponse<Any>(ResponseCode.NOT_FOUND, exception.message.toString(), null))
      .build()
  }
}
