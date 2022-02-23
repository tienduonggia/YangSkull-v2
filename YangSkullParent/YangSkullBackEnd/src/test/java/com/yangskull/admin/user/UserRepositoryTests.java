package com.yangskull.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import com.yangskull.common.entity.Role;
import com.yangskull.common.entity.User;

import java.util.List;

//để kh hiện câu SQL khi chạy test
@DataJpaTest(showSql = false)
//mặc định nó sẽ sử dụng database memeory nhưng mình cần test trên real database
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {

	@Autowired
	private UserRepository repo;
	
	@Autowired
	private TestEntityManager entityManger;
	
	
	@Test
	public void testCreateUserWithOneRole() {
		//lấy dữ liệu từ table Role . với id là 1 (Admin)
		Role roleAdmin = entityManger.find(Role.class, 1);
		User userGTien = new User("howl1542@gmail.com","admin","Tien","Duong Gia");
		userGTien.addRole(roleAdmin);
		
		User savedUser = repo.save(userGTien);
		//KQ mình muốn trả về là id sẽ lớn hơn 0
		assertThat(savedUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateUserWithTwoRoles() {
		User userMHung = new User("hung@gmail.com","123","Hung","Nguyen Tran");
		Role roleEditor = entityManger.find(Role.class, 3);
		Role roleAssistant= entityManger.find(Role.class, 4);
		
		userMHung.addRole(roleEditor);
		userMHung.addRole(roleAssistant);
		User savedUser = repo.save(userMHung);
		assertThat(savedUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testQueryAllUsers()
	{
		Iterable<User> listUsers = repo.findAll();
		listUsers.forEach(System.out::println);
		assertThat(listUsers).isNotNull();
	}
	
	@Test
	public void testGetUserByID()
	{
		User user = repo.findById(8).get();
		System.out.println(user);
		assertThat(user).isNotNull();
	}
	
	@Test
	public void testUpdateUserDetails(){
		User user = repo.findById(8).get();
		user.setEnabled(true);
		user.setEmail("duonggiatien@gmail.com");
		repo.save(user);
	}
	
	@Test
	public void testUpdateUserRoles() {
		User user = repo.findById(9).get();
		Role roleEditor = entityManger.find(Role.class, 3);
		Role roleSalePerson = entityManger.find(Role.class, 2);
		
		user.getRoles().remove(roleEditor);
		user.addRole(roleSalePerson);
		
		repo.save(user);	
	}
	
	@Test
	public void testDeleteUser()
	{
		int userID = 2;
		repo.deleteById(userID);
	}
	
	@Test
	public void testGetUserByEmail()
	{
		String email = "abc@gmail.com";
		String email01 = "hung@gmail.com";
		User user = repo.getUserByEmail(email01);
				
		System.out.println(user); //null
		//assertThat(user).isNull();
	}
	
	@Test
	public void countByID()
	{
		int id = 122;
		Long countByID = repo.countById(id);
		
		System.out.println(countByID);
	}

	@Test
	public void testDisableUser(){
		int id = 8;
		repo.updateEnableStatus(id,false);
	}

	@Test
	public void testEnableUser(){
		int id = 8;
		 repo.updateEnableStatus(id,true);
	}

	//Test phân trang
	//Logic mình chỉ cần khai báo mỗi trang cần hiển thị bao nhiu
	@Test
	public void testListFirstPage()
	{
		int pageNumber = 2;
		int pageSize = 3;

		Pageable pageable = PageRequest.of(pageNumber,pageSize);
		Page<User> userPage = repo.findAll(pageable);

		List<User> userList = userPage.getContent();

		userList.forEach(System.out::println);

		assertThat(userList.size()).isEqualTo(pageSize);

	}

	@Test
	public void testFilterUser()
	{
		String keyword = "N";
		int pageNumber = 0;
		int pageSize = 3;
		Pageable pageable = PageRequest.of(pageNumber,pageSize);
		Page<User> userPage = repo.findAll(keyword,pageable);

		List<User> userList = userPage.getContent();

		userList.forEach(System.out::println);

		assertThat(userList.size()).isGreaterThan(0);
	}
	
}
