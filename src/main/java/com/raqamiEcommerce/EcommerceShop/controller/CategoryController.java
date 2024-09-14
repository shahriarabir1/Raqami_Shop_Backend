package com.raqamiEcommerce.EcommerceShop.controller;

import com.raqamiEcommerce.EcommerceShop.dto.ImageDto;
import com.raqamiEcommerce.EcommerceShop.exception.AlreadyExistsException;
import com.raqamiEcommerce.EcommerceShop.exception.CategoryNotFounException;
import com.raqamiEcommerce.EcommerceShop.exception.ImageNotFoundException;
import com.raqamiEcommerce.EcommerceShop.model.Category;
import com.raqamiEcommerce.EcommerceShop.model.Image;
import com.raqamiEcommerce.EcommerceShop.response.ApiResponse;
import com.raqamiEcommerce.EcommerceShop.service.category.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/categories")
public class CategoryController {
    private final ICategoryService categoryService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addCategory
            (@RequestBody Category request){
        try{
            Category category=categoryService.addCategory(request);
            return ResponseEntity.ok(new ApiResponse("Added Success",category));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(CONFLICT).
                    body(new ApiResponse(e.getMessage(),null));
        }

    }
    @GetMapping("/category/{id}/category")
    public ResponseEntity<ApiResponse> getCategory(@PathVariable long id) {
        try {
            Category category= categoryService.getCategoryById(id);
            return ResponseEntity.ok(new ApiResponse("Found",category));
        } catch (CategoryNotFounException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(),null));
        }

    }
    @GetMapping("/category/{name}")
    public ResponseEntity<ApiResponse> getCategoryByName(@PathVariable String name) {
        try {
            Category category= categoryService.getCategoryByName(name);
            return ResponseEntity.ok(new ApiResponse("Found",category));
        } catch (CategoryNotFounException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(),null));
        }

    }
    @DeleteMapping("/category/{id}/delete")
    public  ResponseEntity<ApiResponse> deleteCategory(@PathVariable long id){
        try {
            categoryService.deleteCategory(id);
            return ResponseEntity.ok(new ApiResponse("Delete Success", null));
        }
        catch (CategoryNotFounException e){
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(),null));
        }


    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse> updateCategory(@RequestBody Category cat, @PathVariable long id){
        try {
            categoryService.updateCategory(cat, id);
            return ResponseEntity.ok(new ApiResponse("Updated Success", null));
        }catch (CategoryNotFounException e){
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(),null));
        }

    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse>getAllCategories(){
        try {
            List<Category> categories=categoryService.getAllCategories();
            return ResponseEntity.ok(new ApiResponse("Found!",categories));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Something Went Wrong",INTERNAL_SERVER_ERROR));
        }
    }
}
