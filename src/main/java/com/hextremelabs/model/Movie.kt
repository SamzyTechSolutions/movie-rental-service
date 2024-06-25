package com.hextremelabs.model

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table
import javax.persistence.Version


@Entity
@Table(name = "movie")
class Movie {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  var id: Long = 0

  @Column(name = "name", nullable = false)
  var name: String

  @Column(name = "director", nullable = false)
  var director: String

  @Column(name = "quantity", nullable = false)
  var quantity: Long = 50

  @Column(name = "available_quantity", nullable = false)
  var availableQuantity: Long = 50

  @JsonIgnore
  @Version
  @Column(name = "version", nullable = false)
  var version: Long = 0

  constructor(name: String, director: String) {
    this.name = name
    this.director = director
  }
}
