package com.uni.vendas.infra.services.commom;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class UpImageService {

    private final Cloudinary cloudinary;

    public UpImageService(
            @Value("${CLOUDINARY_NAME}") String cloudName, // Confirma se no teu .env é o NOME ou a URL completa
            @Value("${CLOUDINARY_KEY}") String apiKey,
            @Value("${CLOUDINARY_SECRET}") String apiSecret) {

        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret));
    }

    public String fazerUpload(MultipartFile arquivo) {
        try {
            Map resultado = cloudinary.uploader().upload(arquivo.getBytes(), ObjectUtils.emptyMap());
            return (String) resultado.get("url");
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Image error");
        }
    }
}