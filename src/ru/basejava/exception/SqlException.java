package ru.basejava.exception;

import org.postgresql.util.PSQLException;

import java.sql.SQLException;

public class SqlException {
    private SqlException() {
    }

    public static StorageException sqlExeption(SQLException e) {
        if (e instanceof PSQLException) {
                return new ExistStorageException(null);
        }
        return new StorageException(e);
    }
}
