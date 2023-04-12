ALTER TABLE PLAN_MST ALTER COLUMN room_id TYPE int USING room_id::integer;
ALTER TABLE CALENDER_TABLE ALTER COLUMN room_id TYPE int USING room_id::integer;
ALTER TABLE RESERVATION_TABLE ALTER COLUMN plan_id TYPE int USING plan_id::integer;

create sequence plan_mst_plan_id_seq start 35;
alter table PLAN_MST alter plan_id set default nextval('plan_mst_plan_id_seq');
alter sequence plan_mst_plan_id_seq owned by PLAN_MST.plan_id;

create sequence room_mst_room_id_seq start 11;
alter table ROOM_MST alter room_id set default nextval('room_mst_room_id_seq');
alter sequence room_mst_room_id_seq owned by ROOM_MST.room_id;

