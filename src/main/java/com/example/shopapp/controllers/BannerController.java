package com.example.shopapp.controllers;

import com.example.shopapp.dtos.BannerDTO;
import com.example.shopapp.exceptions.DataNotFoundException;
import com.example.shopapp.models.Banner;
import com.example.shopapp.responses.ResponseCustom;
import com.example.shopapp.services.BannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("${api.prefix}/banners")
@RequiredArgsConstructor
public class BannerController {
    private final BannerService bannerService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseCustom<?> createBanner(
            BannerDTO bannerDTO,
            BindingResult result) throws Exception {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return new ResponseCustom<>(HttpStatus.BAD_REQUEST.value(), "Create banner failed");
        }
        Banner newBanner = bannerService.createBanner(bannerDTO);
        List<Banner> banners = new ArrayList<Banner>();
        banners.add(newBanner);
        return new ResponseCustom<>(HttpStatus.CREATED.value(), "Create banner successfully", banners);
    }

    @GetMapping
    public ResponseCustom<List<Banner>> getBanners() {
        try {
            List<Banner> banners = bannerService.getAllBanners();
            return new ResponseCustom<>(HttpStatus.OK.value(), "Get all banners successfully", banners);
        } catch (Exception e) {
            return new ResponseCustom<>(HttpStatus.BAD_REQUEST.value(), "Cannot get all banners");
        }
    }

    @GetMapping("/{id}")
    public ResponseCustom<?> getBannerById(@PathVariable("id") Long id) {
        try {
            Banner existingBanner = bannerService.getBannerById(id);
            return new ResponseCustom<>(HttpStatus.OK.value(), "Get banner with id " + id + " successfully", existingBanner);
        } catch (Exception e) {
            return new ResponseCustom<>(HttpStatus.BAD_REQUEST.value(), "Cannot get banner with id " + id);
        }
    }

    @PutMapping("/{id}")
    public ResponseCustom<?> updateBanner(
            @PathVariable("id") Long id,
            BannerDTO bannerDTO) throws DataNotFoundException {
        try {
            bannerService.updateBanner(id, bannerDTO);
            return new ResponseCustom<>(HttpStatus.ACCEPTED.value(), "Update banner with id " + id + " successfully");
        } catch (Exception e) {
            return new ResponseCustom<>(HttpStatus.BAD_REQUEST.value(), "Cannot get update banner with id " + id);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseCustom<?> deleteBanner(@PathVariable("id") Long id) {
        bannerService.deleteBanner(id);
        return new ResponseCustom<>(HttpStatus.NO_CONTENT.value(), "Delete banner with id " + id + " successfully");
    }
}
