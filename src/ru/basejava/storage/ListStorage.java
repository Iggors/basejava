package ru.basejava.storage;

import ru.basejava.model.Resume;

import java.util.ArrayList;
import java.util.List;

public class ListStorage extends AbstractStorage<Integer> {
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
    protected boolean isExist(Integer index) {
        return index >= 0;
    }

    @Override
    protected Resume getResume(Integer index) {
        return listStorage.get(index);
    }

    @Override
    protected void updateResume(Resume r, Integer index) {
        listStorage.set(index, r);
    }

    @Override
    protected void saveResume(Resume r, Integer index) {
        listStorage.add(r);
    }

    @Override
    protected void eraseResume(Integer index) {
        listStorage.remove(index.intValue());
    }

    @Override
    protected List<Resume> copyAllResumes() {
        return new ArrayList<>(listStorage);
    }
}