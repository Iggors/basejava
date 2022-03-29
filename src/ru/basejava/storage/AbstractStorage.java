package ru.basejava.storage;

import ru.basejava.exception.ExistStorageException;
import ru.basejava.exception.NotExistStorageException;
import ru.basejava.model.Resume;

public abstract class AbstractStorage implements Storage {

    protected abstract Object getIndex(String uuid);

    protected abstract Resume getResume(Object index);

    protected abstract void updateResume(Resume r, Object index);

    protected abstract void saveResume(Resume r, Object index);

    protected abstract void eraseResume(Object index);

    protected abstract boolean doesResumeExist(Object index);

    private Object receiveIndexIfExist(String uuid) {
        Object index = getIndex(uuid);

        if (doesResumeExist(index)) {
            throw new ExistStorageException(uuid);
        }
        return index;
    }

    private Object receiveIndexIfNotExist(String uuid) {
        Object index = getIndex(uuid);

        if (!doesResumeExist(index)) {
            throw new NotExistStorageException(uuid);
        }
        return index;
    }

    public void save(Resume r) {
        Object index = receiveIndexIfExist(r.getUuid());
        saveResume(r, index);
    }

    public Resume get(String uuid) {
        Object index = receiveIndexIfNotExist(uuid);
        return getResume(index);
    }

    public void update(Resume r) {
        Object index = receiveIndexIfNotExist(r.getUuid());
        updateResume(r, index);
    }

    public void delete(String uuid) {
        Object index = receiveIndexIfNotExist(uuid);
        eraseResume(index);
    }
}