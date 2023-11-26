import com.epam.training.ticketservice.commands.MovieCommands;
import com.epam.training.ticketservice.dto.MovieDto;
import com.epam.training.ticketservice.services.AccountService;
import com.epam.training.ticketservice.services.MovieService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MovieCommandsTest {
    @Mock
    private AccountService accountService;

    @Mock
    private MovieService movieService;

    @InjectMocks
    private MovieCommands movieCommands;

    @Test
    public void testListMovies_NoMovies() {
        when(movieService.getMovieList()).thenReturn(new ArrayList<>());

        String result = movieCommands.listMovies();

        assertEquals("There are no movies at the moment", result);
    }

    @Test//
    public void testListMovies_WithMovies() {
        List<MovieDto> movieList = new ArrayList<>();
        movieList.add(new MovieDto("Movie 1", "Action", 120));
        movieList.add(new MovieDto("Movie 2", "Comedy", 90));

        when(movieService.getMovieList()).thenReturn(movieList);

        String result = movieCommands.listMovies();

        StringBuilder expected = new StringBuilder();
        for (var movie : movieList) {
            expected.append(movie).append("\n");
        }
        expected.delete(expected.length() - 1, expected.length());

        assertEquals(expected.toString(), result);
    }
}
