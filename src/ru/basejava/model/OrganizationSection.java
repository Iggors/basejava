package ru.basejava.model;

import java.util.List;
import java.util.Objects;

public class OrganizationSection extends AbstractSection {
    private static final long serialVersionUID = 1L;

    private final List<Organization> listOrganizations;

    public OrganizationSection(List<Organization> listOrganizations) {
        Objects.requireNonNull(listOrganizations, "list must not be null");
        this.listOrganizations = listOrganizations;
    }

    public List<Organization> getOrganizations() {
        return listOrganizations;
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

        return listOrganizations.equals(that.listOrganizations);
    }

    @Override
    public int hashCode() {
        return listOrganizations.hashCode();
    }
}
