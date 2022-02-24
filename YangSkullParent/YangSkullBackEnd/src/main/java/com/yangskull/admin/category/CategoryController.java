package com.yangskull.admin.category;

import com.yangskull.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @GetMapping
    public String listAll(Model model){
        List<Category> categoryList = categoryService.listAll();

        model.addAttribute("listCategory",categoryList);
        return "categories/ListCategory";
    }

}
