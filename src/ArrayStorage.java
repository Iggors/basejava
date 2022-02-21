/**
 * Array based storage for Resumes
 */
public class ArrayStorage
{
    Resume[] storage = new Resume[10000];

    // size - The counter of not null elements in the storage[]
    int size = 0;

    void clear()
    {
        for (int i = 0; i < size; i++)
        {
            storage[i] = null;
        }
        size = 0;
    }

    void save(Resume r)
    {
        int i;
        for (i = 0; i < size; i++)
        {
            if (storage[i].toString().equals(r.toString()))
            {
                System.out.println("The resume with unique identifier " + r.uuid + " already exists.");
                break;
            }
        }
        if (i == size)
        {
            storage[size] = r;
            size++;
            System.out.println("The resume with unique identifier " + r.uuid + " was succesfully saved.");
        }
    }

    Resume get(String uuid)
    {
        for (int i = 0; i < size; i++)
        {
            if (storage[i].toString() == uuid)
            {
                return storage[i];
            }
        }
        return null;
    }

    void delete(String uuid)
    {
        int i;
        for (i = 0; i < size; i++)
        {
            if (uuid == storage[i].toString())
            {
                break;
            }
        }
        if (i == size)
        {
            System.out.println("The resume with unique identifier " + uuid + " doesn't exist.");
        }
        else
        {
            for (int k = i; k < size - 1; k++)
            {
                storage[k] = storage[k + 1];
            }
            size--;
            System.out.println("The resume with unique identifier " + uuid + " was succesfully deleted.");
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    Resume[] getAll()
    {
        if (size > 0)
        {
            Resume[] tempStorage = new Resume[size];
            System.arraycopy(storage, 0, tempStorage, 0, size);
            return tempStorage;
        }
        else return new Resume[0];
    }

    int size()
    {
        return size;
    }
}