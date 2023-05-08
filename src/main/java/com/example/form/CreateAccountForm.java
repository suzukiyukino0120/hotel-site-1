package com.example.form;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.example.validator.UserIdExists;

import lombok.Data;

@Data
public class CreateAccountForm {
	@NotBlank(message="ユーザーIDを入力してください")
	@Pattern(regexp="^([a-zA-Z0-9._-]{3,15})$", message="アカウントIDは3～15文字以内で入力してください。使用できる文字：　a-zA-Z0-9_-.")
	@UserIdExists
	private String userId;
	@NotBlank(message="氏名を入力してください")
	private String userName;
	@NotBlank(message="メールアドレスを入力してください")
	@Email(message="メールアドレスの形式が不正です")
	private String email;
	@NotBlank(message="パスワードを入力してください")
	@Pattern(regexp="^(?=.*?[0-9])(?=.*?[a-z])(?=.*?[A-Z])[0-9a-zA-Z]{8,16}$", message="パスワードは半角英数字を混在させて8～16文字以内で入力してください")
	private String password;
	@NotBlank(message="確認用パスワードを入力してください")
	private String confirmPassword;
	@NotBlank(message="権限を選択してください")
	private String authority;
	private Integer createUser;
	private Integer updateUser;
	
	@AssertTrue(message = "パスワードと確認用パスワードが一致しません")
    public boolean isPasswordValid() {
        if (password == null || password.isEmpty()) {
            return true;
        }
        return password.equals(confirmPassword);
    } 
}
