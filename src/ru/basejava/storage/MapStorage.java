package ru.basejava.storage;

import ru.basejava.model.Resume;

import java.util.*;

public class MapStorage extends AbstractStorage {
    private final Map<String, Resume> storage = new HashMap<>();

    @Override
    public int size() {
        return storage.size();
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public Resume[] getAll() {
        List<Resume> tempStorage = new ArrayList<>(storage.values());
        Collections.sort(tempStorage);
        return tempStorage.toArray(new Resume[0]);

    }

    @Override
    protected String getIndex(String uuid) {
        return uuid;
    }

    @Override
    protected boolean doesResumeExist(Object index) {
        return storage.containsKey((String) index);
    }

    @Override
    protected Resume getResume(Object index) {
        return storage.get((String) index);
    }

    @Override
    protected void updateResume(Resume r, Object index) {
        storage.replace(r.getUuid(), r, r);
    }

    @Override
    protected void saveResume(Resume r, Object index) {
        storage.put(r.getUuid(), r);
    }

    @Override
    protected void eraseResume(Object index) {
        storage.remove((String) index);
    }
}
