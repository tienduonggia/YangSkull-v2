package com.yangskull.admin.brand;

import com.yangskull.admin.category.CategoryRepository;
import com.yangskull.common.entity.Brand;
import com.yangskull.common.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

//để kh hiện câu SQL khi chạy test
@DataJpaTest(showSql = true)
//mặc định nó sẽ sử dụng database memeory nhưng mình cần test trên real database
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class BrandRepositoryTest {

    @Autowired
    BrandRepository brandRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Test
    public void testCreateBrand() {
        Category cate = new Category(2);
        Brand chivas = new Brand("CHIVASS", "testlogo.jpg");
        chivas.getCategories().add(cate);
        Brand savedrepo =  brandRepository.save(chivas);

        assertThat(savedrepo.getId()).isGreaterThan(0);
    }

    @Test
    public void testGetById(){
        Brand brand = brandRepository.findById(2).get();
        System.out.println(brand);

        assertThat(brand.getName()).isEqualTo("CHIVASS");
    }

    @Test
    public void testUpdateBrand(){
        String newName = "new Chivass";

        Brand brandById = brandRepository.findById(2).get();
        brandById.setName(newName);
        Brand savedrepo =  brandRepository.save(brandById);

        assertThat(savedrepo.getName()).isEqualTo(newName);
    }

    @Test
    public void testDeleteById(){
        brandRepository.deleteById(2);

        Optional<Brand> brandOptional = brandRepository.findById(2);
        assertThat(brandOptional).isEmpty();
    }
}
