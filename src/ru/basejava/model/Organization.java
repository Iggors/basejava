package ru.basejava.model;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Organization {
    private final String companyName;
    private final String link;
    private final List<Position> positions;

    public Organization(String companyName, String link, Position... positions) {
        this.companyName = companyName;
        this.link = link;
        this.positions = Arrays.asList(positions);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Organization that = (Organization) o;

        if (!companyName.equals(that.companyName)) return false;
        if (!link.equals(that.link)) return false;
        return positions.equals(that.positions);
    }

    @Override
    public int hashCode() {
        int result = companyName.hashCode();
        result = 31 * result + link.hashCode();
        result = 31 * result + positions.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Organization{" +
                "companyName='" + companyName + '\'' +
                ", Link='" + link + '\'' +
                ", positions=" + positions +
                '}';
    }

    public static class Position {
        private final  LocalDate startDate;
        private final  LocalDate endDate;
        private final  String title;
        private final  String description;

        public Position(LocalDate startDate, String title, String description) {
            Objects.requireNonNull(startDate, "startDate must not be null");
            Objects.requireNonNull(title, "title must not be null");
            this.startDate = startDate;
            this.endDate = null;
            this.title = title;
            this.description = description;
        }

        public Position(LocalDate startDate, LocalDate endDate, String title, String description) {
            Objects.requireNonNull(startDate, "startDate must not be null");
            Objects.requireNonNull(endDate, "endDate must not be null");
            Objects.requireNonNull(title, "title must not be null");
            this.startDate = startDate;
            this.endDate = endDate;
            this.title = title;
            this.description = description;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Position position = (Position) o;

            if (!startDate.equals(position.startDate)) return false;
            if (endDate != null ? !endDate.equals(position.endDate) : position.endDate != null) return false;
            if (!title.equals(position.title)) return false;
            return description.equals(position.description);
        }

        @Override
        public int hashCode() {
            int result = startDate.hashCode();
            result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
            result = 31 * result + title.hashCode();
            result = 31 * result + description.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return "\nPosition(" + startDate + ',' + endDate + ',' + title + ',' + description + ')';
        }
    }
}
