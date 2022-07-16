package ru.basejava.storage;

import ru.basejava.exception.NotExistStorageException;
import ru.basejava.model.ContactType;
import ru.basejava.model.Resume;
import ru.basejava.util.SqlHelper;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SqlStorage implements Storage {
    private final SqlHelper sqlHelper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        sqlHelper = new SqlHelper(() -> DriverManager.getConnection(dbUrl, dbUser, dbPassword));
    }

    @Override
    public int size() {
        return sqlHelper.execute("SELECT COUNT(*) FROM resume", ps -> {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        });
    }

    @Override
    public void clear() {
        sqlHelper.execute("DELETE FROM resume", PreparedStatement::execute);
    }

    @Override
    public List<Resume> getAllSorted() {
        List<Resume> resumes = new ArrayList<>();
        sqlHelper.transactionalExecute(connection -> {
            try(PreparedStatement ps = connection.prepareStatement("SELECT * FROM resume ORDER BY full_name, uuid")) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    resumes.add(new Resume(rs.getString(1).trim(), rs.getString(2)));
                }
            }
            try(PreparedStatement ps = connection.prepareStatement("SELECT * FROM contact WHERE resume_uuid = ?")) {
                for (Resume resume : resumes) {
                    ps.setString(1, resume.getUuid());
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        resume.addContact(ContactType.valueOf(rs.getString("type")), rs.getString("value"));
                    }
                }
            }
            return null;
        });
        return resumes;
//        return sqlHelper.execute("SELECT * FROM resume ORDER BY full_name, uuid", ps -> {
//            ResultSet rs = ps.executeQuery();
//            List<Resume> rList = new ArrayList<>();
//            while (rs.next()) {
//                rList.add(new Resume(rs.getString("uuid"), rs.getString("full_name")));
//            }
//            return rList;
//        });
    }

    @Override
    public void save(Resume r) {
        sqlHelper.transactionalExecute(conn -> {
                    try (PreparedStatement ps = conn.prepareStatement("INSERT INTO resume (uuid, full_name) VALUES (?,?)")) {
                        ps.setString(1, r.getUuid());
                        ps.setString(2, r.getFullName());
                        ps.execute();
                    }
                    try (PreparedStatement ps = conn.prepareStatement("INSERT INTO contact (resume_uuid, type, value) VALUES (?,?,?)")) {
                        insertContacts(r, ps);
                    }
                    return null;
                }
        );
//        sqlHelper.execute("INSERT INTO resume(uuid, full_name) VALUES (?,?)", ps -> {
//            ps.setString(1, r.getUuid());
//            ps.setString(2, r.getFullName());
//            ps.execute();
//            return null;
//        });
//        for (Map.Entry<ContactType, String> e : r.getContacts().entrySet()) {
//            sqlHelper.execute("INSERT INTO contact(resume_uuid, type, value) VALUES (?,?,?)", ps -> {
//                ps.setString(1, r.getUuid());
//                ps.setString(2, e.getKey().name());
//                ps.setString(2, e.getValue());
//                ps.execute();
//                return null;
//            });
//        }
    }

    @Override
    public Resume get(String uuid) {
        return sqlHelper.execute("" +
                        "SELECT * FROM resume r " +
                        "LEFT JOIN contact c " +
                        "ON r.uuid = c.resume_uuid " +
                        "WHERE r.uuid =?",
                ps -> {
                    ps.setString(1, uuid);
                    ResultSet rs = ps.executeQuery();
                    if (!rs.next()) {
                        throw new NotExistStorageException(uuid);
                    }
                    Resume r = new Resume(uuid, rs.getString("full_name"));
                    do {
                        String value = rs.getString("value");
                        ContactType type = ContactType.valueOf(rs.getString("type"));
                        r.addContact(type, value);
                    } while (rs.next());
                    return r;
                });
    }

    @Override
    public void update(Resume r) {
        sqlHelper.transactionalExecute(conn -> {
                    try (PreparedStatement ps = conn.prepareStatement("UPDATE resume SET full_name = ? WHERE uuid = ?")) {
                        ps.setString(2, r.getUuid());
                        ps.setString(1, r.getFullName());
                        ps.execute();
                        if (ps.executeUpdate() == 0) {
                            throw new NotExistStorageException("Resume " + r.getUuid() + " not exist");
                        }
                    }
                    try (PreparedStatement ps = conn.prepareStatement("UPDATE contact SET type = ?, value = ? WHERE resume_uuid = ?")) {
                        insertContacts(r, ps);
                    }
                    return null;
                }
        );
//        sqlHelper.execute("UPDATE resume SET full_name =? WHERE uuid =?", ps -> {
//            ps.setString(1, r.getFullName());
//            ps.setString(2, r.getUuid());
//            if (ps.executeUpdate() == 0) {
//                throw new NotExistStorageException(r.getUuid());
//            }
//            return null;
//        });
    }

    @Override
    public void delete(String uuid) {
        sqlHelper.execute("DELETE FROM resume WHERE uuid =?", ps -> {
            ps.setString(1, uuid);
            if (ps.executeUpdate() == 0) {
                throw new NotExistStorageException(uuid);
            }
            return null;
        });
    }

    public void insertContacts(Resume r, PreparedStatement ps) throws SQLException {
        for (Map.Entry<ContactType, String> e : r.getContacts().entrySet()) {
            ps.setString(1, r.getUuid());
            ps.setString(2, e.getKey().name());
            ps.setString(3, e.getValue());
            ps.addBatch();
        }
        ps.executeBatch();
    }
}
