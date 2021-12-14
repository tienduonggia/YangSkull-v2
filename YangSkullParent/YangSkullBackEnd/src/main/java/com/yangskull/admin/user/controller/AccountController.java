package com.yangskull.admin.user.controller;

import com.yangskull.admin.security.YangSkullUserDetails;
import com.yangskull.admin.user.UserService;
import com.yangskull.admin.utils.FileUploadUtil;
import com.yangskull.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
public class AccountController {

    @Autowired
    private UserService userService;

    @GetMapping("/account")
    public String viewDetails(@AuthenticationPrincipal YangSkullUserDetails loggedUser,
            Model model)
    {
        String email = loggedUser.getUsername();
        User user = userService.getByEmail(email);
        model.addAttribute("user",user);
        model.addAttribute("pageTitle", "Profile update");
        return "users/account_form";
    }

    @PostMapping("/account/update")
    public String updateAccount(User user, RedirectAttributes redicAttributes,
                                @RequestParam("image") MultipartFile multipartFile,
                                @AuthenticationPrincipal YangSkullUserDetails userDetails) throws IOException {
        System.out.println(user);
        System.out.println(multipartFile.getOriginalFilename());
        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            user.setPhotos(fileName);
            User savedUser = userService.updateAccount(user);
            String uploadDir = "user-photo/" + savedUser.getId();
            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        } else {
            if (user.getPhotos().isEmpty()) {
                user.setPhotos(null);
            }
            userService.updateAccount(user);

            userDetails.setFirstName(user.getFirstName());
            userDetails.setLastName(user.getLastName());
        }

        //userService.save(user);
        redicAttributes.addFlashAttribute("message", "The user has been saved successfully !");

        return "redirect:/account";
        // return "redirect:/users";
    }
}
