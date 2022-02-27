package com.yangskull.admin.category;

import com.yangskull.admin.user.UserNotFoundException;
import com.yangskull.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

@Service
@Transactional
public class CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    public List<Category> listAll() {
        List<Category> rootCategories = categoryRepository.findRootCategories(Sort.by("name").ascending());
        return listHierarchicalCategories(rootCategories);
    }

    private List<Category> listHierarchicalCategories(List<Category> rootCategories) {
        List<Category> listHierarchicalCategories = new ArrayList<>();

        for (Category rootCategory : rootCategories) {
            listHierarchicalCategories.add(Category.copyFull(rootCategory));

            Set<Category> children = sortSubCategories(rootCategory.getChildren());

            for (Category subCategory : children) {
                String name = "--" + subCategory.getName();
                listHierarchicalCategories.add(Category.copyFull(subCategory, name));

                listSubHierarchicalCategories(listHierarchicalCategories, subCategory, 1);
            }
        }

        return listHierarchicalCategories;
    }

    private void listSubHierarchicalCategories(List<Category> listHierarchicalCategories,
                                               Category parent,
                                               int subLevel) {
        StringBuilder name = new StringBuilder("");
        int newSubLevel = subLevel + 1;

        Set<Category> children = parent.getChildren();
        for (Category subCategory : children) {
            for (int i = 0; i < newSubLevel; i++) {
                name.append("--");
            }
            name.append(subCategory.getName());
            listHierarchicalCategories.add(Category.copyFull(subCategory, name.toString()));

            listSubHierarchicalCategories(listHierarchicalCategories, subCategory, newSubLevel);

        }

    }

    /*
    Ý tưởng đây là
        1. check coi category đó có parent kh => nếu kh nghĩ là nó là cate gốc , cái đầu tiên
        2. bỏ vào list
        3. lấy childrent của parent đó ra => lấy name + -- phía trc
        4. add vào list tiếp
        5. đệ quy tiếp
    * */
    public List<Category> listCategoriesUsedInForm() {
        List<Category> categoriesUsedInForm = new ArrayList<>();

        Iterable<Category> categoryInDB = categoryRepository.findRootCategories(Sort.by("name").ascending());

        for (Category category : categoryInDB) {
            if (Objects.isNull(category.getParent())) {
                categoriesUsedInForm.add(Category.copyIdAndName(category));


                Set<Category> categorySet = sortSubCategories(category.getChildren());
                for (Category subCate : categorySet) {
                    String newName = "--" + subCate.getName();
                    categoriesUsedInForm.add(Category.copyIdAndName(subCate.getId(), newName));

                    //subCate đây chính là parent trong hàm listChildren
                    listChildren(categoriesUsedInForm, subCate, 1);
                }
            }
        }
        return categoriesUsedInForm;
    }

    public Category save(Category category) {
        return categoryRepository.save(category);
    }


    /*
     * Example: Sprint
     *         -- Whisky
     *         -- Rum
     *            ---- Rum 1
     *
     * */
    private void listChildren(List<Category> categoriesUsedInForm, Category parent, int subLevel) {
        StringBuilder name = new StringBuilder("");
        int newSubLevel = subLevel + 1;
        Set<Category> setCategorySet = sortSubCategories(parent.getChildren());

        for (Category subCategory : setCategorySet) {
            for (int i = 0; i < newSubLevel; i++) {
                name.append("--");
            }
            name.append(subCategory.getName());

            //use static crete new Category with id and new name
            categoriesUsedInForm.add(Category.copyIdAndName(subCategory.getId(), name.toString()));

            listChildren(categoriesUsedInForm, subCategory, newSubLevel);
        }
    }


    public Category get(Integer id) throws CategoryNotFoundException {
        try {
            return categoryRepository.findById(id).get();
        } catch (NoSuchElementException ex) {
            throw new CategoryNotFoundException("Could not find any category with ID " + id);
        }
    }

    public String checkUnique(Integer id, String name, String alias) {
        boolean isCreateNew = (id == null || id == 0);
        Category categoryByName = categoryRepository.findByName(name);
        if(isCreateNew){
            if(!Objects.isNull(categoryByName)){
                return "DuplicateName";
            }else{
                Category categoryByAlias = categoryRepository.findByAlias(alias);
                if(!Objects.isNull(categoryByAlias)){
                    return "DuplicateAlias";
                }
            }
        }else{
            //nếu mà dưới mode Edit/Update => mi2nhs ẽ check coi id có giống nhau kh nếu kh giống nghĩ là khác nhau => duplicate
            if(categoryByName !=null && categoryByName.getId() != id){
                return "DuplicateName";
            }
            Category categoryByAlias = categoryRepository.findByAlias(alias);
            if(!Objects.isNull(categoryByAlias) && categoryByAlias.getId() != id ){
                return "DuplicateAlias";
            }
        }
        return "OK";
    }

    private SortedSet<Category> sortSubCategories(Set<Category> chidlren){
        SortedSet<Category> sortedChildren = new TreeSet<>(new Comparator<Category>() {
            @Override
            public int compare(Category o1, Category o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        sortedChildren.addAll(chidlren);
        return sortedChildren;
    }

}
