package com.hiucinema.admin.user;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hiucinema.common.entity.Role;
import com.hiucinema.common.entity.User;


@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private RoleRepository roleRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	//Sử dụng JPA để tự động query lấy hết dữ liệu Users
	public List<User> listAll()
	{
		return (List<User>) userRepo.findAll();
	}
	
	//Sử dụng JPA để tự động query lấy hết dữ liệu Roles
	public List<Role> listRoles()
	{
		return (List<Role>) roleRepo.findAll();
	}

	public void save(User user) {
		//Gọi hàm mã hóa 
		
		//Nếu mà trong updateMode => admin bỏ trống mật khẩu => nghĩ là giữ nguyên => còn nếu nhập là thay đổi
		boolean updateMode = user.getId() != null;
		if(updateMode)
		{
			User existingUser = userRepo.findById(user.getId()).get();
			
			if(user.getPassword().isEmpty()) {
				user.setPassword(existingUser.getPassword());
			}else {
				encodePassword(user);
			}
		}else {
			encodePassword(user);
		}
		
			userRepo.save(user);
		
		
	}
	////hàm Mã hóa mật khẩu user
	private void encodePassword(User user) {
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
	}
	
	public boolean isEmailUnique(Integer id,String email)
	{
		User userByEmail =	userRepo.getUserByEmail(email);
		boolean isCreateNew = (id == null);

		if(isCreateNew){
			if(userByEmail == null)
				return true;
			else
				return false;
		}else{
			//bc này sẽ cho chúng ta update với đúng cái email hiện tại => để kh báo lỗi là email này đã có ng xài
			if(userByEmail.getId() != id)
				return false;
			else
				return true;
		}
	}

	//Lấy Data user với ID
	public User get(Integer id) throws UserNotFoundException {
		try {
			return userRepo.findById(id).get();
		}catch (NoSuchElementException ex) {
			throw new UserNotFoundException("Could not find any user with ID " + id);
		}
		
	}
}
