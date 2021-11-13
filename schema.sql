drop table if exists children cascade;
create table if not exists children (
                                        id INTEGER PRIMARY KEY AUTO_INCREMENT,
                                        birth_day date not null,
                                        first_name varchar(255),
    last_name varchar(255),
    employee_id INTEGER,
    primary key (id)
    );

drop table if exists employee cascade;
create table if not exists employee (
                                        id INTEGER PRIMARY KEY AUTO_INCREMENT,
                                        basic_leave INTEGER,
                                        beginning_of_employment date,
                                        birth_date date not null,
                                        date_of_entry date not null,
                                        extra_leave INTEGER,
                                        first_name varchar(255),
    identity_number varchar(255),
    last_name varchar(255),
    next_year_leave INTEGER,
    position varchar(255),
    sum_holiday INTEGER,
    sum_holiday_next_year INTEGER,
    workplace varchar(255),
    primary key (id)
    );

drop table if exists holiday cascade;
create table if not exists holiday (
                                       holiday_id INTEGER PRIMARY KEY AUTO_INCREMENT,
                                       finish_date date not null,
                                       start_date date not null,
                                       employee_id INTEGER,
                                       primary key (holiday_id)
    );

drop table if exists holiday_day cascade;
create table if not exists holiday_day (
                                           id INTEGER PRIMARY KEY AUTO_INCREMENT,
                                           year varchar(255) not null,
    primary key (id)
    );

drop table if exists holiday_day_local_date cascade;
create table if not exists holiday_day_local_date (
                                                      holiday_day_id INTEGER not null,
                                                      local_date date
);

alter table employee
    add constraint UK_fg7c8qqudo49j0eqghq032xis unique (identity_number);

alter table holiday_day
    add constraint UK_b7aef5nelntbxhxq9jh2x5oco unique (year);

alter table children
    add constraint FKfcifkd8uyf8crh0h0p7k3o3d8
        foreign key (employee_id)
            references employee;

alter table holiday
    add constraint FKfcn4ebwwpcrk1dbvjqr760hyg
        foreign key (employee_id)
            references employee;

alter table holiday_day_local_date
    add constraint FK2yidl60i98ibjt2lavtv8drh9
        foreign key (holiday_day_id)
            references holiday_day;
