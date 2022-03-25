package ru.basejava.storage;

import ru.basejava.exception.ExistStorageException;
import ru.basejava.exception.NotExistStorageException;
import ru.basejava.model.Resume;

public abstract class AbstractStorage implements Storage {

    protected abstract int getIndex(String uuid);

    protected abstract Resume getResume(int index);

    protected abstract void updateResume(Resume r, int index);

    protected abstract void saveResume(Resume r, int index);

    protected abstract void eraseResume(int index);

    private int indexWhenResumeExists(String uuid) {
        int index = getIndex(uuid);

        if (index >= 0) {
            throw new ExistStorageException(uuid);
        }
        return index;
    }

    private int indexWhenResumeNotExists(String uuid) {
        int index = getIndex(uuid);

        if (index < 0) {
            throw new NotExistStorageException(uuid);
        }
        return index;
    }

    public void save(Resume r) {
        int index = indexWhenResumeExists(r.getUuid());
        saveResume(r, index);
    }

    public Resume get(String uuid) {
        int index = indexWhenResumeNotExists(uuid);
        return getResume(index);
    }

    public void update(Resume r) {
        int index = indexWhenResumeNotExists(r.getUuid());
        updateResume(r, index);
    }

    public void delete(String uuid) {
        int index = indexWhenResumeNotExists(uuid);
        eraseResume(index);
    }
}