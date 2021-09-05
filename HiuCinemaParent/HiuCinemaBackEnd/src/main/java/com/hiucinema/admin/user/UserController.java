package com.hiucinema.admin.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hiucinema.common.entity.Role;
import com.hiucinema.common.entity.User;



@Controller
@RequestMapping("/users")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping("")
	public String listAll(Model model)
	{
		List<User> listUser =  userService.listAll();
		model.addAttribute("listUsers", listUser);
		return "ListUser";
	}
	
	@GetMapping("/new")
	public String newUser(Model model)
	{
		User user = new User();
		List<Role> listRoles = userService.listRoles();
		model.addAttribute("user",user);
		model.addAttribute("listRoles",listRoles);
		model.addAttribute("pageTitle","Create New User");
		return "User_Form";
	}
	
	//Đầu tiền mình sẽ get ID user sau đó tìm , nếu kh có id đó thì sẽ zô phần catch => lỗi exception mình đã code ở Service => trả về modal bên view
	@GetMapping("/edit/{id}")
	public String editUser(@PathVariable(name = "id") Integer id,
			RedirectAttributes redicAttributes,
			Model model)
	{
		try {
			User user = userService.get(id);
			List<Role> listRoles = userService.listRoles();
	
			model.addAttribute("listRoles",listRoles);
			model.addAttribute("user",user);
			model.addAttribute("pageTitle","Edit User");
			return "User_Form";
		}catch (UserNotFoundException e) {
			redicAttributes.addFlashAttribute("message",e.getMessage());
			return "redirect:/users";
		}
		
	}
	
	@PostMapping("/save")
	public String saveUser(User user, RedirectAttributes redicAttributes)
	{
		System.out.println(user);
		userService.save(user);
		redicAttributes.addFlashAttribute("message","The user has been saved successfully !");
		return "redirect:/users";
	}
	
}
