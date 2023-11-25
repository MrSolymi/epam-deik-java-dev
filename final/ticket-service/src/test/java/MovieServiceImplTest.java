import com.epam.training.ticketservice.dto.MovieDto;
import com.epam.training.ticketservice.exceptions.AlreadyExistsException;
import com.epam.training.ticketservice.exceptions.NotFoundException;
import com.epam.training.ticketservice.model.Movie;
import com.epam.training.ticketservice.repositories.MovieRepository;
import com.epam.training.ticketservice.services.impl.MovieServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MovieServiceImplTest {
    @Mock
    MovieRepository movieRepository;

    @InjectMocks
    MovieServiceImpl movieService;

    @Test
    void testCreateMovie() {
        String movieTitle = "TestMovie";
        String movieType = "Action";
        int movieLength = 120;

        when(movieRepository.findByTitle(movieTitle)).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> movieService.createMovie(movieTitle, movieType, movieLength));

        verify(movieRepository).save(Mockito.argThat(movie -> movie.getTitle().equals(movieTitle)
                && movie.getType().equals(movieType)
                && movie.getLength() == movieLength));
    }

    @Test
    void testCreateMovieAlreadyExists() {
        String existingMovieTitle = "ExistingMovie";
        String movieType = "Action";
        int movieLength = 120;

        Movie existingMovie = new Movie(existingMovieTitle, movieType, movieLength);
        when(movieRepository.findByTitle(existingMovieTitle)).thenReturn(Optional.of(existingMovie));

        AlreadyExistsException exception = assertThrows(AlreadyExistsException.class, () -> {
            movieService.createMovie(existingMovieTitle, movieType, movieLength);
        });

        assertEquals("The movie with the given title already exist", exception.getMessage());

        Mockito.verify(movieRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void testUpdateMovie() {
        String movieTitle = "ExistingMovie";
        String updatedMovieType = "Comedy";
        int updatedMovieLength = 90;

        Movie existingMovie = new Movie(movieTitle, "Action", 120);
        when(movieRepository.findByTitle(movieTitle)).thenReturn(Optional.of(existingMovie));

        assertDoesNotThrow(() -> movieService.updateMovie(movieTitle, updatedMovieType, updatedMovieLength));

        verify(movieRepository).save(Mockito.argThat(movie -> movie.getTitle().equals(movieTitle)
                && movie.getType().equals(updatedMovieType)
                && movie.getLength() == updatedMovieLength));
    }

    @Test
    void testUpdateMovieNotFound() {
        String nonExistingMovieTitle = "NonExistingMovie";
        String movieType = "Action";
        int movieLength = 120;

        when(movieRepository.findByTitle(nonExistingMovieTitle)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            movieService.updateMovie(nonExistingMovieTitle, movieType, movieLength);
        });

        assertEquals("The movie with the given title not found", exception.getMessage());

        Mockito.verify(movieRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void testDeleteMovie() {
        String movieTitle = "ExistingMovie";

        Movie existingMovie = new Movie(movieTitle, "Action", 120);
        when(movieRepository.findByTitle(movieTitle)).thenReturn(Optional.of(existingMovie));

        assertDoesNotThrow(() -> movieService.deleteMovie(movieTitle));

        verify(movieRepository).deleteByTitle(movieTitle);
    }

    @Test
    void testDeleteMovieNotFound() {
        String nonExistingMovieTitle = "NonExistingMovie";

        when(movieRepository.findByTitle(nonExistingMovieTitle)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            movieService.deleteMovie(nonExistingMovieTitle);
        });

        assertEquals("The movie with the given title not found", exception.getMessage());

        Mockito.verify(movieRepository, Mockito.never()).deleteByTitle(Mockito.any());
    }

    @Test
    void testGetMovieList() {
        Movie movie1 = new Movie("Movie1", "Action", 120);
        Movie movie2 = new Movie("Movie2", "Comedy", 90);

        List<Movie> movies = List.of(movie1, movie2);
        when(movieRepository.findAll()).thenReturn(movies);

        List<MovieDto> movieDtos = movieService.getMovieList();

        assertEquals(2, movieDtos.size());
        assertEquals("Movie1", movieDtos.get(0).getTitle());
        assertEquals("Action", movieDtos.get(0).getType());
        assertEquals(120, movieDtos.get(0).getLength());
        assertEquals("Movie2", movieDtos.get(1).getTitle());
        assertEquals("Comedy", movieDtos.get(1).getType());
        assertEquals(90, movieDtos.get(1).getLength());
    }
}
