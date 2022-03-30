package ru.basejava.storage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.basejava.exception.ExistStorageException;
import ru.basejava.exception.NotExistStorageException;
import ru.basejava.model.Resume;

import static org.junit.jupiter.api.Assertions.*;

public abstract class AbstractStorageTest {
    protected Storage storage;

    protected static final String UUID_1 = "uuid1";
    protected static final String UUID_2 = "uuid2";
    protected static final String UUID_3 = "uuid3";
    protected static final String NEW_UUID = "new_uuid";
    protected static final String DUMMY_UUID = "dummy";

    private final Resume r1 = new Resume(UUID_1);
    private final Resume r2 = new Resume(UUID_2);
    private final Resume r3 = new Resume(UUID_3);
    private final Resume r4 = new Resume(NEW_UUID);

    public AbstractStorageTest(Storage storage) {
        this.storage = storage;
    }

    @BeforeEach
    void setUp() {
        System.out.println("Before each test. " + this);
        storage.save(r1);
        storage.save(r2);
        storage.save(r3);
    }

    @Test
    @DisplayName("Check the size() method.")
    void size() {
        System.out.println(this);
        assertEquals(3, storage.size(), "Test failed. Storage size = 3.");
    }

    @Test
    @DisplayName("Check the clear() method.")
    void clear() {
        System.out.println(this);
        storage.clear();
        assertEquals(0, storage.size(), "Test failed. Storage size must be 0.");
    }

    @Test
    @DisplayName("Check the get() method.")
    void get() {
        System.out.println(this);
        assertEquals(r1, storage.get(UUID_1), "Test failed. Resume r1 must be equal storage.get(\"uuid1\").");
        assertEquals(r2, storage.get(UUID_2), "Test failed. Resume r2 must be equal storage.get(\"uuid2\").");
        assertEquals(r3, storage.get(UUID_3), "Test failed. Resume r3 must be equal storage.get(\"uuid3\").");
    }

    @Test
    @DisplayName("Check the dummy resume in the storage.")
    void getNotExist() {
        System.out.println(this);
        assertThrows(NotExistStorageException.class, () -> storage.get(DUMMY_UUID));
    }

    @Test
    @DisplayName("Check the ability to update existing resume in the storage.")
    void update() {
        System.out.println(this);
        Resume new_r1 = new Resume(UUID_1);
        storage.update(new_r1);
        assertSame(new_r1, storage.get(UUID_1));
    }

    @Test
    @DisplayName("Check the ability to update not existing resume in the storage.")
    void updateNotExist() {
        System.out.println(this);
        assertThrows(NotExistStorageException.class, () -> storage.update(storage.get(DUMMY_UUID)));
    }

    @Test
    @DisplayName("Check the ability to save existing resume.")
    void saveExistingResume() {
        System.out.println(this);
        assertThrows(ExistStorageException.class, () -> storage.save(r2));
    }

    @Test
    @DisplayName("Check the ability to save new resume.")
    void saveNewResume() {
        System.out.println(this);
        storage.save(r4);
        assertEquals(r4, storage.get(NEW_UUID), "Test failed. New resume must be saved in the storage.");
        assertEquals(4, storage.size(), "Test failed. Storage size must be 4.");
    }

    @Test
    @DisplayName("Check the ability to delete existing resume.")
    void delete() {
        System.out.println(this);
        storage.delete(UUID_2);
        assertThrows(NotExistStorageException.class, () -> storage.get(UUID_2));
        assertEquals(2, storage.size(), "Test failed. Storage size must be 2.");
    }

    @Test
    @DisplayName("Check the ability to delete not existing resume. ")
    void deleteNotExist() {
        System.out.println(this);
        assertThrows(NotExistStorageException.class, () -> storage.delete(DUMMY_UUID));
    }

    @Test
    @DisplayName("Check the getAll() method.")
    void getAll() {
        System.out.println(this);
        Resume[] r = {r1, r2, r3};
        assertArrayEquals(r, storage.getAll());
    }

    @AfterEach
    void clearSetUp() {
        System.out.println("After each test. " + this);
        storage.clear();
        System.out.println("\n");
    }
}
