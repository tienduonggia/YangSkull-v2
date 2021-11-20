package com.hiucinema.admin.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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


	//này chỉ sử dụng cho 1 trường hợp thôi, đặt 1 trường hợp khác
	//Bạn có ng dùng có họ và tên , 2 cột riêng => user tìm cả họ và tên theo cách này sẽ kh đc
//	@Query("SELECT u FROM User u WHERE u.firstName LIKE %?1% " +
//										"OR u.lastName LIKE %?1% " +
//										"OR u.email LIKE %?1%")
//	public Page<User> findAll(String keyword, Pageable pageable);

	//Mình sẽ concat để nối chuỗi lại
	@Query("SELECT u FROM User u WHERE CONCAT(u.id, ' ',u.email, ' ',u.firstName, ' ',u.lastName) LIKE %?1% ")
	public Page<User> findAll(String keyword, Pageable pageable);

	@Query("UPDATE User u SET u.enabled = ?2 WHERE u.id =?1")
	@Modifying
	public void updateEnableStatus(Integer id, boolean enabled);
}
