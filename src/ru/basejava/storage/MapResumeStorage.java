package ru.basejava.storage;

import ru.basejava.model.Resume;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapResumeStorage extends AbstractStorage<Resume> {
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
    protected Resume getResume(Resume searchKey) {
        return searchKey;
    }

    @Override
    protected void updateResume(Resume resume, Resume searchKey) {
        mapResumeStorage.replace(resume.getUuid(), resume);
    }

    @Override
    protected void saveResume(Resume resume, Resume searchKey) {
        mapResumeStorage.put(resume.getUuid(), resume);
    }

    @Override
    protected void eraseResume(Resume searchKey) {
        mapResumeStorage.remove(searchKey.getUuid());
    }

    @Override
    protected boolean isExist(Resume searchKey) {
        return searchKey != null;
    }

    @Override
    protected List<Resume> copyAllResumes() {
        return new ArrayList<>(mapResumeStorage.values());
    }
}
