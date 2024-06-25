package com.hextremelabs.service;

import com.hextremelabs.exception.DataNotFoundException;
import com.hextremelabs.model.Movie;
import com.hextremelabs.model.Rent;
import com.hextremelabs.repository.MovieRepository;
import com.hextremelabs.repository.RentRepository;
import com.hextremelabs.util.DateTimeHelper;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

import static com.hextremelabs.enumeration.RentStatus.RENTED;
import static java.lang.String.format;
import static java.util.Arrays.asList;


@Transactional
@ApplicationScoped
public class MovieRentalService {

  private final DateTimeHelper dateTimeHelper = new DateTimeHelper();

  private MovieRepository movieRepository;
  private RentRepository rentRepository;

  @Inject
  public MovieRentalService(MovieRepository movieRepository, RentRepository rentRepository) {
    this.movieRepository = movieRepository;
    this.rentRepository = rentRepository;
  }

  public MovieRentalService() {
  }

  public List<Movie> getAllMovies(int currentPage, int batchSize) {
    return movieRepository.findAll(((currentPage - 1) * batchSize), batchSize);
  }

  public List<Rent> getAllRents(int currentPage, int batchSize) {
    return rentRepository.findAll(((currentPage - 1) * batchSize), batchSize);
  }

  private Movie decreaseMovieAvailableQty(Long movieId) {
    final Movie movie = movieRepository.findBy(movieId);
    if (movie == null) {
      throw new DataNotFoundException(format("Movie with id '%d' not found!", movieId));
    }
    final long availableQuantity = movie.getAvailableQuantity() - 1;
    if (availableQuantity < 1) {
      throw new IllegalArgumentException(format("Movie with id '%d' is not available for rent!", movieId));
    }
    movie.setAvailableQuantity(availableQuantity);
    movieRepository.save(movie);
    return movie;
  }

  private void increaseMovieAvailableQty(Long movieId) {
    final Movie movie = movieRepository.findBy(movieId);

    if (movie == null) {
      throw new DataNotFoundException(format("Movie with id '%d' not found!", movieId));
    }
    final long availableQty = movie.getAvailableQuantity() + 1;
    movie.setAvailableQuantity(availableQty);
    movieRepository.save(movie);
  }

  public Rent reserveMovie(Long movieId) {
    final var movie = decreaseMovieAvailableQty(movieId);
    final var rent = new Rent(movie);
    rentRepository.save(rent);
    return rent;
  }

  public Rent rentMovie(Long rentId) {
    final Rent rent = rentRepository.findBy(rentId);
    if (rent == null) {
      throw new DataNotFoundException(format("Rent record with id '%d' not found!", rentId));
    }
    rent.setStatus(RENTED);
    rentRepository.save(rent);
    return rent;
  }

  public void releaseOverdueReservedMovies() {
    final int reservationLifespanMinutes = 5;
    for (Rent rent : rentRepository.findAll()) {
      if (dateTimeHelper.getTimeDiff(rent.getCreationTime()) > reservationLifespanMinutes) {
        returnMovie(rent.getId());
      }
    }
  }

  public List<Rent> returnMovie(Long rentId) {
    final Rent rent = rentRepository.findBy(rentId);

    if (rent == null) {
      throw new DataNotFoundException("Rent record with id '" + rentId + "' not found!");
    }
    long movie_id = rent.getMovie().getId();
    rentRepository.remove(rent);
    increaseMovieAvailableQty(movie_id);

    //TODO - Send a confirmation email to the user after successful return.

    return rentRepository.findAll();
  }

  public Movie getMovieById(Long id) {
    final Movie movie = movieRepository.findBy(id);
    if (movie == null) {
      throw new DataNotFoundException(format("Movie with id '%d' not found!", id));
    }
    return movie;
  }

  public Rent getRentById(Long id) {
    final Rent rent = rentRepository.findBy(id);
    if (rent == null) {
      throw new DataNotFoundException(format("Rent record with id '%d' not found!", id));
    }
    return rent;
  }

  public void setupDummyMoviesData() {
    asList(
        new Movie("RoboCop", "Paul Verhoeven"),
        new Movie("Teenage Mutant Ninja Turtles III", "Stuart Gillard"),
        new Movie("The Mask of Zorro", "Martin Campbell"),
        new Movie("Thunder Force", "Ben Falcone"),
        new Movie("Underwater", "William Eubank"),
        new Movie("Bad Boys for Life", "Adil El Arbi")
    ).forEach(table -> movieRepository.save(table));
  }
}
