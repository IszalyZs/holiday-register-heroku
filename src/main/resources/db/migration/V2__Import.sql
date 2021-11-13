INSERT INTO holiday_day (id,year) VALUES (1,'2021');
INSERT INTO holiday_day (id,year) VALUES (2,'2022');

INSERT INTO holiday_day_local_date (holiday_day_id,local_date) VALUES (1,'2021-01-01');
INSERT INTO holiday_day_local_date (holiday_day_id,local_date) VALUES (1,'2021-03-15');
INSERT INTO holiday_day_local_date (holiday_day_id,local_date) VALUES (1,'2021-04-02');
INSERT INTO holiday_day_local_date (holiday_day_id,local_date) VALUES (1,'2021-04-04');
INSERT INTO holiday_day_local_date (holiday_day_id,local_date) VALUES (1,'2021-04-05');
INSERT INTO holiday_day_local_date (holiday_day_id,local_date) VALUES (1,'2021-05-01');
INSERT INTO holiday_day_local_date (holiday_day_id,local_date) VALUES (1,'2021-05-23');
INSERT INTO holiday_day_local_date (holiday_day_id,local_date) VALUES (1,'2021-05-24');
INSERT INTO holiday_day_local_date (holiday_day_id,local_date) VALUES (1,'2021-08-20');
INSERT INTO holiday_day_local_date (holiday_day_id,local_date) VALUES (1,'2021-10-23');
INSERT INTO holiday_day_local_date (holiday_day_id,local_date) VALUES (1,'2021-11-01');
INSERT INTO holiday_day_local_date (holiday_day_id,local_date) VALUES (1,'2021-12-24');
INSERT INTO holiday_day_local_date (holiday_day_id,local_date) VALUES (1,'2021-12-25');
INSERT INTO holiday_day_local_date (holiday_day_id,local_date) VALUES (1,'2021-12-26');

INSERT INTO holiday_day_local_date (holiday_day_id,local_date) VALUES (2,'2022-01-01');
INSERT INTO holiday_day_local_date (holiday_day_id,local_date) VALUES (2,'2022-03-14');
INSERT INTO holiday_day_local_date (holiday_day_id,local_date) VALUES (2,'2022-03-15');
INSERT INTO holiday_day_local_date (holiday_day_id,local_date) VALUES (2,'2022-04-02');
INSERT INTO holiday_day_local_date (holiday_day_id,local_date) VALUES (2,'2022-04-04');
INSERT INTO holiday_day_local_date (holiday_day_id,local_date) VALUES (2,'2022-04-05');
INSERT INTO holiday_day_local_date (holiday_day_id,local_date) VALUES (2,'2022-05-01');
INSERT INTO holiday_day_local_date (holiday_day_id,local_date) VALUES (2,'2022-05-23');
INSERT INTO holiday_day_local_date (holiday_day_id,local_date) VALUES (2,'2022-05-24');
INSERT INTO holiday_day_local_date (holiday_day_id,local_date) VALUES (2,'2022-08-20');
INSERT INTO holiday_day_local_date (holiday_day_id,local_date) VALUES (2,'2022-10-23');
INSERT INTO holiday_day_local_date (holiday_day_id,local_date) VALUES (2,'2022-11-01');
INSERT INTO holiday_day_local_date (holiday_day_id,local_date) VALUES (2,'2022-12-24');
INSERT INTO holiday_day_local_date (holiday_day_id,local_date) VALUES (2,'2022-12-25');
INSERT INTO holiday_day_local_date (holiday_day_id,local_date) VALUES (2,'2022-12-26');

INSERT INTO employee (id,basic_leave,extra_leave,beginning_of_employment,birth_date,date_of_entry,first_name,last_name,identity_number,position,workplace,next_year_leave,sum_holiday,sum_holiday_next_year) VALUES (1,23,7,'2021-01-15','1990-10-27','2021-01-05','Kis','Péter','123456789','manager','IBM',30,25,8);
INSERT INTO employee (id,basic_leave,extra_leave,beginning_of_employment,birth_date,date_of_entry,first_name,last_name,identity_number,position,workplace,next_year_leave,sum_holiday,sum_holiday_next_year) VALUES (2,21,4,'2021-05-05','1996-10-27','2021-05-05','Molnár','Ákos','223456789','manager','IBM',25,20,8);
INSERT INTO employee (id,basic_leave,extra_leave,beginning_of_employment,birth_date,date_of_entry,first_name,last_name,identity_number,position,workplace,next_year_leave,sum_holiday,sum_holiday_next_year) VALUES (3,25,2,'2021-03-10','1985-10-27','2021-03-05','Kovács','Rozália','222456789','manager','IBM',27,25,8);
INSERT INTO employee (id,basic_leave,extra_leave,beginning_of_employment,birth_date,date_of_entry,first_name,last_name,identity_number,position,workplace,next_year_leave,sum_holiday,sum_holiday_next_year) VALUES (4,20,0,'2021-02-05','2000-10-27','2021-02-05','Nagy','Tibor','223452789','manager','IBM',20,20,8);

INSERT INTO children (id,birth_day,first_name,last_name,employee_id) VALUES (1,'2008-01-15','Kis','Csaba',1);
INSERT INTO children (id,birth_day,first_name,last_name,employee_id) VALUES (2,'2009-06-02','Kis','Péter',1);
INSERT INTO children (id,birth_day,first_name,last_name,employee_id) VALUES (3,'2012-04-25','Kis','Anna',1);

INSERT INTO children (id,birth_day,first_name,last_name,employee_id) VALUES (4,'2018-03-15','Molnár','Klára',2);
INSERT INTO children (id,birth_day,first_name,last_name,employee_id) VALUES (5,'2019-01-15','Molnár','Csaba',2);

INSERT INTO children (id,birth_day,first_name,last_name,employee_id) VALUES (6,'2006-01-15','Nagy','Zsolt',3);
INSERT INTO children (id,birth_day,first_name,last_name,employee_id) VALUES (7,'2008-07-15','Nagy','Márk',3);

INSERT INTO holiday (holiday_id,start_date,finish_date,employee_id) VALUES (1,'2021-02-16','2021-02-25',1);
INSERT INTO holiday (holiday_id,start_date,finish_date,employee_id) VALUES (2,'2021-03-16','2021-03-19',1);
INSERT INTO holiday (holiday_id,start_date,finish_date,employee_id) VALUES (3,'2021-12-25','2022-01-12',1);
INSERT INTO holiday (holiday_id,start_date,finish_date,employee_id) VALUES (4,'2021-04-29','2021-05-05',1);
INSERT INTO holiday (holiday_id,start_date,finish_date,employee_id) VALUES (5,'2021-05-16','2021-05-19',1);

INSERT INTO holiday (holiday_id,start_date,finish_date,employee_id) VALUES (6,'2021-05-16','2021-05-22',2);
INSERT INTO holiday (holiday_id,start_date,finish_date,employee_id) VALUES (7,'2021-06-16','2021-06-19',2);
INSERT INTO holiday (holiday_id,start_date,finish_date,employee_id) VALUES (8,'2021-12-25','2022-01-12',2);
INSERT INTO holiday (holiday_id,start_date,finish_date,employee_id) VALUES (9,'2021-05-29','2021-06-05',2);
INSERT INTO holiday (holiday_id,start_date,finish_date,employee_id) VALUES (10,'2021-07-16','2021-07-19',2);

INSERT INTO holiday (holiday_id,start_date,finish_date,employee_id) VALUES (11,'2021-03-16','2021-03-25',3);
INSERT INTO holiday (holiday_id,start_date,finish_date,employee_id) VALUES (12,'2021-04-16','2021-04-19',3);
INSERT INTO holiday (holiday_id,start_date,finish_date,employee_id) VALUES (13,'2021-12-25','2022-01-12',3);
INSERT INTO holiday (holiday_id,start_date,finish_date,employee_id) VALUES (14,'2021-07-29','2021-08-05',3);
INSERT INTO holiday (holiday_id,start_date,finish_date,employee_id) VALUES (15,'2021-08-16','2021-08-19',3);

INSERT INTO holiday (holiday_id,start_date,finish_date,employee_id) VALUES (16,'2021-02-16','2021-02-19',4);
INSERT INTO holiday (holiday_id,start_date,finish_date,employee_id) VALUES (17,'2021-03-16','2021-03-19',4);
INSERT INTO holiday (holiday_id,start_date,finish_date,employee_id) VALUES (18,'2021-12-25','2022-01-12',4);
INSERT INTO holiday (holiday_id,start_date,finish_date,employee_id) VALUES (19,'2021-04-29','2021-05-05',4);
INSERT INTO holiday (holiday_id,start_date,finish_date,employee_id) VALUES (20,'2021-09-16','2021-09-19',4);
