package com.hextremelabs.repository

import javax.enterprise.context.ApplicationScoped
import javax.enterprise.inject.Produces
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@ApplicationScoped
class EntityManagerProducer {
  @Produces
  @PersistenceContext
  private lateinit var entityManager: EntityManager
}
