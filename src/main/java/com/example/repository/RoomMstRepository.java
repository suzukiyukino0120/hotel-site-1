package com.example.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.domain.Code;
import com.example.domain.Room;

@Repository
public class RoomMstRepository {
	@Autowired
	private NamedParameterJdbcTemplate template;
	
	private final static RowMapper<Room> ROOM_ROW_MAPPER=
	(rs,i) -> {
		Room room = new Room();
		room.setRoomId(rs.getInt("room_id"));
		room.setRoomType(rs.getString("room_type"));
		room.setRoomRank(rs.getString("room_rank"));
		room.setSmokingType(rs.getString("smoking_type"));
		room.setBathroomType(rs.getString("bathroom_type"));
		room.setGuestCapacity(rs.getInt("guest_capacity"));
		return room;
	};
	
	public Room selectRoomById(Integer roomId) {
		String sql="SELECT * FROM ROOM_MST WHERE room_id = :roomId AND del_flg='0'";
		SqlParameterSource param = new MapSqlParameterSource().addValue("roomId", roomId);
		return template.queryForObject(sql,param,ROOM_ROW_MAPPER);
	}
	
	public List<Room> selectAll(){
		String sql="SELECT * FROM ROOM_MST WHERE del_flg='0'";
		return template.query(sql,ROOM_ROW_MAPPER);
	}
}
