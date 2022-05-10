package ru.basejava.model;

import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

/**
 * Initial resume class
 */
public class Resume implements Comparable<Resume> {

    private final String uuid;

    private final String fullName;
    private Map<ContactType, String> contacts = new EnumMap<>(ContactType.class);
    private Map<SectionType, AbstractSection> sections = new EnumMap<>(SectionType.class);

    public Resume(String fullName) {
        this(UUID.randomUUID().toString(), fullName);
    }

    public Resume(String uuid, String fullName) {
        if (uuid == null || fullName == null) {
            throw new IllegalArgumentException(String.format("Parameters can't be null: uuid=%s, fullName=%s",
                    uuid, fullName));
        }
        this.uuid = uuid;
        this.fullName = fullName;
    }

    public void setSection(Map<SectionType, AbstractSection> section) {
        this.sections = section;
    }

    public void setContacts(Map<ContactType, String> contacts) {
        this.contacts = contacts;
    }

    public Map<SectionType, AbstractSection> getSection() {
        return sections;
    }

    public Map<ContactType, String> getContacts() {
        return contacts;
    }

    public String getUuid() {
        return uuid;
    }

    public String getFullName() {
        return fullName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Resume resume = (Resume) o;

        if (!uuid.equals(resume.uuid)) return false;
        return fullName.equals(resume.fullName);
    }

    @Override
    public int hashCode() {
        int result = uuid.hashCode();
        result = 31 * result + fullName.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return uuid + " - " + fullName;
    }

    @Override
    public int compareTo(Resume o) {
        int result = fullName.compareTo(o.fullName);

        if (result == 0) {
            result = uuid.compareTo(o.uuid);
        }
        return result;
    }
}
