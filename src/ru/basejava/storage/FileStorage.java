package ru.basejava.storage;

import ru.basejava.exception.StorageException;
import ru.basejava.model.Resume;
import ru.basejava.storage.serializer.StreamSerializerStrategy;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileStorage extends AbstractStorage<File> {
    private final File directory;

    private final StreamSerializerStrategy streamSerializerStrategy;

    protected FileStorage(File directory, StreamSerializerStrategy streamSerializerStrategy) {
        this.streamSerializerStrategy = streamSerializerStrategy;

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
            return streamSerializerStrategy.doRead(new BufferedInputStream(new FileInputStream(file)));
        } catch (IOException e) {
            throw new StorageException("File read error", file.getName(), e);
        }
    }

    @Override
    protected void updateResume(Resume r, File file) {
        try {
            streamSerializerStrategy.doWrite(r, new BufferedOutputStream(new FileOutputStream(file)));
        } catch (IOException e) {
            throw new StorageException("File write error", r.getUuid(), e);
        }
    }

    @Override
    protected void saveResume(Resume r, File file) {
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new StorageException("Couldn't create file " + file.getAbsolutePath(), file.getName(), e);
        }
        updateResume(r, file);
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
        File[] fList = getFilesList();
        List<Resume> fileList = new ArrayList<>(fList.length);

        for (File file : fList) {
            fileList.add(getResume(file));
        }
        return fileList;
    }

    @Override
    public int size() {
        return getFilesList().length;
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
