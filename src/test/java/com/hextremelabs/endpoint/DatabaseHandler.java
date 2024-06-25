package com.hextremelabs.endpoint;

import com.hextremelabs.model.Movie;
import com.hextremelabs.model.Rent;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Arrays;

/**
 * @author samuel
 */
@Transactional
@ApplicationScoped
public class DatabaseHandler {

  @PersistenceContext
  private EntityManager em;

  public void wipeTables() {
    final var tables = Arrays.asList(
        Rent.class,
        Movie.class
    );
    tables.forEach(clazz -> {
          final var tableName = clazz.getSimpleName();
          em.createQuery("DELETE FROM " + tableName).executeUpdate();
        }
    );
  }
}
