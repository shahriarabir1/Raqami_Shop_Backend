package com.raqamiEcommerce.EcommerceShop.controller;

import com.raqamiEcommerce.EcommerceShop.dto.ProductDto;
import com.raqamiEcommerce.EcommerceShop.exception.AlreadyExistsException;
import com.raqamiEcommerce.EcommerceShop.exception.ProductNotFoundException;

import com.raqamiEcommerce.EcommerceShop.model.Product;
import com.raqamiEcommerce.EcommerceShop.request.AddProductRequest;
import com.raqamiEcommerce.EcommerceShop.request.UpdateProductRequest;
import com.raqamiEcommerce.EcommerceShop.response.ApiResponse;
import com.raqamiEcommerce.EcommerceShop.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/products")
public class ProductController {
    private final IProductService productService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addProduct
            (@RequestBody AddProductRequest request){
        try{
            Product product=productService.addProduct(request);
            ProductDto productDto=productService.convertToDto(product);
            return ResponseEntity.ok(new ApiResponse("Added Success",productDto));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(CONFLICT).
                    body(new ApiResponse(e.getMessage(),null));
        }

    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/add/all")
    public ResponseEntity<ApiResponse> addProduct
            (@RequestBody List<AddProductRequest> request){
        try{
            List<Product> product=productService.addAllProduct(request);
            List<ProductDto> convertedProducts=productService.getConvertedProducts(product);
            return ResponseEntity.ok(new ApiResponse("Added Success",convertedProducts));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(CONFLICT).
                    body(new ApiResponse(e.getMessage(),null));
        }

    }
    @GetMapping("/product/{id}")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable long id){
        try {
            Product product= productService.getProductById(id);
            ProductDto productDto=productService.convertToDto(product);
            return ResponseEntity.ok(new ApiResponse("Found",productDto));
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(),null));
        }

    }
    @GetMapping("/products/{name}")
    public ResponseEntity<ApiResponse> getProductByName(@PathVariable String name) {
        try {
           List<Product> product= productService.getProductByName(name);
            List<ProductDto> convertedProducts=productService.getConvertedProducts(product);
           if(product.isEmpty()){
               ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Products not found",null));
           }
            return ResponseEntity.ok(new ApiResponse("Found",convertedProducts));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("error",e.getMessage()));
        }

    }
    @GetMapping("/products/by-category/{category}")
    public ResponseEntity<ApiResponse> getProductByCategory(@PathVariable String category) {
        try {
            List<Product> product= productService.getProductByCategory(category);
            List<ProductDto> convertedProducts=productService.getConvertedProducts(product);
            if(product.isEmpty()){
                ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Products not found",null));
            }
            return ResponseEntity.ok(new ApiResponse("Found",convertedProducts));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("error",e.getMessage()));
        }

    }
    @GetMapping("/products/by-brand/{brand}")
    public ResponseEntity<ApiResponse> getProductByBrand(@PathVariable String brand) {
        try {
            List<Product> product= productService.getProductByBrand(brand);
            List<ProductDto> convertedProducts=productService.getConvertedProducts(product);
            if(product.isEmpty()){
                ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Products not found",null));
            }
            return ResponseEntity.ok(new ApiResponse("Found",convertedProducts));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("error",e.getMessage()));
        }

    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/product/delete/{id}")
    public  ResponseEntity<ApiResponse> deleteProduct(@PathVariable long id){
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok(new ApiResponse("Delete Success", id));
        }
        catch (ProductNotFoundException e){
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(),null));
        }


    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/products/delete/all")
    public  ResponseEntity<ApiResponse> deleteAllProduct(){
        try {
            productService.deleteAllProduct();
            return ResponseEntity.ok(new ApiResponse("All Delete Success", null));
        }
        catch (ProductNotFoundException e){
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(),null));
        }


    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateProduct(@RequestBody UpdateProductRequest prod, @PathVariable long id){
        try {
           Product product= productService.updateProduct(prod, id);
            ProductDto productDto=productService.convertToDto(product);
            return ResponseEntity.ok(new ApiResponse("Updated Success", productDto));
        }catch (ProductNotFoundException e){
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(),null));
        }

    }
    @GetMapping("/")
    public String home(){
        return "Raqami Ecommerce";
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse>getAllProducts(){
        try {
            List<Product> products=productService.getAllProduct();
            List<ProductDto> convertedProducts=productService.getConvertedProducts(products);
            return ResponseEntity.ok(new ApiResponse("Found!",convertedProducts));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Something Wents Wrong",INTERNAL_SERVER_ERROR));
        }
    }
    @GetMapping("/products/by/category-and-brand")
    public ResponseEntity<ApiResponse> getProductByCategoryAndBrand(@RequestParam String category,@RequestParam String brand) throws SQLException {
        try {
            List<Product> product= productService.getProductByCategoryAndBrand(category,brand);
            List<ProductDto> convertedProducts=productService.getConvertedProducts(product);
            if(product.isEmpty()){
                ResponseEntity.status(NOT_FOUND)
                        .body(new ApiResponse("Products not found",null));
            }
            return ResponseEntity.ok(new ApiResponse("Found",convertedProducts));
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse("error",e.getMessage()));
        }

    }
    @GetMapping("/products/by/brand-and-name")
    public ResponseEntity<ApiResponse> getProductByNameAndBrand(@RequestParam String name,@RequestParam String brand) throws SQLException {
        try {
            List<Product> product= productService.getProductByBrandAndName(brand,name);
            List<ProductDto> convertedProducts=productService.getConvertedProducts(product);
            return ResponseEntity.ok(new ApiResponse("Found",convertedProducts));
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(),null));
        }

    }
    @GetMapping("/product/count/by-brand/and-name")
    public ResponseEntity<ApiResponse> countProductsByBrandAndName(@RequestParam String brand, @RequestParam String name) {
        try {
            var productCount = productService.countProductsByBrandAndName(brand, name);
            return ResponseEntity.ok(new ApiResponse("Product count!", productCount));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse(e.getMessage(), null));
        }
    }


}
