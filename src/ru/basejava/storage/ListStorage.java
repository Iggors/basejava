package ru.basejava.storage;

import ru.basejava.model.Resume;

import java.util.ArrayList;
import java.util.List;

public class ListStorage extends AbstractStorage {
    private final List<Resume> listStorage = new ArrayList<>();

    @Override
    public int size() {
        return listStorage.size();
    }

    @Override
    public void clear() {
        listStorage.clear();
    }

    @Override
    protected Integer findSearchKey(String uuid) {
        for (int i = 0; i < listStorage.size(); i++) {
            if (uuid.equals(listStorage.get(i).getUuid())) {
                return i;
            }
        }
        return -1;
    }

    @Override
    protected boolean isExist(Object index) {
        return (int) index >= 0;
    }

    @Override
    protected Resume getResume(Object index) {
        return listStorage.get((int) index);
    }

    @Override
    protected void updateResume(Resume r, Object index) {
        listStorage.set((int) index, r);
    }

    @Override
    protected void saveResume(Resume r, Object index) {
        listStorage.add(r);
    }

    @Override
    protected void eraseResume(Object index) {
        listStorage.remove((int) index);
    }

    @Override
    protected List<Resume> copyAllResumes() {
        return new ArrayList<>(listStorage);
    }
}