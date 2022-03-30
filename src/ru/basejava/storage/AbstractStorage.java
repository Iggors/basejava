package ru.basejava.storage;

import ru.basejava.exception.ExistStorageException;
import ru.basejava.exception.NotExistStorageException;
import ru.basejava.model.Resume;

public abstract class AbstractStorage implements Storage {

    protected abstract Object findSearchKey(String uuid);

    protected abstract Resume getResume(Object searchKey);

    protected abstract void updateResume(Resume r, Object searchKey);

    protected abstract void saveResume(Resume r, Object searchKey);

    protected abstract void eraseResume(Object searchKey);

    protected abstract boolean isExist(Object searchKey);

    public void save(Resume r) {
        Object searchKey = receiveSearchKeyIfNotExist(r.getUuid());
        saveResume(r, searchKey);
    }

    public Resume get(String uuid) {
        Object searchKey = receiveSearchKeyIfExist(uuid);
        return getResume(searchKey);
    }

    public void update(Resume r) {
        Object searchKey = receiveSearchKeyIfExist(r.getUuid());
        updateResume(r, searchKey);
    }

    public void delete(String uuid) {
        Object searchKey = receiveSearchKeyIfExist(uuid);
        eraseResume(searchKey);
    }

    private Object receiveSearchKeyIfNotExist(String uuid) {
        Object searchKey = findSearchKey(uuid);

        if (isExist(searchKey)) {
            throw new ExistStorageException(uuid);
        }
        return searchKey;
    }

    private Object receiveSearchKeyIfExist(String uuid) {
        Object searchKey = findSearchKey(uuid);

        if (!isExist(searchKey)) {
            throw new NotExistStorageException(uuid);
        }
        return searchKey;
    }
}