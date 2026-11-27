package com.stylesync.util;

import com.stylesync.dao.ClothingItemDAO;
import com.stylesync.model.ClothingItem;
import com.stylesync.model.User;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Utility to load sample clothing items into the wardrobe
 */
public class SampleDataLoader {
    
    private ClothingItemDAO clothingItemDAO;
    
    public SampleDataLoader() {
        this.clothingItemDAO = new ClothingItemDAO();
    }
    
    /**
     * Add sample clothing items for a user
     */
    public void loadSampleItems(User user) {
        List<ClothingItem> sampleItems = createSampleItems(user);
        
        for (ClothingItem item : sampleItems) {
            try {
                clothingItemDAO.save(item);
                System.out.println("Added sample item: " + item.getName());
            } catch (Exception e) {
                System.err.println("Error adding sample item: " + item.getName());
                e.printStackTrace();
            }
        }
        
        System.out.println("Sample data loaded successfully!");
    }
    
    private Set<String> createSet(String... items) {
        Set<String> set = new HashSet<>();
        for (String item : items) {
            set.add(item);
        }
        return set;
    }
    
    /**
     * Copy sample image to wardrobe_images directory
     */
    private String copySampleImageToWardrobe(String sampleImageFileName) {
        try {
            // Source file in sample_images folder
            File sourceFile = new File("sample_images", sampleImageFileName);
            
            if (!sourceFile.exists()) {
                System.err.println("Sample image not found: " + sourceFile.getAbsolutePath());
                return null;
            }
            
            // Use ImageUtil to save the image properly
            String savedPath = ImageUtil.saveImage(sourceFile);
            System.out.println("Copied " + sampleImageFileName + " to " + savedPath);
            return savedPath;
            
        } catch (IOException e) {
            System.err.println("Failed to copy sample image: " + sampleImageFileName);
            e.printStackTrace();
            return null;
        }
    }
    
    private List<ClothingItem> createSampleItems(User user) {
        List<ClothingItem> items = new ArrayList<>();
        
        System.out.println("Loading sample images from sample_images folder...");
        
        // 1. Brown Wide Leg Pants
        ClothingItem item1 = new ClothingItem();
        item1.setUserId(user.getId());
        item1.setName("Brown Wide Leg Pants");
        item1.setType("Bottom");
        item1.setColor("Brown");
        item1.setSeasons(createSet("Fall", "Winter"));
        item1.setOccasions(createSet("Casual"));
        item1.setNotes("Comfortable wide-leg trousers with drawstring");
        item1.setImagePath(copySampleImageToWardrobe("brown_pants.png"));
        item1.setImageSource("FILE");
        item1.setFavorite(false);
        item1.setWearCount(0);
        items.add(item1);
        
        // 2. Burgundy Off-Shoulder Dress
        ClothingItem item2 = new ClothingItem();
        item2.setUserId(user.getId());
        item2.setName("Burgundy Off-Shoulder Dress");
        item2.setType("Dress");
        item2.setColor("Red");
        item2.setSeasons(createSet("Fall", "Spring"));
        item2.setOccasions(createSet("Party", "Formal"));
        item2.setNotes("Elegant midi dress with puff sleeves");
        item2.setImagePath(copySampleImageToWardrobe("burgundy_dress.png"));
        item2.setImageSource("FILE");
        item2.setFavorite(true);
        item2.setWearCount(3);
        items.add(item2);
        
        // 3. Burgundy Strappy Mini Dress
        ClothingItem item3 = new ClothingItem();
        item3.setUserId(user.getId());
        item3.setName("Burgundy Strappy Mini Dress");
        item3.setType("Dress");
        item3.setColor("Red");
        item3.setSeasons(createSet("Summer", "Spring"));
        item3.setOccasions(createSet("Party", "Casual"));
        item3.setNotes("Flowy mini dress with thin straps");
        item3.setImagePath(copySampleImageToWardrobe("strappy_dress.png"));
        item3.setImageSource("FILE");
        item3.setFavorite(true);
        item3.setWearCount(5);
        items.add(item3);
        
        // 4. White Graphic T-Shirt
        ClothingItem item4 = new ClothingItem();
        item4.setUserId(user.getId());
        item4.setName("Everything Is A Choice Tee");
        item4.setType("Top");
        item4.setColor("White");
        item4.setSeasons(createSet("Summer", "Spring"));
        item4.setOccasions(createSet("Casual"));
        item4.setNotes("White graphic tee with motivational text");
        item4.setImagePath(copySampleImageToWardrobe("white_tee.png"));
        item4.setImageSource("FILE");
        item4.setFavorite(false);
        item4.setWearCount(8);
        items.add(item4);
        
        // 5. White Polka Dot Maxi Dress
        ClothingItem item5 = new ClothingItem();
        item5.setUserId(user.getId());
        item5.setName("White Polka Dot Maxi Dress");
        item5.setType("Dress");
        item5.setColor("White");
        item5.setSeasons(createSet("Spring", "Summer"));
        item5.setOccasions(createSet("Formal", "Party"));
        item5.setNotes("Elegant white dress with black polka dots");
        item5.setImagePath(copySampleImageToWardrobe("polka_dress.png"));
        item5.setImageSource("FILE");
        item5.setFavorite(true);
        item5.setWearCount(2);
        items.add(item5);
        
        // 6. Striped Shirt Dress
        ClothingItem item6 = new ClothingItem();
        item6.setUserId(user.getId());
        item6.setName("Striped Shirt Maxi Dress");
        item6.setType("Dress");
        item6.setColor("Brown");
        item6.setSeasons(createSet("Fall", "Spring"));
        item6.setOccasions(createSet("Casual", "Work"));
        item6.setNotes("Brown and white striped maxi dress");
        item6.setImagePath(copySampleImageToWardrobe("striped_shirt.png"));
        item6.setImageSource("FILE");
        item6.setFavorite(false);
        item6.setWearCount(4);
        items.add(item6);
        
        // 7. Blue Floral Print Top
        ClothingItem item7 = new ClothingItem();
        item7.setUserId(user.getId());
        item7.setName("Blue Floral Print Top");
        item7.setType("Top");
        item7.setColor("Blue");
        item7.setSeasons(createSet("Summer", "Spring"));
        item7.setOccasions(createSet("Casual"));
        item7.setNotes("Navy blue top with white floral pattern");
        item7.setImagePath(copySampleImageToWardrobe("floral_top.png"));
        item7.setImageSource("FILE");
        item7.setFavorite(false);
        item7.setWearCount(6);
        items.add(item7);
        
        // 8. Blue Flared Jeans
        ClothingItem item8 = new ClothingItem();
        item8.setUserId(user.getId());
        item8.setName("Blue Flared Jeans");
        item8.setType("Bottom");
        item8.setColor("Blue");
        item8.setSeasons(createSet("Fall", "Winter", "Spring"));
        item8.setOccasions(createSet("Casual"));
        item8.setNotes("Classic light blue flared jeans");
        item8.setImagePath(copySampleImageToWardrobe("blue_jeans.png"));
        item8.setImageSource("FILE");
        item8.setFavorite(true);
        item8.setWearCount(12);
        items.add(item8);
        
        // 9. Cream Knit Cardigan
        ClothingItem item9 = new ClothingItem();
        item9.setUserId(user.getId());
        item9.setName("Cream Knit Cardigan");
        item9.setType("Outerwear");
        item9.setColor("White");
        item9.setSeasons(createSet("Winter", "Fall"));
        item9.setOccasions(createSet("Casual", "Work"));
        item9.setNotes("Warm cable knit cardigan with pockets");
        item9.setImagePath(copySampleImageToWardrobe("cardigan.png"));
        item9.setImageSource("FILE");
        item9.setFavorite(true);
        item9.setWearCount(7);
        items.add(item9);
        
        // 10. Sky Blue T-Shirt
        ClothingItem item10 = new ClothingItem();
        item10.setUserId(user.getId());
        item10.setName("Sky Blue Basic Tee");
        item10.setType("Top");
        item10.setColor("Blue");
        item10.setSeasons(createSet("Summer", "Spring"));
        item10.setOccasions(createSet("Casual", "Sport"));
        item10.setNotes("Simple bright blue t-shirt");
        item10.setImagePath(copySampleImageToWardrobe("blue_tee.png"));
        item10.setImageSource("FILE");
        item10.setFavorite(false);
        item10.setWearCount(10);
        items.add(item10);
        
        return items;
    }
}
