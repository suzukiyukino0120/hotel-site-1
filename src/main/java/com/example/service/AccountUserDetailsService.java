package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.domain.User;
import com.example.form.CreateAccountForm;
import com.example.repository.UserMstRepository;

@Service
public class AccountUserDetailsService implements UserDetailsService {
	@Autowired
	UserMstRepository userMstRepository;
	
	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		if (userId == null || "".equals(userId)) {
            throw new UsernameNotFoundException(userId + "is not found");
        }
		
		try {
            User user = userMstRepository.selectByUserId(userId);

            if (user != null) {
                return new AccountUserDetails(user);

            } else {
                throw new UsernameNotFoundException(userId + "is not found");
            }

        } catch (EmptyResultDataAccessException e) {
            throw new UsernameNotFoundException(userId + "is not found");
        }
	}
	
}
