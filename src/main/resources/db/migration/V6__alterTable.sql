ALTER TABLE PLAN_MST ALTER COLUMN plan_id TYPE int USING plan_id::integer;
ALTER TABLE ROOM_MST ALTER COLUMN room_id TYPE int USING room_id::integer;

