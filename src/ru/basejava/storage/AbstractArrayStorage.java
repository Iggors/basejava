package ru.basejava.storage;

import ru.basejava.exception.ExistStorageException;
import ru.basejava.exception.NotExistStorageException;
import ru.basejava.exception.StorageException;
import ru.basejava.model.Resume;

import java.util.Arrays;

import static java.util.Arrays.copyOfRange;

/**
 * Array based storage for Resumes
 */
public abstract class AbstractArrayStorage implements Storage {
    private final static int STORAGE_LIMIT = 10000;

    protected Resume[] storage = new Resume[STORAGE_LIMIT];

    // size - The counter of not null elements in the storage[]
    protected static int size = 0;

    protected abstract int getIndex(String uuid);

    protected abstract void addResume(Resume r, int index);

    protected abstract void deleteResume(int index);

    public int size() {
        return size;
    }

    public void clear() {
        if (size != 0) {
            Arrays.fill(storage, 0, size, null);
            size = 0;
            System.out.println("The storage was successfully cleared.");
        } else {
            System.out.println("The storage is empty.");
        }
    }

    public Resume get(String uuid) {
        int index = getIndex(uuid);

        if (index >= 0) {
            return storage[index];
        }
        throw new NotExistStorageException(uuid);
    }

    public void update(Resume r) {
        int index = getIndex(r.getUuid());
        String uuid = r.getUuid();

        if (index >= 0) {
            storage[index] = r;
            System.out.println("The resume with unique identifier " + uuid + " was successfully updated.");
        } else {
            throw new NotExistStorageException(uuid);
        }
    }

    public void save(Resume r) {
        int index = getIndex(r.getUuid());
        String uuid = r.getUuid();

        if (index >= 0) {
            throw new ExistStorageException(uuid);
        } else if (size >= STORAGE_LIMIT) {
            throw new StorageException("The storage overflow.", uuid);
        } else {
            addResume(r, index);
            size++;
            System.out.println("The resume with unique identifier " + uuid + " was successfully saved.");
        }
    }

    public void delete(String uuid) {
        int index = getIndex(uuid);

        if (index < 0) {
            throw new NotExistStorageException(uuid);
        } else {
            deleteResume(index);
            storage[size - 1] = null;
            size--;
            System.out.println("The resume with unique identifier " + uuid + " was successfully deleted.");
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    public Resume[] getAll() {
        return copyOfRange(storage, 0, size);
    }
}