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
    protected String findSearchKey(String uuid) {
        return uuid;
    }

    @Override
    protected boolean isExist(Object uuid) {
        return storage.containsKey((String) uuid);
    }

    @Override
    protected Resume getResume(Object uuid) {
        return storage.get((String) uuid);
    }

    @Override
    protected void updateResume(Resume r, Object uuid) {
        storage.replace(r.getUuid(), r, r);
    }

    @Override
    protected void saveResume(Resume r, Object uuid) {
        storage.put(r.getUuid(), r);
    }

    @Override
    protected void eraseResume(Object uuid) {
        storage.remove((String) uuid);
    }
}
