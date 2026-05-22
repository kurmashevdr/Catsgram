package ru.yandex.practicum.catsgram.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.Instant;

/*
Long id — уникальный идентификатор сообщения,
long authorId — пользователь, который создал сообщение,
String description — текстовое описание сообщения,
Instant postDate — дата и время создания сообщения.
*/
@Data
@EqualsAndHashCode(of = "id")
public class Post {
    private Long id;
    private Long authorId;
    private String description;
    private Instant postDate;

    public Post(long id, long authorId, String description, Instant postDate) {
        this.id = id;
        this.authorId = authorId;
        this.description = description;
        this.postDate = postDate;
    }
}
