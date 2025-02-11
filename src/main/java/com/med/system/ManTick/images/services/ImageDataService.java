

package com.med.system.ManTick.images.services;

import com.med.system.ManTick.images.ImageData;
import com.med.system.ManTick.images.ImageUtil;

import com.med.system.ManTick.images.repository.ImageDataRepository;

import lombok.AllArgsConstructor;

import java.io.IOException;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
public class ImageDataService {
    
    private ImageDataRepository imageDataRepository;

    public ImageData uploadImage(MultipartFile file) throws IOException {
        ImageData imageData = ImageData.builder()
            .name(file.getOriginalFilename())
            .type(file.getContentType())
            .imageData(ImageUtil.compressImage(file.getBytes())).build();

        imageDataRepository.save(imageData);
        // ImageUploadResponse response = ImageUploadResponse.builder().message("Image uploaded successfully " + file.getOriginalFilename()).build();

        return imageDataRepository.save(imageData);

    }

    @Transactional
    public ImageData getInfoByImageByName(String name) {
        Optional<ImageData> dbImage = imageDataRepository.findByName(name);

        return ImageData.builder()
                .name(dbImage.get().getName())
                .type(dbImage.get().getType())
                .imageData(ImageUtil.decompressImage(dbImage.get().getImageData())).build();

    }

    @Transactional
    public byte[] getImage(String name) {
        Optional<ImageData> dbImage = imageDataRepository.findByName(name);
        byte[] image = ImageUtil.decompressImage(dbImage.get().getImageData());
        return image;
    }

}
