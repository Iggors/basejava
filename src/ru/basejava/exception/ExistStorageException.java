package ru.basejava.exception;

public class ExistStorageException extends StorageException{
    public ExistStorageException(String uuid) {
        super("The resume with unique identifier " + uuid + " already exists.", uuid);
    }
}
