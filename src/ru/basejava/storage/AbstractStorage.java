package ru.basejava.storage;

import ru.basejava.exception.ExistStorageException;
import ru.basejava.exception.NotExistStorageException;
import ru.basejava.model.Resume;

import java.util.Comparator;
import java.util.List;

public abstract class AbstractStorage<SK> implements Storage {

    protected abstract SK findSearchKey(String uuid);

    protected abstract Resume getResume(SK searchKey);

    protected abstract void updateResume(Resume r, SK searchKey);

    protected abstract void saveResume(Resume r, SK searchKey);

    protected abstract void eraseResume(SK searchKey);

    protected abstract boolean isExist(SK searchKey);

    protected abstract List<Resume> copyAllResumes();

    /**
     * @return List, contains only Resumes in storage (without null)
     */
    @Override
    public List<Resume> getAllSorted() {
        Comparator<Resume> resumeComparator = Comparator.comparing(Resume::getFullName).thenComparing(Resume::getUuid);
        List<Resume> tempStorage = copyAllResumes();
        tempStorage.sort(resumeComparator);
        return tempStorage;
    }

    public void save(Resume r) {
        SK searchKey = receiveSearchKeyIfNotExist(r.getUuid());
        saveResume(r, searchKey);
    }

    public Resume get(String uuid) {
        SK searchKey = receiveSearchKeyIfExist(uuid);
        return getResume(searchKey);
    }

    public void update(Resume r) {
        SK searchKey = receiveSearchKeyIfExist(r.getUuid());
        updateResume(r, searchKey);
    }

    public void delete(String uuid) {
        SK searchKey = receiveSearchKeyIfExist(uuid);
        eraseResume(searchKey);
    }

    private SK receiveSearchKeyIfNotExist(String uuid) {
        SK searchKey = findSearchKey(uuid);

        if (isExist(searchKey)) {
            throw new ExistStorageException(uuid);
        }
        return searchKey;
    }

    private SK receiveSearchKeyIfExist(String uuid) {
        SK searchKey = findSearchKey(uuid);

        if (!isExist(searchKey)) {
            throw new NotExistStorageException(uuid);
        }
        return searchKey;
    }
}