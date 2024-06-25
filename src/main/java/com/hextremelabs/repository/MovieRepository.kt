package com.hextremelabs.repository

import com.hextremelabs.model.Movie
import org.apache.deltaspike.data.api.EntityRepository
import org.apache.deltaspike.data.api.Repository

@Repository
interface MovieRepository : EntityRepository<Movie, Long>
