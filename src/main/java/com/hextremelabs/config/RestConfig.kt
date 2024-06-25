package com.hextremelabs.config

import javax.ws.rs.ApplicationPath
import javax.ws.rs.core.Application

@ApplicationPath("/api")
class RestConfig : Application()