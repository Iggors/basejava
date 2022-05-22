package ru.basejava.storage;

import ru.basejava.exception.StorageException;
import ru.basejava.model.Resume;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class AbstractFileStorage extends AbstractStorage<File> {
    private final File directory;

    protected abstract void doWrite(Resume r, File file) throws IOException;

    protected abstract Resume doRead(File file) throws IOException;

    protected AbstractFileStorage(File directory) {
        Objects.requireNonNull(directory, "directory must not be null");
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + "is not directory");
        }
        if (!directory.canRead() && !directory.canWrite()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + "is not readable/writable");
        }
        this.directory = directory;
    }

    @Override
    protected File findSearchKey(String uuid) {
        return new File(directory, uuid);
    }

    @Override
    protected Resume getResume(File file) {
        try {
            return doRead(file);
        } catch (IOException e) {
            throw new StorageException("File read error", file.getName(), e);
        }
    }

    @Override
    protected void updateResume(Resume r, File file) {
        try {
            doWrite(r, file);
        } catch (IOException e) {
            throw new StorageException("File write error", file.getName(), e);
        }
    }

    @Override
    protected void saveResume(Resume r, File file) {
        try {
            file.createNewFile();
            doWrite(r, file);
        } catch (IOException e) {
            throw new StorageException("File create error ", file.getName(), e);
        }
    }

    @Override
    protected void eraseResume(File file) {
        if (!file.delete()) {
            throw new StorageException("File delete error", file.getName());
        }
    }

    @Override
    protected boolean isExist(File file) {
        return file.exists();
    }

    @Override
    protected List<Resume> copyAllResumes() {
        List<Resume> fileList = new ArrayList<>(getFilesList().length);
        for (File file : getFilesList()) {
            fileList.add(getResume(file));
        }
        return fileList;
    }

    @Override
    public int size() {
        String[] fileList = directory.list();
        if (fileList == null) {
            throw new StorageException("Directory read error", directory.getName());
        }
        return fileList.length;
    }

    @Override
    public void clear() {
        for (File file : getFilesList()) {
            eraseResume(file);
        }
    }

    private File[] getFilesList() {
        File[] fileList = directory.listFiles();
        if (fileList == null) {
            throw new StorageException("Directory read error", directory.getName());
        }
        return fileList;
    }
}
