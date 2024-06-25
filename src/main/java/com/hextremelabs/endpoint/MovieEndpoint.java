package com.hextremelabs.endpoint;

import com.hextremelabs.model.Movie;
import com.hextremelabs.model.Rent;
import com.hextremelabs.service.MovieRentalService;
import com.hextremelabs.util.AppResponse;

import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.util.List;

import static com.hextremelabs.enumeration.ResponseCode.SUCCESS;
import static java.lang.String.format;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/")
public class MovieEndpoint {

  private MovieRentalService movieService;

  @Inject
  public MovieEndpoint(MovieRentalService movieService) {
    this.movieService = movieService;
  }

  public MovieEndpoint() {
  }

  @GET
  @Produces(APPLICATION_JSON)
  @Path("movies")
  public AppResponse<List<Movie>> getAllMovies(
      @DefaultValue("1") @QueryParam("page") int currentPage,
      @DefaultValue("3") @QueryParam("batch") int batchSize
  ) {
    final var allMovies = movieService.getAllMovies(currentPage, batchSize);
    return new AppResponse<>(
        SUCCESS,
        format("Page %d Retrieved Successfully!", currentPage),
        allMovies
    );
  }

  @GET
  @Produces(APPLICATION_JSON)
  @Path("rents")
  public AppResponse<List<Rent>> getAllRents(
      @DefaultValue("1") @QueryParam("page") int currentPage,
      @DefaultValue("3") @QueryParam("batch") int batchSize
  ) {
    final var allRents = movieService.getAllRents(currentPage, batchSize);
    return new AppResponse<>(
        SUCCESS,
        format("Page %d Retrieved Successfully!", currentPage),
        allRents
    );
  }

  @GET
  @Produces(APPLICATION_JSON)
  @Path("movie/{id}")
  public AppResponse<Movie> getMovieById(@PathParam("id") Long id) {
    final var movie = movieService.getMovieById(id);
    return new AppResponse<Movie>(SUCCESS, "Retrieved Successfully!", movie);
  }

  @GET
  @Produces(APPLICATION_JSON)
  @Path("rent/{id}")
  public AppResponse<Rent> getRentById(@PathParam("id") Long id) {
    final var rent = movieService.getRentById(id);
    return new AppResponse<>(SUCCESS, "Rent record retrieved Successfully!", rent);
  }

  @POST
  @Produces(APPLICATION_JSON)
  @Path("movie/{id}/reserve")
  public AppResponse<Rent> reserveMovie(@PathParam("id") Long movieId) {
    final var rent = movieService.reserveMovie(movieId);
    return new AppResponse<>(SUCCESS, "Movie reserved Successfully!", rent);
  }

  @POST
  @Produces(APPLICATION_JSON)
  @Path("movie/{id}/rent")
  public AppResponse<Rent> rentMovie(@PathParam("id") Long rentId) {
    final var rent = movieService.rentMovie(rentId);
    return new AppResponse<>(SUCCESS, "Movie rented successfully!", rent);
  }

  @DELETE
  @Produces(APPLICATION_JSON)
  @Path("movie/{id}/rent")
  public AppResponse<List<Rent>> returnMovie(@PathParam("id") Long rentId) {
    final var allRentRecords = movieService.returnMovie(rentId);
    return new AppResponse<>(SUCCESS, "Movie returned successfully!", allRentRecords);
  }
}
