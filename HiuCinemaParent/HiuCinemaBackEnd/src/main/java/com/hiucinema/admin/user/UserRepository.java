package com.hiucinema.admin.user;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.hiucinema.common.entity.User;


//Tầng này là Repository để nói chuyện , tương tác với DATAbASE
public interface UserRepository extends CrudRepository<User, Integer> {
	//CrudReposiroty này sẽ cung câp những hàm cần thiết vd như Slect all , find by id ,....List<User> findByEmail(String email);
	//Nếu kh có mình sẽ tự custom câu query như ngày xưa 
	
	
	//Biến param ở dưới sẽ là tham số truyền vào câu query 
	//Ngoài ra có 1 cách là dùng ?1 , ?2 , .... ở dưới chỉ cần String ..., int ... là tự map theo số thứ tự
	@Query("SELECT u FROM User u WHERE u.email = :email")
	public User getUserByEmail(@Param("email") String email);
}
