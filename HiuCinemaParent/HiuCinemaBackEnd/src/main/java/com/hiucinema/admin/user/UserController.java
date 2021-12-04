package com.hiucinema.admin.user;

import java.io.File;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.hiucinema.admin.utils.FileUploadUtil;
import org.apache.tomcat.util.descriptor.web.MultipartDef;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hiucinema.common.entity.Role;
import com.hiucinema.common.entity.User;

import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;


@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

//      @GetMapping("")
//    public String listAll(Model model) {
//        List<User> listUser = userService.listAll();
//        model.addAttribute("listUsers", listUser);
//        return "ListUser";
//    }

    @GetMapping("")
    public String listAll(Model model) {
        return listByPage(1, model,"id","asc",null);
    }

    @GetMapping("/page/{pageNumber}")
    public String listByPage(@PathVariable("pageNumber") int pageNumber, Model model,
                             @Param("sortField") String sortField ,
                             @Param("sortDir") String sortDir,
                            @Param("keyword") String keyword) {
        Page<User> userPage = null;
       try{
           userPage = userService.listByPage(pageNumber,sortField,sortDir,keyword);
           if(pageNumber > userPage.getTotalPages()){
               userPage = userService.listByPage(userPage.getTotalPages(),sortField,sortDir,keyword);
           }
       }catch (IllegalArgumentException ex)
       {
               pageNumber = 1;
               userPage = userService.listByPage(pageNumber,sortField,sortDir,keyword);
       }


        List<User> listUser = userPage.getContent();

        long startCount = (long) (pageNumber - 1) * UserService.USER_PER_PAGE + 1;
        long endCount = startCount + UserService.USER_PER_PAGE - 1;
        if (endCount > userPage.getTotalElements()) {
            endCount = userPage.getTotalElements();
        }
        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("totalPage", userPage.getTotalPages());
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalItems", userPage.getTotalElements());
        model.addAttribute("listUsers", listUser);
        model.addAttribute("reverseSortDir", reverseSortDir);
        model.addAttribute("keyword", keyword);
        return "ListUser";
    }

    @GetMapping("/new")
    public String newUser(Model model) {
        User user = new User();
        List<Role> listRoles = userService.listRoles();
        model.addAttribute("user", user);
        model.addAttribute("listRoles", listRoles);
        model.addAttribute("pageTitle", "Create New User");
        return "User_Form";
    }

    //Đầu tiền mình sẽ get ID user sau đó tìm , nếu kh có id đó thì sẽ zô phần catch => lỗi exception mình đã code ở Service => trả về modal bên view
    @GetMapping("/edit/{id}")
    public String editUser(@PathVariable(name = "id") Integer id,
                           RedirectAttributes redicAttributes,
                           Model model) {
        try {
            User user = userService.get(id);
            List<Role> listRoles = userService.listRoles();

            model.addAttribute("listRoles", listRoles);
            model.addAttribute("user", user);
            model.addAttribute("pageTitle", "Edit User");
            return "User_Form";
        } catch (UserNotFoundException e) {
            redicAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/users";
        }

    }


    @RequestMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id,
                             RedirectAttributes redicAttributes) {
        try {
            String uploadDir = "user-photo/" + id;
            Path uploadPath = Paths.get(uploadDir);
            File file = new File(uploadPath.toString());
            FileUploadUtil.cleanDir(uploadDir);
            file.delete();

            userService.delete(id);
            redicAttributes.addFlashAttribute("message", "The user ID " + id + " has been deleted successfully");
        } catch (UserNotFoundException e) {
            redicAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/users";
    }

    @PostMapping("/save")
    public String saveUser(User user, RedirectAttributes redicAttributes, @RequestParam("image") MultipartFile multipartFile) throws IOException {
        System.out.println(user);
        System.out.println(multipartFile.getOriginalFilename());
        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            user.setPhotos(fileName);
            User savedUser = userService.save(user);
            String uploadDir = "user-photo/" + savedUser.getId();
            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        } else {
            if (user.getPhotos().isEmpty()) {
                user.setPhotos(null);
            }
            userService.save(user);
        }

        //userService.save(user);
        redicAttributes.addFlashAttribute("message", "The user has been saved successfully !");

        String firstPartOfEmail = user.getEmail().split("@")[0];
        return "redirect:/users/page/1?sortField=id&sortDir=asc&keyword=" + firstPartOfEmail;
       // return "redirect:/users";
    }

    @GetMapping("/{id}/enabled/{status}")
    public String updateEnableStatusUser(@PathVariable("id") Integer id,
                                         @PathVariable("status") boolean status,
                                         RedirectAttributes redicAttributes) {
        userService.updateEnableStatusUser(id, status);
        String enable = status ? "enable" : "disable";
        String message = "The user ID " + id + " has been " + enable;
        redicAttributes.addFlashAttribute("message", message);
        return "redirect:/users";
    }

    /*Export to excel*/
    @GetMapping("/export/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        List<User> listUser = userService.listAll();

        UserExelExporter exporter = new UserExelExporter();
        exporter.export(listUser,response);
    }


    /*Export to pdf*/
    @GetMapping("/export/pdf")
    public void exportToPdf(HttpServletResponse response) throws IOException {
        List<User> listUser = userService.listAll();

        UserPDFExporter exporter = new UserPDFExporter();
        exporter.export(listUser,response);
    }

}
