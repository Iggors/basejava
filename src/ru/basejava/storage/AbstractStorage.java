package ru.basejava.storage;

import ru.basejava.exception.ExistStorageException;
import ru.basejava.exception.NotExistStorageException;
import ru.basejava.model.Resume;

import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

public abstract class AbstractStorage<SK> implements Storage {
    private static final Logger LOG = Logger.getLogger(AbstractStorage.class.getName());

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
        LOG.info("getAllSorted");
        Comparator<Resume> resumeComparator = Comparator.comparing(Resume::getFullName).thenComparing(Resume::getUuid);
        List<Resume> tempStorage = copyAllResumes();
        tempStorage.sort(resumeComparator);
        return tempStorage;
    }

    public void save(Resume r) {
        LOG.info("Save " + r);
        SK searchKey = receiveSearchKeyIfNotExist(r.getUuid());
        saveResume(r, searchKey);
    }

    public Resume get(String uuid) {
        LOG.info("Get " + uuid);
        SK searchKey = receiveSearchKeyIfExist(uuid);
        return getResume(searchKey);
    }

    public void update(Resume r) {
        LOG.info("Update " + r);
        SK searchKey = receiveSearchKeyIfExist(r.getUuid());
        updateResume(r, searchKey);
    }

    public void delete(String uuid) {
        LOG.info("Delete " + uuid);
        SK searchKey = receiveSearchKeyIfExist(uuid);
        eraseResume(searchKey);
    }

    private SK receiveSearchKeyIfNotExist(String uuid) {
        SK searchKey = findSearchKey(uuid);

        if (isExist(searchKey)) {
            LOG.warning("The resume with unique identifier " + uuid + " already exists.");
            throw new ExistStorageException(uuid);
        }
        return searchKey;
    }

    private SK receiveSearchKeyIfExist(String uuid) {
        SK searchKey = findSearchKey(uuid);

        if (!isExist(searchKey)) {
            LOG.warning("The resume with unique identifier " + uuid + " doesn't exist.");
            throw new NotExistStorageException(uuid);
        }
        return searchKey;
    }
}