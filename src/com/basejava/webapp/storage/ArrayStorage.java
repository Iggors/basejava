package com.basejava.webapp.storage;

import com.basejava.webapp.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    private Resume[] storage = new Resume[10000];

    // size - The counter of not null elements in the storage[]
    private int size = 0;

    public void clear() {
        if (size != 0) {
            Arrays.fill(storage, 0, size, null);
            size = 0;
            System.out.println("The storage was successfully cleared.");
        } else {
            System.out.println("The storage is empty.");
        }
    }

    private int find(String id) {
        int i;

        for (i = 0; i < size; i++) {
            if (storage[i].toString().equals(id)) {
                return i;
            }
        }
        return -1;
    }

    public void update(Resume r) {
        int findIndex = find(r.getUuid());

        if (findIndex != -1) {
            System.out.println("The resume with unique identifier " + r.getUuid() + " was successfully updated.");
        } else {
            System.out.println("The resume with unique identifier " + r.getUuid() + " was not found.");
        }
    }

    public void save(Resume r) {
        int uuidIndex = find(r.getUuid());

        if (size >= storage.length) {
            System.out.println("The storage is full.");
        } else if (uuidIndex != -1) {
            System.out.println("The resume with unique identifier " + r.getUuid() + " already exists.");
        } else {
            storage[size] = r;
            size++;
            System.out.println("The resume with unique identifier " + r.getUuid() + " was successfully saved.");
        }
    }

    public Resume get(String uuid) {
        int uuidIndex = find(uuid);

        if (uuidIndex != -1) {
            return storage[uuidIndex];
        } else {
            return null;
        }
    }

    public void delete(String uuid) {
        int uuidIndex = find(uuid);

        if (uuidIndex != -1) {
            for (int k = uuidIndex; k < size - 1; k++) {
                storage[k] = storage[k + 1];
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
}