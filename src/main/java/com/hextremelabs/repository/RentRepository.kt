package com.hextremelabs.repository

import com.hextremelabs.model.Rent
import org.apache.deltaspike.data.api.EntityRepository
import org.apache.deltaspike.data.api.Repository

@Repository
interface RentRepository : EntityRepository<Rent, Long>
