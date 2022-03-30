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

    protected abstract Integer findSearchKey(String uuid);

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

    protected Resume getResume(Object index) {
        return storage[(int) index];
    }

    protected void updateResume(Resume r, Object index) {
        storage[(int) index] = r;
    }

    protected void saveResume(Resume r, Object index) {
        if (size >= STORAGE_LIMIT) {
            throw new StorageException("The storage overflow.", r.getUuid());
        }
        addResume(r, (int) index);
        size++;
    }

    protected void eraseResume(Object index) {
        deleteResume((int) index);
        storage[size - 1] = null;
        size--;
    }

    @Override
    protected boolean isExist(Object index) {
        return (int) index >= 0;
    }
}