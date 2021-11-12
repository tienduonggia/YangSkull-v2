package com.hiucinema.common.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(length = 128, nullable = false, unique = true)
	private String email;
	
	@Column(length=64, nullable = false)
	private String password;
	
	@Column(name ="first_name",length=45, nullable = false)
	private String firstName;
	
	@Column(name ="last_name",length=45, nullable = false)
	private String lastName;
	
	@Column(length=64)
	private String photos;
	
	private boolean enabled;
	
	//Vì đây là quan hệ n - n , 1 user sẽ có nhiều role => sẽ có 1 list Role
	@ManyToMany
	@JoinTable(
				name="users_roles",
				joinColumns = @JoinColumn(name="user_id"),
				inverseJoinColumns = @JoinColumn(name="role_id")
			)
	private Set<Role> roles = new HashSet<>();




	public User(String email, String password, String firstName, String lastName) {
		super();
		this.email = email;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
	}



	public void addRole(Role role)
	{
		this.roles.add(role);
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", email=" + email + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", roles=" + roles + "]";
	}

	//lấy đường dẫn theo id người dùng
	@Transient
	public String getPhotosImagePath(){
		if(id == null || photos == null)
			return "/images/default-user.png";
		return "/user-photo/" + this.id + "/" + this.photos;
	}
}
