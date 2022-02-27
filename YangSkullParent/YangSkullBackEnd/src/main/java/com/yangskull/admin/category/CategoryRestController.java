package com.yangskull.admin.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categories/")
public class CategoryRestController {

    @Autowired
    CategoryService categoryService;

    @PostMapping("check_unique")
    public String checkUnique(@Param("id") Integer id,
                              @Param("name") String name,
                              @Param("alias") String alias){
        return categoryService.checkUnique(id,name,alias);
    }
}
