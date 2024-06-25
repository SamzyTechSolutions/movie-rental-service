package com.hextremelabs.endpoint;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.jupiter.api.AfterEach;

import javax.inject.Inject;

/**
 * @author samuel
 */
abstract class AbstractIT {

  @Inject
  private DatabaseHandler databaseHandler;

  final static String RPC_BASE_URL = "http://127.0.0.1:8080/test/";

  @AfterEach
  public void teardown() {
    databaseHandler.wipeTables();
  }

  @Deployment
  public static WebArchive createDeployment() {
    return ShrinkWrap.create(WebArchive.class, "test.war")
        .addAsLibraries(Maven.resolver().loadPomFromFile("pom.xml")
            .resolve(
                "org.jetbrains.kotlin:kotlin-stdlib-jdk8",
                "org.apache.deltaspike.modules:deltaspike-data-module-api",
                "org.apache.deltaspike.modules:deltaspike-data-module-impl"
            )
            .withTransitivity()
            .asFile()
        )
        .addPackages(true, "com.hextremelabs")
        .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
        .addAsResource("test-apache-deltaspike.properties", "META-INF/apache-deltaspike.properties")
        .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
  }
}
