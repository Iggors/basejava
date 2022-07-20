create table resume
(
    uuid char(36) not null primary key,
    fill_name text not null
);

create table contact
(
    id          serial,
    resume_uuid char(36) not null references resume (uuid) on delete cascade,
    type        text     not null,
    value       text     not null
);

create unique index contact_uuid_type_index on contact (resume_uuid, type);

create table section
(
    id          serial
        constraint section_pk
            primary key,
    type        text     not null,
    content     text     not null,
    resume_uuid CHAR(36) not null
        constraint section_resume_uuid_fk
            references resume
            on update restrict on delete cascade
);

create index section__idx
    on section (resume_uuid, type);

create table section
(
    id          serial
        constraint section_pk
            primary key,
    type        text     not null,
    content     text     not null,
    resume_uuid CHAR(36) not null
        constraint section_resume_uuid_fk
            references resume
            on update restrict on delete cascade
);

create unique index section__idx
    on section (resume_uuid, type);