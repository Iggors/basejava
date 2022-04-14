package ru.basejava.storage;

import ru.basejava.model.Resume;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapResumeStorage extends AbstractStorage {
    private final Map<String, Resume> mapResumeStorage = new HashMap<>();

    @Override
    public int size() {
        return mapResumeStorage.size();
    }

    @Override
    public void clear() {
        mapResumeStorage.clear();
    }

    @Override
    protected Resume findSearchKey(String searchKey) {
        return mapResumeStorage.get(searchKey);
    }

    @Override
    protected Resume getResume(Object searchKey) {
        return (Resume) searchKey;
    }

    @Override
    protected void updateResume(Resume resume, Object searchKey) {
        mapResumeStorage.replace(resume.getUuid(), resume);
    }

    @Override
    protected void saveResume(Resume resume, Object searchKey) {
        mapResumeStorage.put(resume.getUuid(), resume);
    }

    @Override
    protected void eraseResume(Object searchKey) {
        mapResumeStorage.remove(((Resume) searchKey).getUuid());
    }

    @Override
    protected boolean isExist(Object searchKey) {
        return searchKey != null;
    }

    @Override
    protected List<Resume> copyAllResumes() {
        return new ArrayList<>(mapResumeStorage.values());
    }
}
