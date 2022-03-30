package ru.basejava.storage;

import ru.basejava.exception.ExistStorageException;
import ru.basejava.exception.NotExistStorageException;
import ru.basejava.model.Resume;

public abstract class AbstractStorage implements Storage {

    protected abstract Object getIndex(String uuid);

    protected abstract Resume getResume(Object searchKey);

    protected abstract void updateResume(Resume r, Object searchKey);

    protected abstract void saveResume(Resume r, Object searchKey);

    protected abstract void eraseResume(Object searchKey);

    protected abstract boolean isExist(Object searchKey);

    public void save(Resume r) {
        Object searchKey = receiveIndexIfNotExist(r.getUuid());
        saveResume(r, searchKey);
    }

    public Resume get(String uuid) {
        Object searchKey = receiveIndexIfExist(uuid);
        return getResume(searchKey);
    }

    public void update(Resume r) {
        Object searchKey = receiveIndexIfExist(r.getUuid());
        updateResume(r, searchKey);
    }

    public void delete(String uuid) {
        Object searchKey = receiveIndexIfExist(uuid);
        eraseResume(searchKey);
    }

    private Object receiveIndexIfNotExist(String uuid) {
        Object searchKey = getIndex(uuid);

        if (isExist(searchKey)) {
            throw new ExistStorageException(uuid);
        }
        return searchKey;
    }

    private Object receiveIndexIfExist(String uuid) {
        Object searchKey = getIndex(uuid);

        if (!isExist(searchKey)) {
            throw new NotExistStorageException(uuid);
        }
        return searchKey;
    }
}