package com.raqamiEcommerce.EcommerceShop.repository;

import com.raqamiEcommerce.EcommerceShop.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image,Long> {
    List<Image> findByProductId(Long id);
}
