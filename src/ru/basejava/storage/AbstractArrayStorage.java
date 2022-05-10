package ru.basejava.storage;

import ru.basejava.exception.StorageException;
import ru.basejava.model.Resume;

import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.copyOfRange;

/**
 * Array based storage for Resumes
 */
public abstract class AbstractArrayStorage extends AbstractStorage<Integer> {
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

    protected Resume getResume(Integer index) {
        return storage[index];
    }

    protected void updateResume(Resume r, Integer index) {
        storage[index] = r;
    }

    protected void saveResume(Resume r, Integer index) {
        if (size >= STORAGE_LIMIT) {
            throw new StorageException("The storage overflow.", r.getUuid());
        }
        addResume(r, index);
        size++;
    }

    protected void eraseResume(Integer index) {
        deleteResume(index);
        storage[size - 1] = null;
        size--;
    }

    @Override
    protected boolean isExist(Integer index) {
        return index >= 0;
    }

    @Override
    protected List<Resume> copyAllResumes() {
        return Arrays.asList(copyOfRange(storage, 0, size));
    }
}