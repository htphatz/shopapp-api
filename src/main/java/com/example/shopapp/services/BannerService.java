package com.example.shopapp.services;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.shopapp.dtos.BannerDTO;
import com.example.shopapp.entities.Banner;
import com.example.shopapp.exceptions.ResourceNotFoundException;
import com.example.shopapp.repositories.BannerRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BannerService implements IBannerService {
    private final BannerRepository bannerRepository;
    private final CloudinaryService cloudinaryService;

    @Value("${resource.noImageUrl}")
    private String noImageUrl;

    @Override
    public Banner createBanner(BannerDTO bannerDTO) {
        Banner newBanner = Banner.builder()
                .name(bannerDTO.getName())
                .description(bannerDTO.getDescription())
                .build();
        String imageUrl = noImageUrl;
        // Nếu tồn tại file thì upload lên Cloudinary
        if (bannerDTO.getFileImage() != null && !bannerDTO.getFileImage().isEmpty()) {
            Map data = this.cloudinaryService.upload(bannerDTO.getFileImage());
            imageUrl = (String) data.get("secure_url");
        }
        newBanner.setImageUrl(imageUrl);
        return bannerRepository.save(newBanner);
    }

    @Override
    public Banner getBannerById(Long id) {
        return bannerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot find banner with id: " + id));
    }

    @Override
    public List<Banner> getAllBanners() {
        return bannerRepository.findAll();
    }

    @Override
    @Transactional
    public Banner updateBanner(Long id, BannerDTO bannerDTO) {
        Banner existingBanner = bannerRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Banner with id " + id + " not found"));
        existingBanner.setName(bannerDTO.getName());
        existingBanner.setDescription(bannerDTO.getDescription());
        existingBanner.setImageUrl(existingBanner.getImageUrl());
        if (bannerDTO.getFileImage() != null && !bannerDTO.getFileImage().isEmpty()) {
            Map data = this.cloudinaryService.upload(bannerDTO.getFileImage());
            String newImageUrl = (String) data.get("secure_url");
            existingBanner.setImageUrl(newImageUrl);
        }
        return bannerRepository.save(existingBanner);
    }

    @Override
    @Transactional
    public void deleteBanner(Long id) {
        bannerRepository.deleteById(id);
    }
}
