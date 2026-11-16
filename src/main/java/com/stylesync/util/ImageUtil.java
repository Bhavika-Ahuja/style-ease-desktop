package com.stylesync.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/**
 * Utility class for image operations
 */
public class ImageUtil {
    private static final String IMAGE_DIRECTORY = ConfigUtil.getProperty("app.image.directory", "wardrobe_images");
    private static final int MAX_IMAGE_WIDTH = 800;
    private static final int MAX_IMAGE_HEIGHT = 800;

    static {
        // Create image directory if it doesn't exist
        File dir = new File(IMAGE_DIRECTORY);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public static String saveImage(File sourceFile) throws IOException {
        String fileName = UUID.randomUUID().toString() + getFileExtension(sourceFile.getName());
        Path destination = Paths.get(IMAGE_DIRECTORY, fileName);
        
        // Resize and save image
        BufferedImage originalImage = ImageIO.read(sourceFile);
        BufferedImage resizedImage = resizeImage(originalImage, MAX_IMAGE_WIDTH, MAX_IMAGE_HEIGHT);
        
        String format = getImageFormat(sourceFile.getName());
        ImageIO.write(resizedImage, format, destination.toFile());
        
        return destination.toString();
    }

    public static String saveImageFromURL(String imageUrl) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(imageUrl).build();
        
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Failed to download image: " + response);
            }
            
            String fileName = UUID.randomUUID().toString() + ".jpg";
            Path destination = Paths.get(IMAGE_DIRECTORY, fileName);
            
            InputStream inputStream = response.body().byteStream();
            BufferedImage originalImage = ImageIO.read(inputStream);
            
            if (originalImage == null) {
                throw new IOException("Invalid image format");
            }
            
            BufferedImage resizedImage = resizeImage(originalImage, MAX_IMAGE_WIDTH, MAX_IMAGE_HEIGHT);
            ImageIO.write(resizedImage, "jpg", destination.toFile());
            
            return destination.toString();
        }
    }

    public static String saveImageFromCamera(BufferedImage image) throws IOException {
        String fileName = UUID.randomUUID().toString() + ".jpg";
        Path destination = Paths.get(IMAGE_DIRECTORY, fileName);
        
        BufferedImage resizedImage = resizeImage(image, MAX_IMAGE_WIDTH, MAX_IMAGE_HEIGHT);
        ImageIO.write(resizedImage, "jpg", destination.toFile());
        
        return destination.toString();
    }

    public static BufferedImage loadImage(String imagePath) {
        try {
            File file = new File(imagePath);
            if (file.exists()) {
                return ImageIO.read(file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return createPlaceholderImage();
    }

    public static ImageIcon getScaledImageIcon(String imagePath, int width, int height) {
        BufferedImage image = loadImage(imagePath);
        Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }

    private static BufferedImage resizeImage(BufferedImage originalImage, int maxWidth, int maxHeight) {
        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();
        
        if (originalWidth <= maxWidth && originalHeight <= maxHeight) {
            return originalImage;
        }
        
        double widthRatio = (double) maxWidth / originalWidth;
        double heightRatio = (double) maxHeight / originalHeight;
        double ratio = Math.min(widthRatio, heightRatio);
        
        int newWidth = (int) (originalWidth * ratio);
        int newHeight = (int) (originalHeight * ratio);
        
        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = resizedImage.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
        g.dispose();
        
        return resizedImage;
    }

    private static BufferedImage createPlaceholderImage() {
        BufferedImage placeholder = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = placeholder.createGraphics();
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, 200, 200);
        g.setColor(Color.DARK_GRAY);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("No Image", 60, 100);
        g.dispose();
        return placeholder;
    }

    private static String getFileExtension(String fileName) {
        int lastDot = fileName.lastIndexOf('.');
        if (lastDot > 0) {
            return fileName.substring(lastDot);
        }
        return ".jpg";
    }

    private static String getImageFormat(String fileName) {
        String extension = getFileExtension(fileName).toLowerCase();
        if (extension.equals(".png")) return "png";
        if (extension.equals(".gif")) return "gif";
        return "jpg";
    }

    public static void deleteImage(String imagePath) {
        try {
            File file = new File(imagePath);
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
