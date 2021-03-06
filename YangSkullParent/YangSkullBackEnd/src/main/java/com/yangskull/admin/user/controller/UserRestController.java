package com.yangskull.admin.user.controller;

import com.yangskull.admin.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users/")
public class UserRestController {
	@Autowired
	private UserService service;
	
	//Mục đích sử dụng restController là vì khi bị trùng email, => chỉ trả về kết quả chứ kh cần view
	
	
	@PostMapping("check_email")
	public String checkDubplicateEmail(@Param("id") Integer id,@Param("email") String email)
	{ 
		return service.isEmailUnique(id,email) ? "OK" : "Duplicated";
	}
}
