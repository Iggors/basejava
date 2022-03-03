package ru.basejava.storage;

import ru.basejava.model.Resume;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage extends AbstractArrayStorage {

    @Override
    protected void addElement(Resume r, int index) {
        storage[size] = r;
    }

    @Override
    protected void deleteElement(int index) {
        storage[index] = storage[size - 1];
    }

    @Override
    protected int getIndex(String id) {
        for (int i = 0; i < size; i++) {
            if (id.equals(storage[i].getUuid())) {
                return i;
            }
        }
        return -1;
    }
}