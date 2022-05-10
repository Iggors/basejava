package ru.basejava.storage;

import ru.basejava.model.Resume;

import java.util.*;

public class MapUuidStorage extends AbstractStorage<String> {
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
    protected Resume getResume(String searchKey) {
        return mapUuidStorage.get(searchKey);
    }

    @Override
    protected void updateResume(Resume resume, String searchKey) {
        mapUuidStorage.replace(searchKey, resume);
    }

    @Override
    protected void saveResume(Resume resume, String searchKey) {
        mapUuidStorage.put(searchKey, resume);
    }

    @Override
    protected void eraseResume(String searchKey) {
        mapUuidStorage.remove(searchKey);
    }

    @Override
    protected boolean isExist(String searchKey) {
        return mapUuidStorage.containsKey(searchKey);
    }

    @Override
    protected List<Resume> copyAllResumes() {
        return new ArrayList<>(mapUuidStorage.values());
    }
}
