package ru.basejava.model;

import java.util.Objects;

public class TextSection extends AbstractSection {
    private final String description;

    public TextSection(String description) {
        Objects.requireNonNull(description, "description must not be null");
        this.description = description;
    }

    @Override
    public String toString() {
        return description + "\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TextSection that = (TextSection) o;

        return Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return description.hashCode();
    }
}
