package com.yangskull.admin.category;

import com.yangskull.common.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

//để kh hiện câu SQL khi chạy test
@DataJpaTest(showSql = false)
//mặc định nó sẽ sử dụng database memeory nhưng mình cần test trên real database
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository repo;

    //Test create root category
    @Test
    public void testCreateRootCategory() {
        Category cate = new Category("Beer");
        Category savedrepo = repo.save(cate);

        assertThat(savedrepo.getId()).isGreaterThan(0);
    }

    //Test create sub category
    @Test
    public void testCreateSubCategory() {
        Category parent = new Category(2);
        Category subCategory = new Category("Whisky", parent);
        Category savedrepo = repo.save(subCategory);

        assertThat(savedrepo.getId()).isGreaterThan(0);
    }

    @Test
    public void testUpdateCategory() {
        int id = 1;
        Category cate = repo.findById(1).get();
        Category parentCate = new Category(2);
        cate.setParent(parentCate);
        repo.save(cate);
    }

    @Test
    public void testGetRootAndSubCategoryByID() {
        Category category = repo.findById(2).get();
        System.out.println(category.getName());

        Set<Category> setCategorySet = category.getChildren();
        for (Category chidrent : setCategorySet) {
            System.out.println("    " + chidrent.getName());
        }
    }

    @Test
    public void testPrintHerarchicalCategiries() {
        Iterable<Category> categories = repo.findAll();
        for (Category cate : categories) {
            if (Objects.isNull(cate.getParent())) {
                System.out.println(cate.getName());
                testPrintChildCategory(cate,0);


            }
        }
    }

    //Sử dụng đệ quy để gọi chính nó
    //in ra gia phả của Category
    //Example: Sprint
    //         -- Whisky
    //         -- Rum
    //         ---- Another...
    public void testPrintChildCategory(Category parent, int subLevel) {
        StringBuilder sb = new StringBuilder("--");
        int newSubLevel = subLevel + 1;
        Set<Category> setCategorySet = parent.getChildren();
        for(Category subCategory : setCategorySet){
            for(int i = 0 ; i < newSubLevel; i++)
            {

                System.out.print(sb);
            }

            System.out.println(subCategory.getName());
            testPrintChildCategory(subCategory,newSubLevel);
        }
    }

    @Test
    public void testListRootCateries()
    {
        List<Category> list = repo.findRootCategories(Sort.by("name").ascending());
        list.forEach(category -> System.out.println(category.getName()));
    }

    @Test
    public void testFindByName(){
        Category category = repo.findByName("Whisky");
        System.out.println(category);
    }

    @Test
    public void testFindByAlias(){
        Category category = repo.findByAlias("brandy");
        System.out.println(category);
    }


}