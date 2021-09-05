package com.hiucinema.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderTests {
		@Test
		public void testEncodePassword()
		{
			//Test BCrypt from Spring security
			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			String pw = "GiaTien";
			String encodePw = passwordEncoder.encode(pw);
			System.out.println(encodePw);
			
			//Test matching passOriginal and encodePass 
			boolean matches = passwordEncoder.matches(pw, encodePw);
		//	System.out.println(matches);
			assertThat(matches).isTrue();
					
		}
}
