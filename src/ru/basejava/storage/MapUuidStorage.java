package ru.basejava.storage;

import ru.basejava.model.Resume;

import java.util.*;

public class MapUuidStorage extends AbstractStorage {
    private final Map<String, Resume> mapUuidStorage = new HashMap<>();

    @Override
    public int size() {
        return mapUuidStorage.size();
    }

    @Override
    public void clear() {
        mapUuidStorage.clear();
    }

    @Override
    protected String findSearchKey(String uuid) {
        return uuid;
    }

    @Override
    protected Resume getResume(Object searchKey) {
        return mapUuidStorage.get((String) searchKey);
    }

    @Override
    protected void updateResume(Resume resume, Object searchKey) {
        mapUuidStorage.replace((String) searchKey, resume);
    }

    @Override
    protected void saveResume(Resume resume, Object searchKey) {
        mapUuidStorage.put((String) searchKey, resume);
    }

    @Override
    protected void eraseResume(Object searchKey) {
        mapUuidStorage.remove((String) searchKey);
    }

    @Override
    protected boolean isExist(Object searchKey) {
        return mapUuidStorage.containsKey((String) searchKey);
    }

    @Override
    protected List<Resume> copyAllResumes() {
        return new ArrayList<>(mapUuidStorage.values());
    }
}
