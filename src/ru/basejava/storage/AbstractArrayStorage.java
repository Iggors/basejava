package ru.basejava.storage;

import ru.basejava.model.Resume;

import java.util.Arrays;

import static java.util.Arrays.copyOfRange;

/**
 * Array based storage for Resumes
 */
public abstract class AbstractArrayStorage implements Storage {
    protected static final int STORAGE_LIMIT = 10000;

    protected Resume[] storage = new Resume[STORAGE_LIMIT];

    // size - The counter of not null elements in the storage[]
    protected int size = 0;

    protected abstract int getIndex(String uuid);
    protected abstract void addElement(Resume r, int index);
    protected abstract void deleteElement(int index);

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

        if (index != -1) {
            return storage[index];
        }
        System.out.println("The resume with unique identifier " + uuid + " not found.");
        return null;
    }

    public void update(Resume r) {
        int index = getIndex(r.getUuid());
        String uuid = r.getUuid();

        if (index != -1) {
            storage[index] = r;
            System.out.println("The resume with unique identifier " + uuid + " was successfully updated.");
        } else {
            System.out.println("The resume with unique identifier " + uuid + " not found.");
        }
    }

    public void save(Resume r) {
        int index = getIndex(r.getUuid());
        String uuid = r.getUuid();

        if (index != -1) {
            System.out.println("The resume with unique identifier " + uuid + " already exists.");
        } else if (size >= STORAGE_LIMIT) {
            System.out.println("The storage overflow.");
        } else {
            addElement(r, index);
            size++;
            System.out.println("The resume with unique identifier " + uuid + " was successfully saved.");
        }
    }

    public void delete(String uuid) {
        int index = getIndex(uuid);

        if (index != -1) {
            if (size - 1 - index >= 0) {
                deleteElement(index);
            }
            size--;
            System.out.println("The resume with unique identifier " + uuid + " was successfully deleted.");
        } else {
            System.out.println("The resume with unique identifier " + uuid + " doesn't exist.");
        }

    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    public Resume[] getAll() {
        if (size > 0) {
            return copyOfRange(storage, 0, size);
        } else return new Resume[0];
    }
}