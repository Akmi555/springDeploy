package org.blb.service.rent.productServise;

import org.blb.config.SupabaseConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

@Service
public class SupabaseService {

    @Autowired
    private SupabaseConfig supabaseConfig;


    public String uploadImage(MultipartFile image) throws IOException {
        String originalFileName = image.getOriginalFilename();
        System.out.println(originalFileName + "-----------");
        String uniqueFileName = UUID.randomUUID().toString() + "_" + originalFileName;
        String supabaseUrl = supabaseConfig.getSupabaseUrl();

        // Check valid URL
        if (!supabaseUrl.startsWith("http://") && !supabaseUrl.startsWith("https://")) {
            throw new MalformedURLException("Supabase URL must start with http:// or https://");
        }

        // Check if the file is an image
        String contentType = image.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IOException("File type not allowed. Only image files are allowed.");
        }

        String endpoint = String.format("%s/storage/v1/object/%s/%s",
                supabaseUrl, supabaseConfig.getBucketName(), uniqueFileName);

        System.out.println("Endpoint: " + endpoint);

        URL url = new URL(endpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Bearer " + supabaseConfig.getSupabaseServiceRoleSecret());
        connection.setRequestProperty("Content-Type", contentType);

        System.out.println(supabaseConfig.getSupabaseServiceRoleSecret());
        System.out.println(supabaseConfig.getBucketName());
        System.out.println(supabaseUrl);

        try (InputStream inputStream = image.getInputStream();
             OutputStream outputStream = connection.getOutputStream()) {

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
System.out.println( connection + "--------------------------------------------");
        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new IOException("Failed to upload image: " + connection.getResponseMessage());
        }

        // Return URL of the uploaded image
        return String.format("%s/storage/v1/object/public/%s/%s",
                supabaseConfig.getSupabaseUrl(), supabaseConfig.getBucketName(), uniqueFileName);
    }
}
