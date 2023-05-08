package com.example.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.domain.Reservation;
import com.example.domain.User;
import com.example.form.CreateAccountForm;

@Repository
public class UserMstRepository {
	@Autowired
	private NamedParameterJdbcTemplate template;
	
	public static final RowMapper<User> USER_ROW_MAPPER=
			(rs,i) -> {
				User user = new User();
				user.setUserSeqNo(rs.getInt("user_seq_no"));
				user.setUserId(rs.getString("user_id"));
				user.setUserName(rs.getString("user_name"));
				user.setEmail(rs.getString("email"));
				user.setPassword(rs.getString("password"));
				user.setAuthority(rs.getString("authority"));
				return user;
			};
			
	public User selectByUserId(String userId) {
		String sql="SELECT user_seq_no,user_id,user_name,email,password,authority FROM USER_MST WHERE user_id=:userId AND del_flg='0'";
		SqlParameterSource param = new MapSqlParameterSource().addValue("userId",userId);
		List<User> list =template.query(sql, param, USER_ROW_MAPPER);
		if(list.size()==0) {
			return null;
		}else {
			return list.get(0);
		}
	}
	
	public void insert(CreateAccountForm createAccountForm) {
		String sql="INSERT INTO USER_MST (user_id, user_name, email, password, authority, create_user, create_date, update_user, update_date, del_flg) "
				+"VALUES(:userId, :userName, :email, :password, :authority, :createUser, CURRENT_DATE, :updateUser, CURRENT_DATE, '0')";
		SqlParameterSource param = new BeanPropertySqlParameterSource(createAccountForm);
		template.update(sql, param);
	}

}
