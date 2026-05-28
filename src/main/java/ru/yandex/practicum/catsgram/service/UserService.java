package ru.yandex.practicum.catsgram.service;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.DuplicatedDataException;
import ru.yandex.practicum.catsgram.model.User;
import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class UserService {
    private final Map<Long, User> users = new HashMap<>();

    public Collection<User> getUsers() {
        return users.values();
    }

    public Optional<User> getUserById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    public User createUser(User newUser) {
        if (newUser.getUsername() == null || newUser.getUsername().isBlank()) {
            throw new ConditionsNotMetException("Имя пользователя не может быть пустым");
        }
        if (newUser.getEmail() == null || newUser.getEmail().isBlank()) {
            throw new ConditionsNotMetException("Имейл должен быть указан");
        }
        if (newUser.getPassword() == null || newUser.getPassword().isBlank()) {
            throw new ConditionsNotMetException("Пароль пользователя не может быть пустым");
        }
        for (User user : users.values()) {
            if (user.getEmail().equals(newUser.getEmail())) {
                throw new DuplicatedDataException("Этот имейл уже используется");
            }
        }
        newUser.setId(getNextId());
        newUser.setRegistrationDate(Instant.now());
        users.put(newUser.getId(), newUser);
        return newUser;
    }

    public User updateUser(User updateUser) {
        if (updateUser.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        if (!users.containsKey(updateUser.getId())) {
            throw new ConditionsNotMetException("Пользователь с id " + updateUser.getId() + " не найден");
        }
        User user = users.get(updateUser.getId());
        if (updateUser.getUsername() != null && !updateUser.getUsername().isBlank()) {
            user.setUsername(updateUser.getUsername());
        }
        if (updateUser.getEmail() != null && !updateUser.getEmail().isBlank()) {
            user.setEmail(updateUser.getEmail());
        }
        if (updateUser.getPassword() != null && !updateUser.getPassword().isBlank()) {
            user.setPassword(updateUser.getPassword());
        }
        return user;
    }

    public Optional<User> findUserById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}

