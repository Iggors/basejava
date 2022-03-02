package ru.basejava.storage;

import ru.basejava.model.Resume;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage extends AbstractArrayStorage {

     protected void addElement(Resume r, int index) {
         storage[size] = r;
    }

    @Override
    protected void deleteElement(int index) {
         System.arraycopy(storage, index + 1, storage, index, size - 1 - index);
    }

    protected int getIndex(String id) {
        for (int i = 0; i < size; i++) {
            if (id.equals(storage[i].getUuid())) {
                return i;
            }
        }
        return -1;
    }
}