package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
@AllArgsConstructor
@Slf4j
public class FilmController {
    private final FilmService filmService;

    @GetMapping
    public List<Film> getFilms() {
        List<Film> filmsList = filmService.getAllFilms();
        log.debug("Количество фильмов: {}", filmsList.size());
        return filmsList;
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        log.debug("Создание фильма: {}", film);
        return filmService.createFilm(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        log.debug("Обновление фильма: {}", film);
        return filmService.updateFilm(film);
    }
}