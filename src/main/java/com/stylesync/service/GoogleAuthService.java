package com.stylesync.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
 * Service for Google OAuth authentication
 */
public class GoogleAuthService {
    
    private static final GsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final java.util.List<String> SCOPES = Arrays.asList(
        "openid",
        "https://www.googleapis.com/auth/userinfo.email",
        "https://www.googleapis.com/auth/userinfo.profile"
    );
    
    public GoogleUserInfo authenticateWithGoogle() throws Exception {
        // Load client secrets from resources
        InputStream in = getClass().getResourceAsStream("/client_secret.json");
        if (in == null) {
            throw new Exception("client_secret.json not found! Please add it to src/main/resources/");
        }
        
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
            JSON_FACTORY, new InputStreamReader(in)
        );
        
        // Build OAuth flow
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                new NetHttpTransport(), JSON_FACTORY, clientSecrets, SCOPES)
                .setAccessType("offline")
                .build();
        
        // Open browser and wait for authorization
        LocalServerReceiver receiver = new LocalServerReceiver.Builder()
                .setPort(8888)
                .build();
        
        Credential credential = new AuthorizationCodeInstalledApp(flow, receiver)
                .authorize("user");
        
        // Get user info using access token
        com.google.api.client.http.GenericUrl url = 
            new com.google.api.client.http.GenericUrl(
                "https://www.googleapis.com/oauth2/v1/userinfo?alt=json&access_token=" + 
                credential.getAccessToken()
            );
        
        com.google.api.client.http.HttpRequest request = 
            new NetHttpTransport().createRequestFactory()
                .buildGetRequest(url);
        
        String response = request.execute().parseAsString();
        
        // Parse JSON response
        String email = extractJsonValue(response, "email");
        String name = extractJsonValue(response, "name");
        String picture = extractJsonValue(response, "picture");
        
        return new GoogleUserInfo(email, name, picture);
    }
    
    /**
     * Simple JSON value extractor
     */
    private String extractJsonValue(String json, String key) {
        String searchKey = "\"" + key + "\":";
        int startIndex = json.indexOf(searchKey);
        if (startIndex == -1) return "";
        
        startIndex += searchKey.length();
        
        // Skip whitespace and opening quote
        while (startIndex < json.length() && 
               (json.charAt(startIndex) == ' ' || json.charAt(startIndex) == '"')) {
            startIndex++;
        }
        
        // Find closing quote
        int valueEnd = json.indexOf("\"", startIndex);
        if (valueEnd == -1) valueEnd = json.indexOf(",", startIndex);
        if (valueEnd == -1) valueEnd = json.indexOf("}", startIndex);
        
        return json.substring(startIndex, valueEnd).trim();
    }
    
    /**
     * Container for Google user information
     */
    public static class GoogleUserInfo {
        private final String email;
        private final String name;
        private final String pictureUrl;
        
        public GoogleUserInfo(String email, String name, String pictureUrl) {
            this.email = email;
            this.name = name;
            this.pictureUrl = pictureUrl;
        }
        
        public String getEmail() { 
            return email; 
        }
        
        public String getName() { 
            return name != null && !name.isEmpty() ? name : email.split("@")[0]; 
        }
        
        public String getPictureUrl() { 
            return pictureUrl; 
        }
    }
}
