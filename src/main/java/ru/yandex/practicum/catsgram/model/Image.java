package ru.yandex.practicum.catsgram.model;

import lombok.Data;

/*
Long id — уникальный идентификатор изображения,
long postId — уникальный идентификатор поста, к которому прикреплено изображение,
String originalFileName — имя файла, который содержит изображение,
String filePath — путь, по которому изображение было сохранено
*/
@Data
public class Image {
    private long id;
    private long postId;
    private String originalFileName;
    private String filePath;

    public Image(long id, long postId, String originalFileName, String filePath) {
        this.id = id;
        this.postId = postId;
        this.originalFileName = originalFileName;
        this.filePath = filePath;
    }
}
