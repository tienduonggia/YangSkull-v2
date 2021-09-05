package com.hiucinema.admin.user;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.hiucinema.common.entity.Role;
import com.hiucinema.common.entity.User;

@DataJpaTest
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
		listUsers.forEach(user -> System.out.println(user));
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
		int userID = 9;
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
	
}
