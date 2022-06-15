package ru.basejava.storage.serializer;

import ru.basejava.model.*;
import ru.basejava.util.DateUtil;

import java.io.*;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataStreamSerializer implements StreamSerializerStrategy {

    @Override
    public void doWrite(Resume r, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(r.getUuid());
            dos.writeUTF(r.getFullName());
            Map<ContactType, String> contacts = r.getContacts();
            dos.writeInt(contacts.size());
            for (Map.Entry<ContactType, String> entry : contacts.entrySet()) {
                dos.writeUTF(entry.getKey().name());
                dos.writeUTF(entry.getValue());
            }

            //TODO implements sections
            Map<SectionType, AbstractSection> sections = r.getSection();
            dos.writeInt(sections.size());
            for (Map.Entry<SectionType, AbstractSection> section  : sections.entrySet()) {
                SectionType sectionType = section.getKey();
                dos.writeUTF(String.valueOf(sectionType));
                AbstractSection sectionTypeValue = section.getValue();
                switch (sectionType) {
                    case OBJECTIVE, PERSONAL -> {
                        dos.writeUTF(((TextSection) sectionTypeValue).getDescription());
                    }
                    case ACHIEVEMENT, QUALIFICATIONS -> {
                        List<String> itemList = ((ListSection) sectionTypeValue).getItems();
                        dos.writeInt(itemList.size());
                        for (String content : itemList) {
                            dos.writeUTF(content);
                        }
                    }
                    case EXPERIENCE, EDUCATION -> {
                        List<Organization> organizationsList = (((OrganizationSection) sectionTypeValue)).getOrganizations();
                        dos.writeInt(organizationsList.size());
                        for (Organization organization : organizationsList) {
                            Link link = organization.getHomePage();
                            dos.writeUTF(link.getName());
                            String linkUrl = organization.getHomePage().getUrl() == null ? "" : organization.getHomePage().getUrl();
                            dos.writeUTF(linkUrl);
                            List<Organization.Position> positions = organization.getPositions();
                            dos.writeInt(positions.size());
                            for (Organization.Position position : positions) {
                                writeLocalDate(dos, position.getStartDate());
                                writeLocalDate(dos, position.getEndDate());
                                dos.writeUTF(position.getTitle());
                                String description = position.getDescription() == null ? "" : position.getDescription();
                                dos.writeUTF(description);
                            }
                        }
                    }
                }
            }
        }
    }

    private void writeLocalDate(DataOutputStream dos, LocalDate localDate) throws IOException {
        dos.writeInt(localDate.getYear());
        dos.writeInt(localDate.getMonthValue());
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (DataInputStream dis = new DataInputStream(is)) {
            String uuid = dis.readUTF();
            String fullName = dis.readUTF();
            Resume resume = new Resume(uuid, fullName);
            int size = dis.readInt();

            for (int i = 0; i < size; i++) {
                resume.addContact(ContactType.valueOf(dis.readUTF()), dis.readUTF());
            }

            //TODO implements sections
            int sizeSection = dis.readInt();
            for (int i = 0; i < sizeSection; i++) {
                SectionType sectionType = SectionType.valueOf(dis.readUTF());
                switch (sectionType) {
                    case OBJECTIVE, PERSONAL -> {
                        resume.addSection(sectionType, new TextSection(dis.readUTF()));
                    }
                    case ACHIEVEMENT, QUALIFICATIONS -> {
                        List<String> elements = new ArrayList<>();
                        int sizeElements = dis.readInt();
                        for (int j = 0; j < sizeElements; j++) {
                            elements.add(dis.readUTF());
                        }
                        resume.addSection(sectionType, new ListSection(elements));
                    }

                    case EXPERIENCE, EDUCATION -> {
                        int organizationsListSize = dis.readInt();
                        List<Organization> organizationsList = new ArrayList<>(organizationsListSize);
                        for (int j = 0; j < organizationsListSize; j++) {
                            Link link = new Link(dis.readUTF(), dis.readUTF());
                            int positionSize = dis.readInt();
                            List<Organization.Position> positionsList = new ArrayList<>();
                            for (int k = 0; k < positionSize; k++) {
                                LocalDate startLocalDate = readLocalDate(dis);
                                LocalDate endLocalDate = readLocalDate(dis);
                                String title = dis.readUTF();
                                String description = dis.readUTF();
                                positionsList.add(new Organization.Position(startLocalDate, endLocalDate, title, description));
                            }
                            organizationsList.add(new Organization(link, positionsList));
                        }
                        resume.addSection(sectionType, new OrganizationSection(organizationsList));
                    }
                }
            }
            return resume;
        }
    }

    private LocalDate readLocalDate(DataInputStream dis) throws IOException {
        return DateUtil.of(dis.readInt(), Month.of(dis.readInt()));
    }
}


