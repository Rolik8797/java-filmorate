package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
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

    @GetMapping("/popular")
    public Collection<Film> findMostPopular(@RequestParam(defaultValue = "10") Integer count) {
        log.info("Получен запрос GET к эндопоинту: /films/popular?count={}", count);
        return filmService.getMostPopularFilms(count);
    }

    @PostMapping
    public FilmDto create(@Valid @RequestBody FilmDto filmDto) {
        log.info("Получен запрос POST. Данные тела запроса: {}", filmDto);
        Film addedFilm = filmService.add(filmDto);
        log.info("Создан объект {} с идентификатором {}", FilmDto.class.getSimpleName(), addedFilm.getId());
        return convertToDto(addedFilm);
    }

    @PutMapping
    public FilmDto put(@Valid @RequestBody FilmDto filmDto) {
        log.info("Получен запрос PUT. Данные тела запроса: {}", filmDto);
        Film updatedFilm = filmService.update(filmDto);
        log.info("Обновлен объект {} c идентификатором {}", FilmDto.class.getSimpleName(), updatedFilm.getId());
        return convertToDto(updatedFilm);
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

    private FilmDto convertToDto(Film film) {
        FilmDto filmDto = new FilmDto();
        filmDto.setId(film.getId());
        filmDto.setName(film.getName());
        filmDto.setDescription(film.getDescription());
        filmDto.setReleaseDate(film.getReleaseDate());
        filmDto.setDuration(film.getDuration());
        filmDto.setRate(film.getRate());
        filmDto.setMpa(film.getMpa());
        filmDto.setGenres(film.getGenres());
        filmDto.setLikes(film.getLikes()); // Подставьте соответствующее поле
        return filmDto;
    }
}