package ru.basejava.storage;

import ru.basejava.model.Resume;

import java.util.List;

/**
 * Array based storage for Resumes
 */
public interface Storage {
    int size();

    void clear();

    List<Resume> getAllSorted();

    void save(Resume r);

    Resume get(String uuid);

    void update(Resume r);

    void delete(String uuid);
}