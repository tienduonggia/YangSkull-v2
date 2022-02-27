package com.yangskull.admin.category;

import com.yangskull.admin.user.UserNotFoundException;
import com.yangskull.admin.utils.FileUploadUtil;
import com.yangskull.common.entity.Category;
import com.yangskull.common.entity.Role;
import com.yangskull.common.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    CategoryService categoryService;

    @GetMapping
    public String listAll(Model model) {
        List<Category> categoryList = categoryService.listAll();

        model.addAttribute("listCategory", categoryList);
        return "categories/ListCategory";
    }

    @GetMapping("/new")
    public String newCategory(Model model) {
        Category category = new Category();
        List<Category> listCategoriesUsedInForm = categoryService.listCategoriesUsedInForm();
        model.addAttribute("category", category);
        model.addAttribute("listCategory", listCategoriesUsedInForm);
        model.addAttribute("pageTitle", "Create New Category");
        return "categories/Category_Form";
    }

    //Đầu tiền mình sẽ get ID user sau đó tìm , nếu kh có id đó thì sẽ zô phần catch => lỗi exception mình đã code ở Service => trả về modal bên view
    @GetMapping("/edit/{id}")
    public String editCategory(@PathVariable(name = "id") Integer id,
                               RedirectAttributes redicAttributes,
                               Model model) {
        try {
            Category category = categoryService.get(id);
            List<Category> listCategoriesUsedInForm = categoryService.listCategoriesUsedInForm();

            model.addAttribute("listCategory", listCategoriesUsedInForm);
            model.addAttribute("category", category);
            model.addAttribute("pageTitle", "Edit Category");
            return "categories/Category_Form";
        } catch (CategoryNotFoundException e) {
            redicAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/categories";
        }
    }

    @PostMapping("/save")
    public String saveCategory(Category category,
                               @RequestParam("fileImage") MultipartFile multipartlFile,
                               RedirectAttributes redicAttributes) throws IOException {
        Category savedCategory = null;

        if (!multipartlFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartlFile.getOriginalFilename());
            //set imageName to category to DB
            category.setImage(fileName);
            savedCategory = categoryService.save(category);

            //create Folder contain category images
            String uploadDir = "../category-images/" + savedCategory.getId();
            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartlFile);

        } else {
            savedCategory = categoryService.save(category);
        }
        String message = "Category " + savedCategory.getName() + " has been saved successfully";
        redicAttributes.addFlashAttribute("message", message);

        return "redirect:/categories";
    }

}
