package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.form.CreateAccountForm;
import com.example.service.AccountUserDetails;
import com.example.service.UserService;

@Controller
@RequestMapping("/account")
public class AccountController {
	@ModelAttribute
	public CreateAccountForm setUpCreateAccountForm() {
		return new CreateAccountForm();
	}
	
	@Autowired
	UserService userService;
	
	@RequestMapping("/")
	public String createAccountView() {
		return "create_account";
	}

	@RequestMapping("/create")
	public String createAccount(@AuthenticationPrincipal AccountUserDetails accountUserDetails, @Validated CreateAccountForm createAccountForm, BindingResult result, Model model) {
		if(result.hasErrors()) {
			return "create_account";
		}
		if(accountUserDetails!=null) {
			createAccountForm.setCreateUser(accountUserDetails.getUserSeqNo());
			createAccountForm.setUpdateUser(accountUserDetails.getUserSeqNo());
		}else {
			createAccountForm.setCreateUser(0);
			createAccountForm.setUpdateUser(0);
		}
		userService.createAccount(createAccountForm);
		return "redirect:/account/complete";
	}
	
	@RequestMapping("/complete")
	public String completeAccountView() {
		return "complete_account";
	}
}
