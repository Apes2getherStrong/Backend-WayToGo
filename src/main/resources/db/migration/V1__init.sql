drop table if exists audios cascade;
drop table if exists map_locations cascade;
drop table if exists routes cascade;
drop table if exists routes_map_locations cascade;
drop table if exists users cascade;
create table audios
(
    audio_id        uuid         not null,
    map_location_id uuid,
    user_id         uuid,
    audio_name      varchar(100) not null,
    description     varchar(255),
    audio_data BYTEA,
    primary key (audio_id)
);
create table map_locations
(
    map_location_id uuid         not null,
    name            varchar(100) not null,
    description     varchar(255),
    coordinates     Geometry(point) not null,
    image_data BYTEA,
    primary key (map_location_id)
);
create table routes
(
    route_id    uuid        not null,
    user_id     uuid,
    name        varchar(32) not null,
    description varchar(255),
    image_data BYTEA,
    primary key (route_id)
);
create table routes_map_locations
(
    sequence_nr            integer not null check ((sequence_nr >= 0) and (sequence_nr <= 1000)),
    map_location_id        uuid    not null,
    route_id               uuid    not null,
    routes_map_location_id uuid    not null,
    primary key (routes_map_location_id)
);
create table users
(
    user_id  uuid        not null,
    password varchar(20) not null,
    username varchar(20) not null,
    login    varchar(50) not null,
    primary key (user_id)
);
alter table if exists audios add constraint FKb9n91bg6ntmqcubd5qj3h75mt foreign key (map_location_id) references map_locations on delete cascade;
alter table if exists audios add constraint FKcjfi0tcraemsm3bu7qd8lw7w0 foreign key (user_id) references users;
alter table if exists routes add constraint FKtn5l1ci7sxbp52akvblqjg4jm foreign key (user_id) references users;
alter table if exists routes_map_locations add constraint FK4ya9jurkwidykseo4e5k409g foreign key (map_location_id) references map_locations on delete cascade;
alter table if exists routes_map_locations add constraint FKn6rwqt3ppk9gqinmrlei775pv foreign key (route_id) references routes on delete cascade;
