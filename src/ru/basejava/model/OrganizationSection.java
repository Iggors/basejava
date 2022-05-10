package ru.basejava.model;

import java.util.List;
import java.util.Objects;

public class OrganizationSection extends AbstractSection {
    private final List<Organization> listOrganizations;

    public OrganizationSection(List<Organization> listOrganizations) {
        Objects.requireNonNull(listOrganizations, "list must not be null");
        this.listOrganizations = listOrganizations;
    }

    @Override
    public String toString() {
        return listOrganizations.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrganizationSection that = (OrganizationSection) o;

        return Objects.equals(listOrganizations, that.listOrganizations);
    }

    @Override
    public int hashCode() {
        return listOrganizations.hashCode();
    }
}
