package com.example.domain;


import java.io.Serializable;

import lombok.Data;

@Data
public class User implements Serializable{
	private Integer userSeqNo;
	private String userId;
	private String userName;
	private String email;
	private String password;
	private String authority;
}
