package com.basejava.webapp.storage;

import com.basejava.webapp.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    private Resume[] storage = new Resume[10000];

    // size - The counter of not null elements in the storage[]
    private static int size = 0;

    public void clear() {
        if (size != 0) {
            Arrays.fill(storage, 0, size, null);
            size = 0;
            System.out.println("The storage was successfully cleared.");
        } else {
            System.out.println("The storage is empty.");
        }
    }

    public void update(Resume r) {
        int index = findIndex(r.getUuid());
        String uuid = r.getUuid();

        if (index != -1) {
            storage[index] = r;
            System.out.println("The resume with unique identifier " + uuid + " was successfully updated.");
        } else {
            System.out.println("The resume with unique identifier " + uuid + " not found.");
        }
    }

    public void save(Resume r) {
        int index = findIndex(r.getUuid());
        String uuid = r.getUuid();

        if (size >= storage.length) {
            System.out.println("The storage is full.");
        } else if (index != -1) {
            System.out.println("The resume with unique identifier " + uuid + " already exists.");
        } else {
            storage[size] = r;
            size++;
            System.out.println("The resume with unique identifier " + uuid + " was successfully saved.");
        }
    }

    public Resume get(String uuid) {
        int index = findIndex(uuid);

        if (index != -1) {
            return storage[index];
        }
        System.out.println("The resume with unique identifier " + uuid + " not found.");
        return null;
    }

    public void delete(String uuid) {
        int index = findIndex(uuid);

        if (index != -1) {
            if (size - 1 - index >= 0) {
                System.arraycopy(storage, index + 1, storage, index, size - 1 - index);
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
            Resume[] tempStorage = new Resume[size];
            System.arraycopy(storage, 0, tempStorage, 0, size);
            return tempStorage;
        } else return new Resume[0];
    }

    public int size() {
        return size;
    }

    private int findIndex(String id) {
        for (int i = 0; i < size; i++) {
            if (storage[i].toString().equals(id)) {
                return i;
            }
        }
        return -1;
    }
}