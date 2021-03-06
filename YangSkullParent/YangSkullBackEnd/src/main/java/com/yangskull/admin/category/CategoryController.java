package com.yangskull.admin.category;

import com.yangskull.admin.category.export.CategoryCsvExporter;
import com.yangskull.admin.user.UserNotFoundException;
import com.yangskull.admin.user.UserService;
import com.yangskull.admin.utils.FileUploadUtil;
import com.yangskull.common.entity.Category;
import com.yangskull.common.entity.Role;
import com.yangskull.common.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
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

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    CategoryService categoryService;

    @GetMapping
    public String listFirstPage(@Param("sortDir") String sortDir,
                          Model model) {
        return listByPage(1,model ,sortDir, null);
    }

    @GetMapping("/page/{pageNum}")
    public String listByPage(@PathVariable("pageNum") int pageNumber,
                             Model model,
                             @Param("sortDir") String sortDir,
                             @Param("keyword") String keyword
                             ) {
        if (org.apache.commons.lang3.StringUtils.isBlank(sortDir)) {
            sortDir = "asc";
        }

        CategoryPageInfo categoryPageInfo = new CategoryPageInfo();
        List<Category> categoryList = categoryService.listByPage(categoryPageInfo, pageNumber, sortDir, keyword);

        long startCount = (long) (pageNumber - 1) * CategoryService.ROOT_CATEGORIES_PER_PAGE + 1;
        long endCount = startCount + CategoryService.ROOT_CATEGORIES_PER_PAGE - 1;
        if (endCount > categoryPageInfo.getTotalElements()) {
            endCount = categoryPageInfo.getTotalElements();
        }


        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";


        model.addAttribute("totalElement",categoryPageInfo.getTotalElements());
        model.addAttribute("totalPage",categoryPageInfo.getTotalPages());
        model.addAttribute("currentPage",pageNumber);
        model.addAttribute("sortField","name");
        model.addAttribute("sortDir",sortDir);
        model.addAttribute("keyword",keyword);
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);

        model.addAttribute("listCategory", categoryList);
        model.addAttribute("reverseSortDir", reverseSortDir);
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

    //?????u ti???n m??nh s??? get ID user sau ???? t??m , n???u kh c?? id ???? th?? s??? z?? ph???n catch => l???i exception m??nh ???? code ??? Service => tr??? v??? modal b??n view
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

    @GetMapping("/{id}/enabled/{status}")
    public String updateEnableStatusCategory(@PathVariable("id") Integer id,
                                             @PathVariable("status") boolean status,
                                             RedirectAttributes redicAttributes) {
        categoryService.updateEnabledCategory(id, status);
        String enable = status ? "enable" : "disable";
        String message = "The catgeory ID " + id + " has been " + enable;
        redicAttributes.addFlashAttribute("message", message);
        return "redirect:/categories";
    }

    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable(name = "id") Integer id,
                                 RedirectAttributes redirectAttributes) throws CategoryNotFoundException {

        try {
            categoryService.delete(id);

            String categoryDir = "../category-images/" + id;
            FileUploadUtil.removeDir(categoryDir);
        } catch (CategoryNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
        }
        redirectAttributes.addFlashAttribute("message", "The category ID " + id + " has been deleted successfully");
        return"redirect:/categories";
    }

    @GetMapping("/categories/export/csv")
    public void exportToCSV(HttpServletResponse response) throws IOException {
        List<Category> listCategories = categoryService.listCategoriesUsedInForm();
        CategoryCsvExporter exporter = new CategoryCsvExporter();
        exporter.export(listCategories, response);
    }


}
