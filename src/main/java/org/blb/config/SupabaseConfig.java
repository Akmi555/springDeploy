package org.blb.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SupabaseConfig {
    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.anon.public}")
    private String supabaseAnonKey;

    @Value("${supabase.service.role.secret}")
    private String supabaseServiceRoleSecret;

    @Value("${supabase.bucket}")
    private String bucketName;

    public String getSupabaseUrl() {
        return supabaseUrl;
    }

    public String getSupabaseAnonKey() {
        return supabaseAnonKey;
    }

    public String getSupabaseServiceRoleSecret() {
        return supabaseServiceRoleSecret;
    }

    public String getBucketName() {
        return bucketName;
    }

}
