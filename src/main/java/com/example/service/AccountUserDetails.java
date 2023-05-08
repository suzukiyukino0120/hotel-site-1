package com.example.service;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.domain.User;

public class AccountUserDetails implements UserDetails {

	private final User user;
	
	public AccountUserDetails(User user) {
		this.user = user;
	}
	
	public User getUser() {
        return user;
    }

    public String getName() {
        return this.user.getUserName();
    }
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		if(this.user.getAuthority().equals("S")) {
			return AuthorityUtils.createAuthorityList("ROLE_S","ROLE_SA");
		}else {
			return AuthorityUtils.createAuthorityList("ROLE_A","ROLE_SA");
		}
	}

	@Override
	public String getPassword() {
		return this.user.getPassword();
	}

	@Override//ユーザーID
	public String getUsername() {
		return this.user.getUserId().toString();
	}
	
	//ユーザー連番
	public Integer getUserSeqNo() {
		return this.user.getUserSeqNo();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
