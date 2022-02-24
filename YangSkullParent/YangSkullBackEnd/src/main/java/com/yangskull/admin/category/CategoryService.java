package com.yangskull.admin.category;

import com.yangskull.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    public List<Category> listAll(){
        return (List<Category>) categoryRepository.findAll();
    }

}
