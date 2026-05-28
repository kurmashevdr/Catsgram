package ru.yandex.practicum.catsgram.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.Post;
import java.time.Instant;
import java.util.*;

// Указываем, что класс PostService - является бином и его
// нужно добавить в контекст приложения
@Service
public class PostService {
    private final Map<Long, Post> posts = new HashMap<>();
    private final UserService userService;
    private Long currentMaxId;
    private static final Long TEN_NEW_POSTS = 10L;

    @Autowired
    public PostService(UserService userService) {
        this.userService = userService;
    }

    public Collection<Post> findAll(Integer from, Integer size, SortOrder sort) {
        Map<Long, Post> posts = new LinkedHashMap<>();
        if (sort == null) {
            throw  new ConditionsNotMetException("Sort should be asc or desc");
        }
        if (size == null || size <= 0) {
            throw new ConditionsNotMetException("Size should be greater than 0");
        }
        if (from == null || from < 0) {
            throw new ConditionsNotMetException("From should be greater than or equal to 0");
        }
        Map<Long, Post> sortedPosts = sortPosts(sort);
        size = sortedPosts.size() <= size ? sortedPosts.size() : size;
        int count = 0;
        int index = 0;
        for (Long id : sortedPosts.keySet()) {
            if (index++ < from) {
                continue;
            }
            posts.put(id, sortedPosts.get(id));
            count++;
            if (count == size) {
                break;
            }
        }
        return posts.values();
    }

    public Optional<Post> findPostsById(Long id) {
        return Optional.ofNullable(posts.get(id));
    }

    public Post create(Post post) {
        if (post.getDescription() == null || post.getDescription().isBlank()) {
            throw new ConditionsNotMetException("Описание не может быть пустым");
        }
        if (userService.findUserById(post.getAuthorId()).isEmpty()) {
            throw new NotFoundException("Пользователь с id = " + post.getAuthorId() + " не найден");
        }
        post.setId(getNextId());
        post.setPostDate(Instant.now());
        posts.put(post.getId(), post);
        return post;
    }

    public Post update(Post newPost) {
        if (newPost.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        if (posts.containsKey(newPost.getId())) {
            Post oldPost = posts.get(newPost.getId());
            if (newPost.getDescription() == null || newPost.getDescription().isBlank()) {
                throw new ConditionsNotMetException("Описание не может быть пустым");
            }
            oldPost.setDescription(newPost.getDescription());
            return oldPost;
        }
        throw new NotFoundException("Пост с id = " + newPost.getId() + " не найден");
    }

    public Map<Long, Post> sortPosts(SortOrder sort) {
        if (SortOrder.ASCENDING.equals(sort)) {
            return new TreeMap<>(posts);
        } else {
            Map<Long, Post> reversePosts = new TreeMap<>(Collections.reverseOrder());
            reversePosts.putAll(posts);
            return reversePosts;
        }
    }

    private long getNextId() {
        if (currentMaxId == null) {
            currentMaxId = posts.keySet()
                    .stream()
                    .mapToLong(id -> id)
                    .max()
                    .orElse(0);
        }
        return ++currentMaxId;
    }
}