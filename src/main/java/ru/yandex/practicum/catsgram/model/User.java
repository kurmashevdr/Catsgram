package ru.yandex.practicum.catsgram.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.Instant;

/*
Long id — уникальный идентификатор пользователя,
String username — имя пользователя,
String email — электронная почта пользователя,
String password — пароль пользователя,
Instant registrationDate — дата и время регистрации.
*/
@Data
@EqualsAndHashCode (of = "email")
public class User {
    private Long id;
    private String username;
    private String email;
    private String password;
    private Instant registrationDate;

    public User(long id, String username, String email, String password, Instant registrationDate) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.registrationDate = registrationDate;
    }
}
