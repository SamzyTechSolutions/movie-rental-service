package com.hextremelabs.util

import com.hextremelabs.enumeration.ResponseCode
import com.hextremelabs.enumeration.ResponseCode.SUCCESS

data class AppResponse<ENTITY>(
  val code: ResponseCode = SUCCESS,
  val description: String = "Request completed successfully",
  val entity: ENTITY? = null
)
