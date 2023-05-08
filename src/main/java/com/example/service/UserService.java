package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.User;
import com.example.form.CreateAccountForm;
import com.example.repository.UserMstRepository;

@Service
@Transactional
public class UserService {
	@Autowired
	UserMstRepository userMstRepository;
	
	@Autowired
	PasswordEncoder passwordEncode;
	
	public User searchByUserId(String userId) {
		return userMstRepository.selectByUserId(userId);
	}
	
	public void createAccount(CreateAccountForm createAccountForm) {
		createAccountForm.setPassword(passwordEncode.encode(createAccountForm.getPassword()));
		userMstRepository.insert(createAccountForm);
	}

}
