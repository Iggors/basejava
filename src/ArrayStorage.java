/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    Resume[] storage = new Resume[10000];

    // nElems - The counter of not null elements in the storage[]
    int nElems = 0;

    void clear() {
        for (int i = 0; i < nElems; i++) {
            if (storage[i] != null){
                storage[i] = null;
                nElems = 0;
            }
        }
    }

    void save(Resume r) {
        storage[nElems] = r;
        nElems++;
    }

    Resume get(String uuid) {
        for (int i = 0; i < nElems; i++) {
            if (storage[i].toString() == uuid)
                return storage[i];
        }
        return null;
    }

    void delete(String uuid) {
        int i;
        for (i = 0; i < nElems; i++)
            if (uuid == storage[i].toString())
                break;
        if (i == nElems)
            System.out.println("The resume with unique identifier " + uuid + "doesn't exist.");
        else {
            for (int k = i; k < nElems; k++)
                storage[k] = storage[k+1];
            nElems--;
            System.out.println("The resume with unique identifier " + uuid + " was succesfully deleted.");
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    Resume[] getAll() {
        if (nElems > 0){
            Resume[] tempStorage = new Resume[nElems];
            System.arraycopy(storage, 0, tempStorage, 0, nElems);
            return tempStorage;
        }
        else return new Resume[0];
    }

    int size() {
        if (nElems != 0)
            return nElems;
        else return 0;

    }
}
