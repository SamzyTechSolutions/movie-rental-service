package com.hextremelabs.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.hextremelabs.enumeration.RentStatus
import com.hextremelabs.util.DateTimeHelper
import com.hextremelabs.util.InstantAdapter
import java.time.Instant
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.ForeignKey
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table
import javax.persistence.Version
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter

@Entity
@Table(name = "rent")
class Rent @JvmOverloads constructor(

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  var id: Long = 0,

  @ManyToOne
  @JoinColumn(name = "movie", nullable = false, foreignKey = ForeignKey(name = "FK_rent_movie"))
  var movie: Movie,

  @Column(name = "status", nullable = false)
  var status: RentStatus = RentStatus.RESERVED,

  @Column(name = "creation_time", nullable = false)
  @get:XmlJavaTypeAdapter(InstantAdapter::class) var creationTime: Instant = DateTimeHelper().currentTime(),

  @JsonIgnore
  @Version
  @Column(name = "version", nullable = false)
  var version: Long = 0
)
