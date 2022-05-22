package ru.basejava.storage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.basejava.ResumeTestData;
import ru.basejava.exception.ExistStorageException;
import ru.basejava.exception.NotExistStorageException;
import ru.basejava.model.Resume;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class AbstractStorageTest {
    protected Storage storage;

    protected static final String UUID_1 = "uuid_1";
    protected static final String UUID_2 = "uuid_2";
    protected static final String UUID_3 = "uuid_3";
    protected static final String NEW_UUID = "new_uuid";
    protected static final String DUMMY_UUID = "dummy";

    protected static final String FULL_NAME_1 = "name_1";
    protected static final String FULL_NAME_2 = "name_2";
    protected static final String FULL_NAME_3 = "name_3";
    protected static final String NEW_FULL_NAME = "new_name";

    private static final Resume R1;
    private static final Resume R2;
    private static final Resume R3;
    private static final Resume R4;

    static {
        R1 = ResumeTestData.resumeCreator(UUID_1, FULL_NAME_1);
        R2 = ResumeTestData.resumeCreator(UUID_2, FULL_NAME_2);
        R3 = ResumeTestData.resumeCreator(UUID_3, FULL_NAME_3);
        R4 = ResumeTestData.resumeCreator(NEW_UUID, NEW_FULL_NAME);
    }

    public AbstractStorageTest(Storage storage) {
        this.storage = storage;
    }

    @BeforeEach
    void setUp() {
       // System.out.println("Before each test. " + this);
        storage.save(R1);
        storage.save(R2);
        storage.save(R3);
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
        assertEquals(R1, storage.get(UUID_1), "Test failed. Resume R1 must be equal storage.get(\"uuid1\").");
        assertEquals(R2, storage.get(UUID_2), "Test failed. Resume R2 must be equal storage.get(\"uuid2\").");
        assertEquals(R3, storage.get(UUID_3), "Test failed. Resume R3 must be equal storage.get(\"uuid3\").");
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
        Resume new_R1 = new Resume(UUID_1,FULL_NAME_1);
        storage.update(new_R1);
        assertSame(new_R1, storage.get(UUID_1));
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
        assertThrows(ExistStorageException.class, () -> storage.save(R2));
    }

    @Test
    @DisplayName("Check the ability to save new resume.")
    void saveNewResume() {
        System.out.println(this);
        storage.save(R4);
        assertEquals(R4, storage.get(NEW_UUID), "Test failed. New resume must be saved in the storage.");
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
    @DisplayName("Check the getAllSorted() method.")
    void getAllSorted() {
        System.out.println(this);
        List<Resume> rList = Arrays.asList(R1, R2, R3);
        assertEquals(rList, storage.getAllSorted());
    }

    @AfterEach
    void clearSetUp() {
        System.out.println("After each test. " + this);
        storage.clear();
        System.out.println("\n");
    }
}
