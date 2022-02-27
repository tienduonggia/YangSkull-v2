package com.yangskull.admin.category;


import com.yangskull.common.entity.Category;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class CategoryServiceTests {

    //mock , fake object repo
    @MockBean
    private CategoryRepository categoryRepository;

    //inject vào service
    @InjectMocks
    private CategoryService categoryService;


    @Test
    public void TestCheckUniqueInNewModeReturnDuplicateName(){
        Integer id = null;
        String name = "Whisky";
        String alias = "demo";
        Category category = new Category(id, name, alias);
        Mockito.when(categoryRepository.findByName(name)).thenReturn(category);

       String error =  categoryService.checkUnique(id,name,alias);
        System.out.println(error);
        assertThat(error).isNotEmpty();
        assertThat(error).isEqualTo("DuplicateName");
    }


    @Test
    public void TestCheckUniqueInNewModeReturnDuplicateAlias(){
        Integer id = null;
        String name = "ABC";
        String alias = "brandy";
        Category category = new Category(id, name, alias);
        Mockito.when(categoryRepository.findByAlias(alias)).thenReturn(category);

        String error =  categoryService.checkUnique(id,name,alias);
        System.out.println(error);
        assertThat(error).isNotEmpty();
        assertThat(error).isEqualTo("DuplicateAlias");
    }


    @Test
    public void TestCheckUniqueInNewModeReturnOK(){
        Integer id = null;
        String name = "ABC";
        String alias = "XYZ";
        Category category = new Category(id, name, alias);
        Mockito.when(categoryRepository.findByAlias(alias)).thenReturn(null);
        Mockito.when(categoryRepository.findByName(alias)).thenReturn(null);

        String error =  categoryService.checkUnique(id,name,alias);
        System.out.println(error);
        assertThat(error).isNotEmpty();
        assertThat(error).isEqualTo("OK");
    }


    @Test
    public void TestCheckUniqueInEditModeReturnDuplicateName(){
        Integer id = 1;
        String name = "Whisky";
        String alias = "demo";
        Category category = new Category(2, name, alias);
        Mockito.when(categoryRepository.findByName(name)).thenReturn(category);

        String error =  categoryService.checkUnique(id,name,alias);
        System.out.println(error);
        assertThat(error).isNotEmpty();
        assertThat(error).isEqualTo("DuplicateName");
    }

    @Test
    public void TestCheckUniqueInEditModeReturnDuplicateAlias(){
        Integer id = 1;
        String name = "ABC";
        String alias = "brandy";
        Category category = new Category(2, name, alias);
        Mockito.when(categoryRepository.findByAlias(alias)).thenReturn(category);

        String error =  categoryService.checkUnique(id,name,alias);
        System.out.println(error);
        assertThat(error).isNotEmpty();
        assertThat(error).isEqualTo("DuplicateAlias");
    }

    @Test
    public void TestCheckUniqueInNEditModeReturnOK(){
        Integer id = 2;
        String name = "ABC";
        String alias = "XYZ";
        Category category = new Category(id, name, alias);
        Mockito.when(categoryRepository.findByAlias(alias)).thenReturn(null);
        Mockito.when(categoryRepository.findByName(alias)).thenReturn(null);

        String error =  categoryService.checkUnique(id,name,alias);
        System.out.println(error);
        assertThat(error).isNotEmpty();
        assertThat(error).isEqualTo("OK");
    }
}
