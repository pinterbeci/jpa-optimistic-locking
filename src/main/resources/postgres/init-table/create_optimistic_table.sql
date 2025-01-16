create table if not exists optimistic
(
    id            varchar      not null unique default uuid_generate_v4(),
    description   varchar,
    is_deleted    boolean      default false,
    version       numeric      default 0,
    modifier_id   varchar      not null,
    last_modified timestamp(6) not null default current_timestamp(6),
    creator_id    varchar      not null,
    created_at    timestamp(6) not null,
    constraint pk_address_entity primary key (id)
 );