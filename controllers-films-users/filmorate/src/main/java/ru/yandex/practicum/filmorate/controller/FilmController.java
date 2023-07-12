package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @Autowired(required = false)
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public Collection<Film> findAll() {
        log.info("Получен запрос GET к эндопоинту: /films");
        return filmService.getFilms();
    }

    @GetMapping("/{id}")
    public Film findFilm(@PathVariable String id) {
        log.info("Получен запрос GET к эндопоинту: /films/{}", id);
        return filmService.getFilm(id);
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        log.info("Получен запрос POST. Данные тела запроса: {}", film);
        Film validFilm = filmService.add(film);
        log.info("Создан объект {} c тдентификатором {}", Film.class.getSimpleName(), validFilm.getId());
        return validFilm;
    }

    @PutMapping
    public Film put(@RequestBody Film film) {
        log.info("Получен запрос PUT. Данные тела запроса: {}",film);
        Film validFilm = filmService.update(film);
        log.info("Обновлен объект {} с идентификатором {}", Film.class.getSimpleName(), validFilm.getId());
        return validFilm;
    }


}
