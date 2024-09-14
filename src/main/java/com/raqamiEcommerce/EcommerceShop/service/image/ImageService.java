package com.raqamiEcommerce.EcommerceShop.service.image;

import com.raqamiEcommerce.EcommerceShop.dto.ImageDto;
import com.raqamiEcommerce.EcommerceShop.exception.ImageNotFoundException;
import com.raqamiEcommerce.EcommerceShop.exception.ProductNotFoundException;
import com.raqamiEcommerce.EcommerceShop.model.Image;
import com.raqamiEcommerce.EcommerceShop.model.Product;
import com.raqamiEcommerce.EcommerceShop.repository.ImageRepository;
import com.raqamiEcommerce.EcommerceShop.repository.ProductRepository;
import com.raqamiEcommerce.EcommerceShop.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImageService implements IImageService{
    private final ImageRepository imageRepository;
    private final IProductService productService;
    @Override
    public Image getImageById(long id) {
        return imageRepository.findById(id)
                .orElseThrow(()->new ImageNotFoundException("Image not Found"));
    }

    @Override
    public void deleteImageById(long id) {
        Optional.ofNullable(getImageById(id))
                .ifPresentOrElse(imageRepository::delete,
                        ()->{throw new ImageNotFoundException("Image not Found");});
    }

    @Override
    public List<ImageDto> saveImage(List<MultipartFile> files, long productId) {
        Product product = productService.getProductById(productId);
        List<ImageDto> savedImageDto = new ArrayList<>();
        for (MultipartFile file : files) {
            try {
                Image image = new Image();
                image.setFileName(file.getOriginalFilename());
                image.setFileType(file.getContentType());
                image.setImage(new SerialBlob(file.getBytes()));
                image.setProduct(product);

                String buildDownloadUrl = "/api/v1/images/image/download/";
                String downloadUrl = buildDownloadUrl+image.getId();
                image.setDownloadUrl(downloadUrl);
                Image savedImage = imageRepository.save(image);
                savedImage.setDownloadUrl(buildDownloadUrl+savedImage.getId());
                imageRepository.save(savedImage);
                ImageDto imageDto = new ImageDto();
                imageDto.setId(savedImage.getId());
                imageDto.setFileName(savedImage.getFileName());
                imageDto.setDownloadUrl(savedImage.getDownloadUrl());
                savedImageDto.add(imageDto);

            }   catch(IOException | SQLException e){
                throw new RuntimeException(e.getMessage());
            }
        }
        return savedImageDto;
    }

    @Override
    public void updateImage(MultipartFile file, long id) {
        try {
            Image image=getImageById(id);
            image.setFileName(file.getOriginalFilename());
            image.setFileType(file.getContentType());
            image.setImage(new SerialBlob(file.getBytes()));
            imageRepository.save(image);
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }

    }
}
