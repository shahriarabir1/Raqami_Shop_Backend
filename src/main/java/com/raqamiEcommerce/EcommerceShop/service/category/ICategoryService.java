package com.raqamiEcommerce.EcommerceShop.service.category;

import com.raqamiEcommerce.EcommerceShop.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ICategoryService{
    Category getCategoryById(long id);
    Category getCategoryByName(String name);
    List<Category> getAllCategories();
    Category addCategory(Category category);
    Category updateCategory(Category category,long id);
    void deleteCategory(long id);
}
