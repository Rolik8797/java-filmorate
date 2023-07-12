package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FilmControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private FilmController filmController;

    @Test
    void shouldReturn200whenGetFilms() throws Exception {
        Film film = new Film();
        film.setName("Correct Name");
        film.setDescription("Correct description");
        film.setReleaseDate(LocalDate.of(1995, 5, 26));
        film.setDuration(100L);
        Mockito.when(filmController.findAll()).thenReturn(Collections.singletonList(film));
        mockMvc.perform(get("/films"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(Collections.singletonList(film))));
    }
    @Test
    void shouldReturn200whenPostCorrectFilmData() throws Exception{
        Film film = new Film();
        film.setName("Correct Name");
        film.setDescription("Correct description");
        film.setReleaseDate(LocalDate.of(1995,5,26));
        film.setDuration(100L);
        Mockito.when(filmController.create(Mockito.any())).thenReturn(film);
        mockMvc.perform(post("/films")
                .content(objectMapper.writeValueAsString(film))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(film)));
    }
}
