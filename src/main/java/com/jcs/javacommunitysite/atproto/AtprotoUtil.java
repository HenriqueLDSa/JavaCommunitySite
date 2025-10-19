package com.jcs.javacommunitysite.atproto;

import dev.mccue.json.Json;
import dev.mccue.json.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.Set;

import static dev.mccue.json.JsonDecoder.*;

/**
 * Utility class for AT Protocol operations.
 */
public class AtprotoUtil {
    
    private static final Set<String> KNOWN_BSKY_DOMAINS = Set.of(
        "bsky.social", "bsky.app", "bsky.team"
    );
    
    private static final String BSKY_API_BASE = "https://api.bsky.app";
    private static final String PLC_DIRECTORY_BASE = "https://plc.directory";
    
    /**
     * Resolves a PDS (Personal Data Server) host from an AT Protocol handle.
     * 
     * @param handle The handle to resolve (e.g., "user.bsky.social")
     * @return The PDS host URL, or null if resolution fails
     */
    public static String getPdsHostFromHandle(String handle) {
        if (handle == null || handle.trim().isEmpty()) {
            return null;
        }
        
        handle = handle.trim().toLowerCase();
        
        // Remove @ prefix if present
        if (handle.startsWith("@")) {
            handle = handle.substring(1);
        }
        
        // Check for known Bluesky domains first (fast path)
        for (String domain : KNOWN_BSKY_DOMAINS) {
            if (handle.equals(domain) || handle.endsWith("." + domain)) {
                return "https://" + domain;
            }
        }
        
        // Dynamic resolution for unknown handles
        try {
            return resolvePdsFromHandle(handle);
        } catch (Exception e) {
            // Log error in production, return null for now
            System.err.println("Failed to resolve PDS for handle: " + handle + " - " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Dynamically resolves PDS host using AT Protocol APIs.
     */
    private static String resolvePdsFromHandle(String handle) throws IOException {
        // Step 1: Resolve handle to DID
        String did = resolveHandleToDid(handle);
        if (did == null) {
            return null;
        }
        
        // Step 2: Fetch DID document and extract PDS endpoint
        return extractPdsFromDidDocument(did);
    }
    
    /**
     * Resolves a handle to a DID using the AT Protocol identity resolution API.
     */
    private static String resolveHandleToDid(String handle) throws IOException {
        String apiUrl = BSKY_API_BASE + "/xrpc/com.atproto.identity.resolveHandle?handle=" + handle;
        
        try {
            JsonObject response = makeGetRequest(apiUrl);
            if (response != null && response.containsKey("did")) {
                return field(response, "did", string());
            }
        } catch (Exception e) {
            throw new IOException("Failed to resolve handle to DID: " + e.getMessage(), e);
        }
        
        return null;
    }
    
    /**
     * Extracts PDS endpoint from DID document.
     */
    private static String extractPdsFromDidDocument(String did) throws IOException {
        String didDocUrl = getDidDocumentUrl(did);
        if (didDocUrl == null) {
            return null;
        }
        
        try {
            JsonObject didDoc = makeGetRequest(didDocUrl);
            if (didDoc == null || !didDoc.containsKey("service")) {
                return null;
            }
            
            // Find the AtprotoPersonalDataServer service
            var services = field(didDoc, "service", array(object()));
            for (JsonObject service : services) {
                if (service.containsKey("type") && service.containsKey("serviceEndpoint")) {
                    String serviceType = field(service, "type", string());
                    if ("AtprotoPersonalDataServer".equals(serviceType)) {
                        return field(service, "serviceEndpoint", string());
                    }
                }
            }
        } catch (Exception e) {
            throw new IOException("Failed to extract PDS from DID document: " + e.getMessage(), e);
        }
        
        return null;
    }
    
    /**
     * Constructs the URL for fetching a DID document based on DID type.
     */
    private static String getDidDocumentUrl(String did) {
        if (did.startsWith("did:plc:")) {
            return PLC_DIRECTORY_BASE + "/" + did;
        } else if (did.startsWith("did:web:")) {
            // Extract domain from did:web:{domain}
            String domain = did.substring("did:web:".length());
            return "https://" + domain + "/.well-known/did.json";
        }
        
        return null;
    }
    
    /**
     * Makes a GET request to the specified URL and returns the JSON response.
     */
    private static JsonObject makeGetRequest(String urlString) throws IOException {
        URI uri = URI.create(urlString);
        HttpURLConnection connection = (HttpURLConnection) uri.toURL().openConnection();
        
        try {
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setConnectTimeout(10000); // 10 seconds
            connection.setReadTimeout(10000);    // 10 seconds
            
            int statusCode = connection.getResponseCode();
            if (statusCode != 200) {
                throw new IOException("HTTP " + statusCode + " response from " + urlString);
            }
            
            try (InputStream is = connection.getInputStream();
                 BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                
                Json responseElement = Json.read(response.toString());
                return object(responseElement);
            }
            
        } finally {
            connection.disconnect();
        }
    }
}