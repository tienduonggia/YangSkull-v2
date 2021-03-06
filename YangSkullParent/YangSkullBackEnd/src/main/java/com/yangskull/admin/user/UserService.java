package com.yangskull.admin.user;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.yangskull.common.entity.Role;
import com.yangskull.common.entity.User;

import javax.transaction.Transactional;


@Service
@Transactional
public class UserService {
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private RoleRepository roleRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	public static final int USER_PER_PAGE = 5;

	public User getByEmail(String email){
		return userRepo.getUserByEmail(email);
	}

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


	public Page<User> listByPage(int pageNumber, String sortField, String sortDir,String keyword){
		Sort sort = Sort.by(sortField);

		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

		Pageable pageable = PageRequest.of(pageNumber-1,USER_PER_PAGE,sort);
		if(keyword != null)
		{
			return userRepo.findAll(keyword,pageable);
		}
		return userRepo.findAll(pageable);
	}

	public User save(User user) {
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
		
			return userRepo.save(user);
	}


	public User updateAccount(User userInForm)
	{
		User userInDB = userRepo.findById(userInForm.getId()).get();

		if(!userInForm.getPassword().isEmpty()){
			userInDB.setPassword(userInForm.getPassword());
			encodePassword(userInDB);
		}
		if(userInForm.getPhotos() != null)
		{
			userInDB.setPhotos(userInForm.getPhotos());
		}

		userInDB.setFirstName(userInForm.getFirstName());
		userInDB.setLastName(userInForm.getLastName());

		return userRepo.save(userInDB);

	}

	////hàm Mã hóa mật khẩu user
	private void encodePassword(User user) {
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
	}

	//dùng cho bên restController
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
	
	//Mục đích dùng countbyID vì mình chỉ muốn biết ng đó có tồn tại kh thôi => còn find by id thì sẽ lấy hết tất cả các thông tin 
	public void delete(Integer id) throws UserNotFoundException
	{
		Long coutByID = userRepo.countById(id);
		if(coutByID == null || coutByID == 0)
		{
			throw new UserNotFoundException("Could not find any user with ID "+id);
		}
		
		userRepo.deleteById(id);
	}

	public void updateEnableStatusUser(Integer id, boolean enable){
		userRepo.updateEnableStatus(id,enable);
	}


}
