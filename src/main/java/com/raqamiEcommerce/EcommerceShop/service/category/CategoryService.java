package com.raqamiEcommerce.EcommerceShop.service.category;

import com.raqamiEcommerce.EcommerceShop.exception.AlreadyExistsException;
import com.raqamiEcommerce.EcommerceShop.exception.CategoryNotFounException;
import com.raqamiEcommerce.EcommerceShop.model.Category;
import com.raqamiEcommerce.EcommerceShop.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService{
   private final CategoryRepository categoryRepository;
    @Override
    public Category getCategoryById(long id) {
        return categoryRepository.findById(id).orElseThrow(()->new CategoryNotFounException("Category not Found"));
    }

    @Override
    public Category getCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category addCategory(Category category) {
        return  Optional.of(category).filter(c -> !categoryRepository.existsByName(c.getName()))
                .map(categoryRepository :: save)
                .orElseThrow(() -> new AlreadyExistsException(category.getName()+" already exists"));
    }

    @Override
    public Category updateCategory(Category category,long id) {
        return Optional.ofNullable(getCategoryById(id))
                .map(oc->{
                    oc.setName(category.getName());
                    return categoryRepository.save(oc);
                }).orElseThrow(()->new CategoryNotFounException("Category not Found"));
    }

    @Override
    public void deleteCategory(long id) {
        categoryRepository.findById(id)
                .ifPresentOrElse(categoryRepository::delete, ()->{throw new CategoryNotFounException("Category Not Found");});
    }
}
