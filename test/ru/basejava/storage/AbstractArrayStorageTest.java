package ru.basejava.storage;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.basejava.exception.StorageException;
import ru.basejava.model.Resume;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

public abstract class AbstractArrayStorageTest extends AbstractStorageTest {

    public AbstractArrayStorageTest(Storage storage) {
        super(storage);
    }

    @Test
    @DisplayName("Check the storage overflow.")
    void storageOverflow() {
        try {
            for (int i = 4; i < AbstractArrayStorage.STORAGE_LIMIT + 1; i++) {
                storage.save(new Resume());
            }
        } catch (StorageException e) {
            fail("The storage overflow occurred prematurely.");
        }
        assertThrows(StorageException.class, () -> storage.save(new Resume()));
    }
}