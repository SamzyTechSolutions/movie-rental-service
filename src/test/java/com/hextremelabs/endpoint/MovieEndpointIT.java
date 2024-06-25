package com.hextremelabs.endpoint;

import com.hextremelabs.enumeration.ResponseCode;
import com.hextremelabs.model.Movie;
import com.hextremelabs.model.Rent;
import com.hextremelabs.service.MovieRentalService;
import com.hextremelabs.util.AppResponse;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.inject.Inject;
import javax.ws.rs.core.GenericType;
import java.util.List;

import static com.hextremelabs.enumeration.ResponseCode.SUCCESS;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.OK;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


@ExtendWith(ArquillianExtension.class)
class MovieEndpointIT extends AbstractIT {

  private final String BASE_URL = RPC_BASE_URL + "api";
  private static final int CURRENT_PAGE = 1;
  private static final int PAGE_BATCH_SIZE = 3;

  private final RestClient restClient = new RestClient();

  @Inject
  private MovieRentalService movieRentalService;

  @BeforeEach
  void setupDatabase() {
    movieRentalService.setupDummyMoviesData();
  }

  @Test
  @DisplayName("Get all movie records")
  void getAllMovies() {
    final var response = restClient.get(BASE_URL + "/movies?page=1&batch=6");
    assertEquals(OK.getStatusCode(), response.getStatus());
    final var appResponse = response.readEntity(new GenericType<AppResponse<List<Movie>>>() {});
    assertEquals(SUCCESS, appResponse.getCode());
    assertEquals(6, appResponse.getEntity().size());
    assertEquals("Page 1 Retrieved Successfully!", appResponse.getDescription());
  }

  @Test
  @DisplayName("Get all rent records")
  void getAllRents() {
    final var response = restClient.get(BASE_URL + "/rents");
    assertEquals(OK.getStatusCode(), response.getStatus());
    final var appResponse = response.readEntity(new GenericType<AppResponse<List<Rent>>>() {});
    assertEquals(SUCCESS, appResponse.getCode());
    assertEquals(0, appResponse.getEntity().size());
  }

  @Test
  @DisplayName("Get movie by id when movie exists")
  void getMovieById() {
    final var movieId = getLatestMovieId();
    final var response = restClient.get(BASE_URL + "/movie/" + movieId);
    assertEquals(OK.getStatusCode(), response.getStatus());
    final var appResponse = response.readEntity(new GenericType<AppResponse<Movie>>() {});
    assertEquals(SUCCESS, appResponse.getCode());
    assertEquals("RoboCop", appResponse.getEntity().getName());
    assertEquals("Paul Verhoeven", appResponse.getEntity().getDirector());
    assertEquals(50, appResponse.getEntity().getQuantity());
    assertEquals(50, appResponse.getEntity().getAvailableQuantity());
  }

  @Test
  @DisplayName("Get movie by id when movie doesn't exists")
  void getMovieByIdNotFound() {
    final var response = restClient.get(BASE_URL + "/movie/10");
    assertEquals(NOT_FOUND.getStatusCode(), response.getStatus());
    final var appResponse = response.readEntity(AppResponse.class);
    assertNull(appResponse.getEntity());
    assertEquals(ResponseCode.NOT_FOUND, appResponse.getCode());
  }

  @Test
  @DisplayName("Get rent record by rent-id when record exists")
  void getRentById() {
    final var movieId = getLatestMovieId();
    restClient.post(BASE_URL + "/movie/" + movieId + "/reserve");

    final var rentId = getLatestRentId();
    final var response = restClient.get(BASE_URL + "/rent/" + rentId);
    assertEquals(OK.getStatusCode(), response.getStatus());
    final var appResponse = response.readEntity(AppResponse.class);
    assertEquals(SUCCESS, appResponse.getCode());
  }

  @Test
  @DisplayName("Get rent record by rent-id when record does not exists")
  void getRentByIdNotFound() {
    final var response = restClient.get(BASE_URL + "/rent/1");
    assertEquals(NOT_FOUND.getStatusCode(), response.getStatus());
    final var appResponse = response.readEntity(AppResponse.class);
    assertNull(appResponse.getEntity());
    assertEquals(ResponseCode.NOT_FOUND, appResponse.getCode());
  }

  @Test
  @DisplayName("Reserve movie for movie record that exists")
  void reserveMovie() {
    final var movieId = getLatestMovieId();
    final var response = restClient.post(BASE_URL + "/movie/" + movieId + "/reserve");
    assertEquals(OK.getStatusCode(), response.getStatus());
    final var appResponse = response.readEntity(AppResponse.class);
    assertEquals(SUCCESS, appResponse.getCode());
  }

  @Test
  @DisplayName("Rent a reserved movie using rent record id that exists")
  void rentMovie() {
    final var movieId = getLatestMovieId();
    restClient.post(BASE_URL + "/movie/" + movieId + "/reserve");

    final var rentId = getLatestRentId();
    final var response = restClient.post(BASE_URL + "/movie/" + rentId + "/rent");
    assertEquals(OK.getStatusCode(), response.getStatus());
    final var appResponse = response.readEntity(AppResponse.class);
    assertEquals(SUCCESS, appResponse.getCode());
  }

  @Test
  @DisplayName("Return a rented movie using a rent record id that exists")
  void returnMovie() {
    final var movieId = getLatestMovieId();
    restClient.post(BASE_URL + "/movie/" + movieId + "/reserve");

    final var rentId = getLatestRentId();
    restClient.post(BASE_URL + "/movie/" + rentId + "/rent");
    final var response = restClient.delete(BASE_URL + "/movie/" + rentId + "/rent");
    assertEquals(OK.getStatusCode(), response.getStatus());
    final var appResponse = response.readEntity(AppResponse.class);
    assertEquals(SUCCESS, appResponse.getCode());
    assertEquals("Movie returned successfully!", appResponse.getDescription());
  }

  private long getLatestMovieId() {
    List<Movie> movies = movieRentalService.getAllMovies(CURRENT_PAGE, PAGE_BATCH_SIZE);
    return movies.isEmpty() ? 0 : movies.get(0).getId();
  }

  private long getLatestRentId() {
    List<Rent> rents = movieRentalService.getAllRents(CURRENT_PAGE, PAGE_BATCH_SIZE);
    return rents.isEmpty() ? 0 : rents.get(0).getId();
  }
}
