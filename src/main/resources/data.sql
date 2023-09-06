DELETE FROM likes;
DELETE FROM genreline;
DELETE FROM friendship;
DELETE FROM users;
DELETE FROM film;

ALTER TABLE users ALTER COLUMN userid RESTART WITH 1;
ALTER TABLE film ALTER COLUMN filmid RESTART WITH 1;
ALTER TABLE friendship ALTER COLUMN friendshipid RESTART WITH 1;
ALTER TABLE likes ALTER COLUMN likeid RESTART WITH 1;

MERGE INTO ratingmpa KEY(ratingid)
VALUES(1, 'G', 'Нет возрастных ограничений'),
      (2, 'PG', 'Рекомендуется присутствие родителей'),
      (3, 'PG-13', 'Детям до 13 лет просмотр не желателен'),
      (4, 'R', 'Лицам до 17 лет обязательно присутствие взрослого'),
      (5, 'NC-17', 'Лицам до 18 лет просмотр запрещен');

MERGE INTO genre KEY(genreid)
VALUES(1, 'Комедия'),
      (2, 'Драма'),
      (3, 'Мультфильм'),
      (4, 'Триллер'),
      (5, 'Документальный'),
      (6, 'Боевик');