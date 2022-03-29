package ru.basejava.storage;

import ru.basejava.exception.StorageException;
import ru.basejava.model.Resume;

import java.util.Arrays;

import static java.util.Arrays.copyOfRange;

/**
 * Array based storage for Resumes
 */
public abstract class AbstractArrayStorage extends AbstractStorage {
    protected final static int STORAGE_LIMIT = 10000;

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

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    public Resume[] getAll() {
        return copyOfRange(storage, 0, size);
    }

    protected Resume getResume(int index) {
        return storage[index];
    }

    protected void updateResume(Resume r, int index) {
        storage[index] = r;
//        System.out.println("The resume with unique identifier " + uuid + " was successfully updated.");
    }

    protected void saveResume(Resume r, int index) {
        if (size >= STORAGE_LIMIT) {
            throw new StorageException("The storage overflow.", r.getUuid());
        }
        addResume(r, index);
        size++;
//        System.out.println("The resume with unique identifier " + uuid + " was successfully saved.");
    }

    protected void eraseResume(int index) {
        deleteResume(index);
        storage[size - 1] = null;
        size--;
//        System.out.println("The resume with unique identifier " + uuid + " was successfully deleted.");
    }
}