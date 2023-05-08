package com.example.repository;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.domain.Code;

@Repository
public class CodeMstRepository {
	@Autowired
	private NamedParameterJdbcTemplate template;
	
	private static final RowMapper<Code> CODE_ROW_MAPPER
	= (rs,i) -> {
		Code code = new Code();
		code.setGroupCode(rs.getString("group_code"));
		code.setCodeName(rs.getString("code_name"));
		code.setCode(rs.getString("code"));		
		code.setSortNumber(rs.getInt("sort_number"));
		return code;
	};
	
	public List<Code> selectByGroupCode(String groupCode) {
		String sql = "SELECT group_code, code_name, code, sort_number FROM CODE_MST WHERE group_code = :groupCode  AND del_flg='0' ORDER BY sort_number;";
		SqlParameterSource param = new MapSqlParameterSource().addValue("groupCode", groupCode);
		return template.query(sql,param,CODE_ROW_MAPPER);
	}
}
