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
        Arrays.fill(storage, null);
        size = 0;
    }

    public void update(Resume r) {
        int i;
        for (i = 0; i < size; i++) {
            //if (storage[i].getUuid() == r.getUuid()) {
            if (storage[i].toString().equals(r.toString())) {
                System.out.println("The resume with unique identifier " + r.getUuid() + " was succesfully updated.");
                break;
            }
        }
        if (i == size) {
            System.out.println("The resume with unique identifier " + r.getUuid() + " was not found.");
        }
    }

    public void save(Resume r) {
        int i;
        for (i = 0; i < size; i++) {
            if (storage[i].toString().equals(r.toString())) {
                System.out.println("The resume with unique identifier " + r.getUuid() + " already exists.");
                break;
            }
        }
        if (i == size) {
            if (size < storage.length) {
                storage[size] = r;
                size++;
                System.out.println("The resume with unique identifier " + r.getUuid() + " was succesfully saved.");
            } else {
                System.out.println("The storage is full.");
            }
        }
    }

    public Resume get(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].toString().equals(uuid)) {
                return storage[i];
            }
        }
        return null;
    }

    public void delete(String uuid) {
        int i;
        for (i = 0; i < size; i++) {
 //           if (uuid == storage[i].toString()) {
            if (storage[i].toString().equals(uuid)) {
                break;
            }
        }
        if (i == size) {
            System.out.println("The resume with unique identifier " + uuid + " doesn't exist.");
        } else {
            for (int k = i; k < size - 1; k++) {
                storage[k] = storage[k + 1];
            }
            size--;
            System.out.println("The resume with unique identifier " + uuid + " was succesfully deleted.");
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