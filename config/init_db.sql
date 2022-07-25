CREATE TABLE resume
(
    uuid char(36) not null primary key,
    full_name text not null
);

CREATE TABLE contact
(
    id          serial primary key,
    resume_uuid char(36) not null references resume (uuid) on delete cascade,
    type        text     not null,
    value       text     not null
);


CREATE TABLE section
(
    id          serial
        constraint section_pk
            primary key,
    type        text     not null,
    value     text     not null,
    resume_uuid CHAR(36) not null
        constraint section_resume_uuid_fk
            references resume (uuid)
            on update restrict on delete cascade
);

CREATE UNIQUE INDEX contact_uuid_type_index ON contact (resume_uuid, type);

CREATE UNIQUE INDEX section_uuid_type_index ON section (resume_uuid, type);