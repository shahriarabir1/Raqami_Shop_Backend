package com.raqamiEcommerce.EcommerceShop.service.product;

import com.raqamiEcommerce.EcommerceShop.dto.ProductDto;
import com.raqamiEcommerce.EcommerceShop.model.Product;
import com.raqamiEcommerce.EcommerceShop.request.AddProductRequest;
import com.raqamiEcommerce.EcommerceShop.request.UpdateProductRequest;

import java.util.List;

public interface IProductService {
    Product addProduct(AddProductRequest request);
    List<Product> getAllProduct();
    Product getProductById(long id);
    void deleteProduct(long id);
    void deleteAllProduct();
    Product updateProduct(UpdateProductRequest product, long id);
    List<Product>getProductByCategory(String category);
    List<Product>getProductByBrand(String brand);
    List<Product>getProductByCategoryAndBrand(String category,String brand);
    List<Product>getProductByName(String name);
    List<Product>getProductByBrandAndName(String brand,String name);
    List<Product>addAllProduct(List<AddProductRequest> request);
    long countProductsByBrandAndName(String brand,String name);

    List<ProductDto> getConvertedProducts(List<Product> products);

    ProductDto convertToDto(Product product);
}
