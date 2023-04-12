
CREATE TABLE USER_MST (
 user_seq_no serial 
 ,user_id varchar(100) NOT NULL 
 ,user_name varchar(100) NOT NULL
 , email varchar(100) NOT NULL 
 , password varchar(100) NOT NULL
 , create_user int NOT NULL
 , create_date date NOT NULL
 , update_user int NOT NULL 
 , update_date date NOT NULL 
 , del_flg char(1) NOT NULL 
 , PRIMARY KEY (user_seq_no)
) ;


CREATE TABLE PLAN_MST (
 plan_id char(2)  
 ,plan_name varchar(100) NOT NULL
 , plan_contents varchar(1000) NOT NULL 
 , meal_type char(1) NOT NULL
 , plan_fee int NOT NULL
 , room_id char(2) NOT NULL
 , image_photo varchar(100) NOT NULL
 , create_user int NOT NULL
 , create_date date NOT NULL
 , update_user int NOT NULL 
 , update_date date NOT NULL 
 , del_flg char(1) NOT NULL 
 , PRIMARY KEY (plan_id)
) ;


CREATE TABLE ROOM_MST (
 room_id char(2)  
 ,room_type char(1) NOT NULL
 ,room_rank char(1) NOT NULL
 ,smoking_type char(1) NOT NULL
 ,bathroom_type char(1) NOT NULL
 , guest_capacity int NOT NULL
 , create_user int NOT NULL
 , create_date date NOT NULL
 , update_user int NOT NULL 
 , update_date date NOT NULL 
 , del_flg char(1) NOT NULL 
 , PRIMARY KEY (room_id)
) ;


CREATE TABLE CALENDER_TABLE (
 date date  
 ,date_type char(1) NOT NULL
 ,room_id char(2) 
 ,total_rooms int NOT NULL
 ,vacancy_rooms int NOT NULL
 , room_fee int NOT NULL
 , create_user int NOT NULL
 , create_date date NOT NULL
 , update_user int NOT NULL 
 , update_date date NOT NULL 
 , del_flg char(1) NOT NULL 
 , PRIMARY KEY (date, room_id)
) ;


CREATE TABLE RESERVATION_TABLE (
 reservation_id serial  
 ,reservation_user int NOT NULL
 ,name varchar(100) NOT NULL
 ,name_kana varchar(100) NOT NULL
 ,zip_code varchar(7) NOT NULL
 ,address varchar(100) NOT NULL
 ,telephone_number varchar(12) NOT NULL
 ,checkin_date date NOT NULL
 ,checkout_date date NOT NULL
 ,guest_number int NOT NULL
 ,plan_id varchar(2) NOT NULL
 ,cancel_status varchar(1) NOT NULL
 ,total_price int NOT NULL
 , create_user int NOT NULL
 , create_date date NOT NULL
 , update_user int NOT NULL 
 , update_date date NOT NULL 
 , del_flg char(1) NOT NULL 
 , PRIMARY KEY (reservation_id)
) ;


CREATE TABLE CODE_MST (
 group_code varchar(100) 
 ,code_name varchar(100) NOT NULL
 ,code char(2) 
 ,sort_number int NOT NULL
 , create_user int NOT NULL
 , create_date date NOT NULL
 , update_user int NOT NULL 
 , update_date date NOT NULL 
 , del_flg char(1) NOT NULL 
 , PRIMARY KEY (group_code,code)
) ;
