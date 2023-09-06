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
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

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
    void shouldReturn200whenPostCorrectFilmData() throws Exception {
        FilmDto filmDto = new FilmDto();
        filmDto.setName("Correct Name");
        filmDto.setDescription("Correct description");
        filmDto.setReleaseDate(LocalDate.of(1995, 5, 26));
        filmDto.setDuration(100L);
        filmDto.setMpa(new Mpa(1, "mpa", "description"));
        Film film = new Film();
        film.setName("Correct Name");
        film.setDescription("Correct description");
        film.setReleaseDate(LocalDate.of(1995, 5, 26));
        film.setDuration(100L);

        Mockito.when(filmController.create(Mockito.any())).thenReturn(filmDto);

        mockMvc.perform(post("/films")
                        .content(objectMapper.writeValueAsString(filmDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(filmDto)));
    }
}
