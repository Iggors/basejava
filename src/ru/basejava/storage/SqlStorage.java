package ru.basejava.storage;

import ru.basejava.exception.NotExistStorageException;
import ru.basejava.exception.StorageException;
import ru.basejava.model.*;
import ru.basejava.util.SqlHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SqlStorage implements Storage {
    private final SqlHelper sqlHelper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
        sqlHelper = new SqlHelper(() -> DriverManager.getConnection(dbUrl, dbUser, dbPassword));
    }

    @Override
    public int size() {
        return sqlHelper.execute("SELECT COUNT(*) FROM resume", ps -> {
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        });
    }

    @Override
    public void clear() {
        sqlHelper.execute("DELETE FROM resume", PreparedStatement::execute);
    }

    @Override
    public List<Resume> getAllSorted() {
        return sqlHelper.transactionalExecute(conn -> {
            Map<String, Resume> resumes = new LinkedHashMap<>();

            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM resume ORDER BY full_name, uuid")) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String uuid = rs.getString("uuid");
                    resumes.put(uuid, new Resume(uuid, rs.getString("full_name")));
                }
            }

            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM contact")) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Resume r = resumes.get(rs.getString("resume_uuid"));
                    addContact(rs, r);
                }
            }

            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM section")) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Resume r = resumes.get(rs.getString("resume_uuid"));
                    addSection(rs, r);
                }
            }

            return new ArrayList<>(resumes.values());
        });
    }

    @Override
    public void save(Resume r) {
        sqlHelper.transactionalExecute(conn -> {
                    try (PreparedStatement ps = conn.prepareStatement("INSERT INTO resume (uuid, full_name) VALUES (?,?)")) {
                        ps.setString(1, r.getUuid());
                        ps.setString(2, r.getFullName());
                        ps.execute();
                    }
                    insertContacts(r, conn);
                    insertSections(r, conn);
                    return null;
                }
        );
    }

    @Override
    public Resume get(String uuid) {
        return sqlHelper.transactionalExecute(conn -> {
            Resume r;
            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM resume WHERE uuid =?")) {
                ps.setString(1, uuid);
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) {
                    throw new NotExistStorageException(uuid);
                }
                r = new Resume(uuid, rs.getString("full_name"));
            }

            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM contact WHERE resume_uuid =?")) {
                ps.setString(1, uuid);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    addContact(rs, r);
                }
            }

            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM section WHERE resume_uuid =?")) {
                ps.setString(1, uuid);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    addSection(rs, r);
                }
            }

            return r;
        });

//        Resume resume = sqlHelper.execute("SELECT * FROM resume r " +
//                        "LEFT JOIN contact c on r.uuid = c.resume_uuid " +
//                        "WHERE r.uuid =?",
//                ps -> {
//                    ps.setString(1, uuid);
//                    ResultSet rs = ps.executeQuery();
//                    if (!rs.next()) {
//                        throw new NotExistStorageException("Resume " + uuid + " is not exist");
//                    }
//                    Resume r = new Resume(uuid, rs.getString("full_name"));
//                    do {
//                        addContact(rs, r);
//                    }
//                    while (rs.next());
//                    return r;
//                });
//        sqlHelper.execute("SELECT * FROM resume r " +
//                        "LEFT JOIN section s on r.uuid = s.resume_uuid " +
//                        "WHERE r.uuid =?",
//                ps -> {
//                    ps.setString(1, uuid);
//                    ResultSet rs = ps.executeQuery();
//                    if (!rs.next()) {
//                        throw new NotExistStorageException("Resume " + uuid + " is not exist");
//                    }
//
//                    do {
//                        addSection(rs, resume);
//                    }
//                    while (rs.next());
//                    return resume;
//                });
//        return resume;
    }

    @Override
    public void update(Resume r) {
        sqlHelper.transactionalExecute(conn -> {
            PreparedStatement ps = conn.prepareStatement("UPDATE resume SET full_name = ? WHERE uuid = ?");
            ps.setString(1, r.getFullName());
            ps.setString(2, r.getUuid());
            if (ps.executeUpdate() != 1) {
                throw new NotExistStorageException("Resume " + r.getUuid() + " not exist");
            }
            deleteContacts(r, conn);
            insertContacts(r, conn);
            deleteSections(r, conn);
            insertSections(r, conn);
            return null;
        });
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

    private void insertContacts(Resume r, Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO contact (resume_uuid, type, value) VALUES (?,?,?)")) {
            for (Map.Entry<ContactType, String> e : r.getContacts().entrySet()) {
                ps.setString(1, r.getUuid());
                ps.setString(2, e.getKey().name());
                ps.setString(3, e.getValue());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void deleteContacts(Resume r, Connection conn) {
//        sqlHelper.execute("DELETE  FROM contact WHERE resume_uuid=?", ps -> {
//            ps.setString(1, r.getUuid());
//            ps.execute();
//            return null;
//        });
        try (PreparedStatement preparedStatement = conn.prepareStatement("DELETE FROM contact WHERE resume_uuid =?")) {
            preparedStatement.setString(1, r.getUuid());
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    private void addContact(ResultSet rs, Resume r) throws SQLException {
        String value = rs.getString("value");
        ContactType type = ContactType.valueOf(rs.getString("type"));
        if (value != null) {
            r.addContact(type, value);
        }
    }

    private void insertSections(Resume r, Connection conn) {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO section(resume_uuid, type, content) VALUES (?, ?, ?)")) {
            for (Map.Entry<SectionType, AbstractSection> e : r.getSection().entrySet()) {
                SectionType type = e.getKey();
                String stringSection = null;
                switch (type) {
                    case PERSONAL, OBJECTIVE -> stringSection = ((TextSection) e.getValue()).getDescription();
                    case ACHIEVEMENT, QUALIFICATIONS -> stringSection = ((ListSection) e.getValue()).getItems().stream()
                            .map(s -> s + System.lineSeparator())
                            .collect(Collectors.joining());
                }
                if (stringSection != null) {
                    ps.setString(1, r.getUuid());
                    ps.setString(2, e.getKey().name());
                    ps.setString(3, stringSection);
                    ps.addBatch();
                }
            }
            ps.executeBatch();
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    private void deleteSections(Resume r, Connection conn) {
        try (PreparedStatement preparedStatement = conn.prepareStatement("DELETE FROM section WHERE resume_uuid =?")) {
            preparedStatement.setString(1, r.getUuid());
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    private void addSection(ResultSet rs, Resume r) throws SQLException {
        String type = rs.getString("type");
        if (type == null) {
            return;
        }
        SectionType sectionType = SectionType.valueOf(type);
        String sectionString = rs.getString("content");
        switch (sectionType) {
            case PERSONAL, OBJECTIVE -> r.addSection(sectionType, new TextSection(sectionString));
            case ACHIEVEMENT, QUALIFICATIONS -> r.addSection(sectionType, new ListSection(sectionString.split(System.lineSeparator())));
        }
    }
}
