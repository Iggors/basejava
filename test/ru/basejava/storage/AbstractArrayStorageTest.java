package ru.basejava.storage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.basejava.exception.ExistStorageException;
import ru.basejava.exception.NotExistStorageException;
import ru.basejava.exception.StorageException;
import ru.basejava.model.Resume;

class AbstractArrayStorageTest {

    private final Storage storage;

    private static final int STORAGE_LIMIT = 10000;
    private static final String UUID_1 = "uuid1";
    private static final String UUID_2 = "uuid2";
    private static final String UUID_3 = "uuid3";

    private Resume r1 = new Resume(UUID_1);
    private Resume r2 = new Resume(UUID_2);
    private Resume r3 = new Resume(UUID_3);
    private Resume r4 = new Resume("new_uuid");

    public AbstractArrayStorageTest(Storage storage) {
        this.storage = storage;
    }

    @BeforeEach
    void setUp() throws Exception {
        System.out.println("Before each test. " + this);
        storage.save(r1);
        storage.save(r2);
        storage.save(r3);
    }

    @Test
    void size() throws Exception {
        System.out.println("Test #1. Check the size() method. " + this);
        Assertions.assertEquals(3, storage.size(), "Test faild. Storage size = 3.");
    }

    @Test
    void clear() throws Exception {
        System.out.println("Test #2. Check the clear() method. " + this);
        storage.clear();
        Assertions.assertEquals(0, storage.size(), "Test faild. Storage size must be 0.");
    }

    @Test
    void get() throws Exception {
        System.out.println("Test #3. Check the get() method. " + this);
        Assertions.assertEquals(r1, storage.get(r1.getUuid()), "Test faild. Resume r1 must be equal r1.Get().");
        Assertions.assertEquals(r2, storage.get(r2.getUuid()), "Test faild. Resume r2 must be equal r2.Get().");
        Assertions.assertEquals(r3, storage.get(r3.getUuid()), "Test faild. Resume r3 must be equal r3.Get().");
    }

    @Test
    void getNotExist() throws NotExistStorageException {
        System.out.println("Test #4. Check the dummy resume in the storage. " + this);
        Assertions.assertThrows(NotExistStorageException.class, () -> {
            storage.get("dummy");
        });
    }

    @Test
    void update() throws Exception {
        System.out.println("Test #5. Check the ability to update existing resume in the storage. " + this);
        Resume new_r1 = new Resume(UUID_1);
        storage.update(new_r1);
        Assertions.assertSame(new_r1, storage.get(UUID_1));
    }

    @Test
    void updateNotExist() throws NotExistStorageException {
        System.out.println("Test #6. Check the ability to update not existing resume in the storage. " + this);
        Assertions.assertThrows(NotExistStorageException.class, () -> {
            storage.update(storage.get("dummy"));
        });
    }

    @Test
    void saveExistingResume() throws ExistStorageException {
        System.out.println("Test #7. Check the ability to save existing resume. " + this);
        Assertions.assertThrows(ExistStorageException.class, () -> {
            storage.save(r2);
        });
    }

    @Test
    void saveNewResume() throws Exception {
        System.out.println("Test #8. Check the ability to save new resume. " + this);
        storage.save(r4);
        Assertions.assertEquals(4, storage.size(), "Test faild. Storage size must be 4.");
    }

    @Test
    void storageOverflow() throws StorageException {
        System.out.println("Test #7. Check the storage overflow. ");
        Assertions.assertThrows(StorageException.class, () -> {
            try {
                for (int i = 4; i < STORAGE_LIMIT + 1; i++) {
                    storage.save(new Resume());
                }
            } catch (StorageException e) {
                Assertions.fail("The storage overflow.");
            }
            storage.save(new Resume());
        });
    }

    @Test
    void delete() throws Exception {
        System.out.println("Test #10. Check the ability to delete existing resume. " + this);
        storage.delete(UUID_2);
        Assertions.assertEquals(2, storage.size(), "Test faild. Storage size must be 2.");
    }

    @Test
    void deleteNotExist() throws NotExistStorageException {
        System.out.println("Test #11. Check the ability to delete not existing resume. " + this);
        Assertions.assertThrows(NotExistStorageException.class, () -> {
            storage.delete("dummy");
        });
    }

    @Test
    void getAll() throws Exception {
        System.out.println("Test #12. Check the getAll() method. " + this);
        Resume[] r = {r1, r2, r3};
        Assertions.assertArrayEquals(r, storage.getAll());
    }

    @AfterEach
    void clearSetUp() throws Exception {
        System.out.println("After each test. " + this);
        storage.clear();
        System.out.println("\n");
    }
}