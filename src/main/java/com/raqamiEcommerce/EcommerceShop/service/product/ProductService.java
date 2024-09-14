package com.raqamiEcommerce.EcommerceShop.service.product;

import com.raqamiEcommerce.EcommerceShop.dto.ImageDto;
import com.raqamiEcommerce.EcommerceShop.dto.ProductDto;
import com.raqamiEcommerce.EcommerceShop.exception.AlreadyExistsException;
import com.raqamiEcommerce.EcommerceShop.exception.ProductNotFoundException;
import com.raqamiEcommerce.EcommerceShop.model.Category;
import com.raqamiEcommerce.EcommerceShop.model.Image;
import com.raqamiEcommerce.EcommerceShop.model.Product;
import com.raqamiEcommerce.EcommerceShop.repository.CategoryRepository;
import com.raqamiEcommerce.EcommerceShop.repository.ImageRepository;
import com.raqamiEcommerce.EcommerceShop.repository.ProductRepository;
import com.raqamiEcommerce.EcommerceShop.request.AddProductRequest;
import com.raqamiEcommerce.EcommerceShop.request.UpdateProductRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService{
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final ImageRepository imageRepository;
    @Override
    public Product addProduct(AddProductRequest request) {
        if(productExists(request.getName(),request.getBrand())){
            throw new AlreadyExistsException(request.getName()+" Already Exists in "+request.getBrand());
        }
        Category category= Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName()))
                .orElseGet(()->{
                    Category newCategory= new Category(request.getCategory().getName());
                    return categoryRepository.save(newCategory);
                });
        request.setCategory(category);
        return productRepository.save(createProduct(request,category));
    }

    public Product createProduct(AddProductRequest request, Category category){
        return new Product(
                request.getName(),
                request.getBrand(),
                request.getPrice(),
                request.getInventory(),
                request.getDescription(),
                category
        );
    }

    @Override
    public List<Product> getAllProduct() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(long id) {
        return productRepository
                .findById(id).orElseThrow(()->new ProductNotFoundException("Product Not Found"));
    }

    @Override
    public void deleteProduct(long id) {
        productRepository.findById(id)
                .ifPresentOrElse(productRepository::delete,
                        ()->{throw new ProductNotFoundException("Product Not Found");});

    }

    @Override
    public void deleteAllProduct() {
       long amount= productRepository.count();
       if(amount>0){
           productRepository.deleteAll();
       }else{
           throw new ProductNotFoundException("No Product is there");
       }

    }

    @Override
    public Product updateProduct(UpdateProductRequest request, long id) {
        return productRepository.findById(id)
                .map(ep->updataExistingproduct(ep,request))
                .map(productRepository::save)
                .orElseThrow(()->new ProductNotFoundException("Product Not Found"));
    }
    public Product updataExistingproduct(Product existingProduct, UpdateProductRequest request){
        existingProduct.setName(request.getName());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setInventory(request.getInventory());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setDescription(request.getDescription());
        Category category=categoryRepository.findByName(request.getCategory().getName());
        existingProduct.setCategory(category);
        return existingProduct;
    }
    private boolean productExists(String name, String brand){
        return productRepository.existsByNameAndBrand(name,brand);
    }

    @Override
    public List<Product> getProductByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand(category,brand);
    }

    @Override
    public List<Product> getProductByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<Product> getProductByBrandAndName(String brand, String name) {
        return productRepository.findByBrandAndName(brand,name);
    }

    @Override
    public List<Product> addAllProduct(List<AddProductRequest> request) {
        List<Product>prod=new ArrayList<>();
        for(AddProductRequest req:request){
            if(productExists(req.getName(),req.getBrand())){
                continue;
            }
            Category category= Optional.ofNullable(categoryRepository.findByName(req.getCategory().getName()))
                    .orElseGet(()->{
                        Category newCategory= new Category(req.getCategory().getName());
                        return categoryRepository.save(newCategory);
                    });
            req.setCategory(category);
           prod.add(productRepository.save(createProduct(req,category)));
        }

        return prod;

    }

    @Override
    public long countProductsByBrandAndName(String brand,String name) {
        return productRepository.countByBrandAndName(brand,name);
    }
    @Override
    public List<ProductDto> getConvertedProducts(List<Product> products){
        return products.stream().map(this::convertToDto).toList();
    }
    @Override
    public ProductDto convertToDto(Product product){
        ProductDto productDto=modelMapper.map(product,ProductDto.class);
        List<Image> images=imageRepository.findByProductId(product.getId());
        List<ImageDto> imageDto=images
                .stream()
                .map(image -> modelMapper.map(image,ImageDto.class))
                .toList();

        productDto.setImages(imageDto);
        return productDto;


    }
}
