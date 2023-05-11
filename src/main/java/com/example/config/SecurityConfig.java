package com.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.service.AccountUserDetailsService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
    AccountUserDetailsService userDetailsService;
	
	@Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/img/**", "/css/**", "/js/**", "/webjars/**");
    }
	
	@Bean
	public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 認可の設定
    	http.authorizeRequests()
            .antMatchers("/loginForm","/plan/**","/account/**").permitAll()  // ログインフォーム、プラン検索画面、ユーザー登録は全員許可
            .anyRequest().authenticated();          // それ以外は、認証を求める

        // ログイン設定
        http.formLogin()
                .loginPage("/loginForm")                //ログインフォームを表示するパス
                .loginProcessingUrl("/authenticate")    //フォーム認証処理のパス
                .usernameParameter("userId")          //ユーザ名のリクエストパラメータ名
                .passwordParameter("password")          //パスワードのリクエストパラメータ名
                .defaultSuccessUrl("/logined")             //認証成功時に遷移するデフォルトのパス
                .failureUrl("/loginForm?error=true");

        // ログアウト設定
        http.logout()
        		.logoutUrl("/logout")
                .logoutSuccessUrl("/home")         //ログアウト成功時に遷移するパス
                .permitAll();                           //全ユーザに対してアクセスを許可
        
        http.exceptionHandling()
    	.accessDeniedPage("/accessDeniedPage");//アクセス拒否された時に遷移するパス
    }

}
