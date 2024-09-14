package com.raqamiEcommerce.EcommerceShop.service.image;

import com.raqamiEcommerce.EcommerceShop.dto.ImageDto;
import com.raqamiEcommerce.EcommerceShop.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageService {
    Image getImageById(long id);
    void deleteImageById(long id);
    List<ImageDto> saveImage(List<MultipartFile> files, long productId);
    void updateImage(MultipartFile file,long id);
}
