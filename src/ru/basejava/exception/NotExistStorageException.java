package ru.basejava.exception;

public class NotExistStorageException extends StorageException {
    public NotExistStorageException(String uuid) {
        super("The resume with unique identifier " + uuid + " doesn't exist.", uuid);
    }
}
