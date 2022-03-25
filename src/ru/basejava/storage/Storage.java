package ru.basejava.storage;

import ru.basejava.model.Resume;

/**
 * Array based storage for Resumes
 */
public interface Storage {
    int size();

    void clear();

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    Resume[] getAll();

    void save(Resume r);

    Resume get(String uuid);

    void update(Resume r);

    void delete(String uuid);
}