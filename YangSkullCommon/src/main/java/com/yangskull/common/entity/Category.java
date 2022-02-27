package com.yangskull.common.entity;

import lombok.Builder;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 128, nullable = false, unique = true)
    private String name;

    @Column(length = 64, nullable = false, unique = true)
    private String alias;

    @Column(length = 128, nullable = false)
    private String image;

    private boolean enabled;

    @OneToOne
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private Set<Category> children = new HashSet<>();

    public Category() {

    }

    public Category(Integer id, String name, String alias) {
        this.id = id;
        this.name = name;
        this.alias = alias;
    }

    public Category(Integer id) {
        this.id = id;
    }

    public static Category copyIdAndName(Category category) {
        Category copyCate = new Category();
        copyCate.setId(category.getId());
        copyCate.setName(category.getName());
        return copyCate;
    }

    public static Category copyIdAndName(Integer id, String name) {
        Category copyCate = new Category();
        copyCate.setId(id);
        copyCate.setName(name);
        return copyCate;
    }

    public static Category copyFull(Category category) {
        Category copyCate = new Category();
        copyCate.setId(category.getId());
        copyCate.setName(category.getName());
        copyCate.setImage(category.getImage());
        copyCate.setAlias(category.alias);
        copyCate.setEnabled(category.isEnabled());
        return copyCate;
    }

    public static Category copyFull(Category category, String name) {
        Category copyCate = Category.copyFull(category);
        copyCate.setName(name);
        return copyCate;
    }

    //constructor tạo root category
    public Category(String name) {
        this.name = name;
        this.alias = name;
        this.image = "default.png";
    }

    //constructor tạo sub category
    public Category(String name, Category rootCategory) {
        this(name);
        this.alias = name;
        this.parent = rootCategory;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

    public Set<Category> getChildren() {
        return children;
    }

    public void setChildren(Set<Category> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "Category[id: " + id + " name: " + name + "]";
    }


    @Transient
    public String getImagesPath() {
        if (id == null || image.isEmpty() || image.isBlank()) {
            return "/images/categoryThumbnail.png";
        }
        return "/category-images/" + this.id + "/" + this.image;
    }
}
