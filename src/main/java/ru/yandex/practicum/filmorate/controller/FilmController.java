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

    @Autowired
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

    @GetMapping({"/popular?count={count}", "/popular"})
    public Collection<Film> findMostPopular(@RequestParam(defaultValue = "10") Integer count) {
        log.info("Получен запрос GET к эндопоинту: /films/popular?count={}", count);
        return filmService.getMostPopularFilms(count);
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        log.info("Получен запрос POST. Данные тела запроса: {}", film);
        Film validFilm = filmService.add(film);
        log.info("Создан объект {} с идинтификатором {}", Film.class.getSimpleName(), validFilm.getId());
        return validFilm;
    }

    @PutMapping
    public Film put(@RequestBody Film film) {
        log.info("Получен запрос PUT. Данные тела запроса: {}", film);
        Film validFilm = filmService.update(film);
        log.info("Обновлен объект {} c идентификатором {}", Film.class.getSimpleName(), validFilm.getId());
        return validFilm;
    }

    @PutMapping("/{id}/like/{userId}")
    public void putLike(@PathVariable String id, @PathVariable String userId) {
        log.info("Получен запрос PUT к эндопоинту: /films/{}/like/{}", id, userId);
        filmService.addLike(id, userId);
        log.info("Обновлен объект {} с идентификатором {}, добавлен лайк от пользователя {}",
                Film.class.getSimpleName(), id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable String id, @PathVariable String userId) {
        log.info("Получен запрос DELETE к эндопоинту: films/{}/like/{}", id, userId);
        filmService.deleteLike(id, userId);
        log.info("Обновлен объект {} с идентификатором {}, удален лайк от пользователя {}",
                Film.class.getSimpleName(), id, userId);
    }
}