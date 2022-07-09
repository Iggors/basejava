//package ru.basejava.exception;
//
//import org.postgresql.util.PSQLException;
//
//import java.sql.SQLException;
//
//public class SqlException {
//    private SqlException() {
//    }
//
//    public static StorageException sqlException(SQLException e) {
//        if (e instanceof PSQLException) {
//            if (e.getSQLState().equals("23505")) {
//                return new ExistStorageException(null);
//            }
//        }
//        return new StorageException(e);
//    }
//}
