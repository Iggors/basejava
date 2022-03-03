package ru.basejava.storage;

import ru.basejava.model.Resume;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage {

    @Override
    protected void addElement(Resume r, int index) {
        int addIdx = -index - 1;
        System.arraycopy(storage, addIdx, storage, addIdx + 1, size - addIdx);
        storage[addIdx] = r;
    }

    @Override
    protected void deleteElement(int index) {
        System.arraycopy(storage, index + 1, storage, index, size - index - 1);
    }

    @Override
    protected int getIndex(String uuid) {
        Resume searchKey = new Resume();
        searchKey.setUuid(uuid);
        return Arrays.binarySearch(storage, 0, size, searchKey);
    }
}
