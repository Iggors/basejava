package ru.basejava.storage.serializer;

import ru.basejava.model.*;
import ru.basejava.util.XmlParser;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class XmlStreamSerializer implements StreamSerializerStrategy {
    private XmlParser xmlParser;

    public XmlStreamSerializer() {
        xmlParser = new XmlParser(
                Resume.class,
                Organization.class,
                Link.class,
                OrganizationSection.class,
                TextSection.class,
                ListSection.class,
                Organization.Position.class);
    }

    @Override
    public void doWrite(Resume r, OutputStream os) throws IOException {
        try (Writer w = new OutputStreamWriter(os, StandardCharsets.UTF_8)) {
            xmlParser.marshall(r, w);
        }
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (Reader r = new InputStreamReader(is, StandardCharsets.UTF_8)) {
            return xmlParser.unmarshall(r);
        }
    }
}
