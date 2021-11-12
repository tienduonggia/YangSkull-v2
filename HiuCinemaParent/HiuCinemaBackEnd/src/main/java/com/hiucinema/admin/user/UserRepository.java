package com.hiucinema.admin.user;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.hiucinema.common.entity.User;
import org.springframework.stereotype.Repository;


//Tầng này là Repository để nói chuyện , tương tác với DATAbASE
@Repository

public interface UserRepository extends PagingAndSortingRepository<User, Integer> {
	//CrudReposiroty này sẽ cung câp những hàm cần thiết vd như Slect all , find by id ,....List<User> findByEmail(String email);
	//Nếu kh có mình sẽ tự custom câu query như ngày xưa 
	
	
	//Biến param ở dưới sẽ là tham số truyền vào câu query 
	//Ngoài ra có 1 cách là dùng ?1 , ?2 , .... ở dưới chỉ cần String ..., int ... là tự map theo số thứ tự
	@Query("SELECT u FROM User u WHERE u.email = :email")
	public User getUserByEmail(@Param("email") String email);
	
	public Long countById(Integer id);

	@Query("UPDATE User u SET u.enabled = ?2 WHERE u.id =?1")
	@Modifying
	public void updateEnableStatus(Integer id, boolean enabled);
}
