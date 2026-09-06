package com.vikas.onefimarketplace.data.datasource;

import android.os.Handler;
import android.os.Looper;

import com.vikas.onefimarketplace.R;
import com.vikas.onefimarketplace.data.model.EmiPlan;
import com.vikas.onefimarketplace.data.model.Product;
import com.vikas.onefimarketplace.data.model.ProductVariant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Mock data source providing realistic product catalog data for 1Fi Marketplace.
 * Simulated asynchronous network latency included.
 */
public class MockProductDataSource implements ProductDataSource {

    private static final long SIMULATED_LATENCY_MS = 600L;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private final List<Product> mockCatalog = new ArrayList<>();

    // Simulated error flag for error state testing
    private boolean forceErrorState = false;

    public MockProductDataSource() {
        initMockData();
    }

    public void setForceErrorState(boolean forceError) {
        this.forceErrorState = forceError;
    }

    private void initMockData() {
        // Standard EMI Plans
        List<EmiPlan> phoneEmiPlans = Arrays.asList(
                new EmiPlan("emi_3", 3, 0.0, 0.0, true, "3 Months - 0% No Cost EMI"),
                new EmiPlan("emi_6", 6, 0.0, 0.0, true, "6 Months - 0% No Cost EMI"),
                new EmiPlan("emi_9", 9, 12.0, 199.0, false, "9 Months - Low Interest (12% p.a.)"),
                new EmiPlan("emi_12", 12, 14.0, 299.0, false, "12 Months - Standard EMI (14% p.a.)")
        );

        List<EmiPlan> laptopEmiPlans = Arrays.asList(
                new EmiPlan("emi_3", 3, 0.0, 0.0, true, "3 Months - 0% No Cost EMI"),
                new EmiPlan("emi_6", 6, 0.0, 0.0, true, "6 Months - 0% No Cost EMI"),
                new EmiPlan("emi_12", 12, 0.0, 0.0, true, "12 Months - Special No Cost EMI"),
                new EmiPlan("emi_18", 18, 13.5, 499.0, false, "18 Months - Easy EMI (13.5% p.a.)")
        );

        List<EmiPlan> audioEmiPlans = Arrays.asList(
                new EmiPlan("emi_3", 3, 0.0, 0.0, true, "3 Months - 0% No Cost EMI"),
                new EmiPlan("emi_6", 6, 0.0, 0.0, true, "6 Months - 0% No Cost EMI"),
                new EmiPlan("emi_9", 9, 12.5, 99.0, false, "9 Months - Flexi EMI")
        );

        // Product 1: Apple iPhone 15 Pro
        List<ProductVariant> iphoneVariants = Arrays.asList(
                new ProductVariant("var_128", "Storage", "128 GB", 0.0, true),
                new ProductVariant("var_256", "Storage", "256 GB", 10000.0, true),
                new ProductVariant("var_512", "Storage", "512 GB", 30000.0, true)
        );
        List<String> iphoneFeatures = Arrays.asList(
                "A17 Pro Chip with 6-core GPU",
                "Titanium design with textured matte glass back",
                "48 MP Main Camera with 3x optical zoom",
                "Action Button for quick customized actions",
                "USB-C connector with USB 3 support"
        );
        Product p1 = new Product(
                "prod_iphone15pro",
                "iPhone 15 Pro",
                "Apple",
                "Smartphones",
                "Supercharged by the A17 Pro chip, featuring lightweight titanium frame and pro camera system.",
                134900.0,
                4.8f,
                1420,
                R.drawable.ic_product_phone,
                iphoneFeatures,
                iphoneVariants,
                phoneEmiPlans,
                true
        );

        // Product 2: Apple MacBook Air M3
        List<ProductVariant> macbookVariants = Arrays.asList(
                new ProductVariant("var_8_256", "Config", "8GB RAM / 256GB SSD", 0.0, true),
                new ProductVariant("var_16_512", "Config", "16GB RAM / 512GB SSD", 20000.0, true),
                new ProductVariant("var_24_1tb", "Config", "24GB RAM / 1TB SSD", 45000.0, true)
        );
        List<String> macbookFeatures = Arrays.asList(
                "Apple M3 chip with 8-core CPU & 10-core GPU",
                "13.6-inch Liquid Retina Display with True Tone",
                "Up to 18 hours battery life",
                "1080p FaceTime HD camera & 4-speaker sound system",
                "MagSafe 3 charging port + 2x Thunderbolt ports"
        );
        Product p2 = new Product(
                "prod_macbookm3",
                "MacBook Air M3 13-inch",
                "Apple",
                "Laptops",
                "Incredibly thin, lightning fast laptop powered by M3 silicon with all-day battery life.",
                114900.0,
                4.9f,
                890,
                R.drawable.ic_product_laptop,
                macbookFeatures,
                macbookVariants,
                laptopEmiPlans,
                true
        );

        // Product 3: Sony WH-1000XM5
        List<ProductVariant> sonyVariants = Arrays.asList(
                new ProductVariant("var_black", "Color", "Black", 0.0, true),
                new ProductVariant("var_silver", "Color", "Silver", 0.0, true),
                new ProductVariant("var_blue", "Color", "Midnight Blue", 1000.0, true)
        );
        List<String> sonyFeatures = Arrays.asList(
                "Industry-leading Active Noise Canceling with 8 microphones",
                "Auto NC Optimizer based on environment and wearing condition",
                "Up to 30-hour battery life with quick charging (3 min for 3 hrs)",
                "Ultra-comfortable, lightweight design with soft fit leather",
                "Crystal clear hands-free calling with 4 beamforming mics"
        );
        Product p3 = new Product(
                "prod_sonyxm5",
                "WH-1000XM5 Wireless Headphones",
                "Sony",
                "Audio",
                "World-class noise-canceling headphones with premium sound quality and multipoint connection.",
                29990.0,
                4.7f,
                3200,
                R.drawable.ic_product_headphones,
                sonyFeatures,
                sonyVariants,
                audioEmiPlans,
                true
        );

        // Product 4: Samsung Galaxy Watch 6 Pro
        List<ProductVariant> watchVariants = Arrays.asList(
                new ProductVariant("var_44mm_bt", "Model", "44mm Bluetooth", 0.0, true),
                new ProductVariant("var_44mm_lte", "Model", "44mm LTE / Cellular", 4000.0, true)
        );
        List<String> watchFeatures = Arrays.asList(
                "Advanced BioActive Sensor for ECG & BIA body composition",
                "Sapphire Crystal glass display with IP68 water resistance",
                "Personalized Heart Rate Zones & Sleep Coaching",
                "Customizable Watch Faces & Seamless Galaxy Connectivity",
                "Long-lasting battery with fast wireless charging"
        );
        Product p4 = new Product(
                "prod_galaxywatch6",
                "Galaxy Watch 6 Pro",
                "Samsung",
                "Wearables",
                "Your daily health and wellness companion with advanced fitness tracking and premium titanium build.",
                34999.0,
                4.6f,
                750,
                R.drawable.ic_product_watch,
                watchFeatures,
                watchVariants,
                audioEmiPlans,
                true
        );

        // Product 5: Apple iPad Air M2
        List<ProductVariant> ipadVariants = Arrays.asList(
                new ProductVariant("var_wifi_128", "Connectivity", "Wi-Fi (128 GB)", 0.0, true),
                new ProductVariant("var_cellular_128", "Connectivity", "Wi-Fi + 5G Cellular (128 GB)", 15000.0, true),
                new ProductVariant("var_wifi_256", "Connectivity", "Wi-Fi (256 GB)", 10000.0, true)
        );
        List<String> ipadFeatures = Arrays.asList(
                "Apple M2 chip delivering breakthrough performance",
                "11-inch Liquid Retina display with P3 wide color & True Tone",
                "12MP Ultra Wide front camera in landscape orientation",
                "Supports Apple Pencil Pro and Magic Keyboard",
                "All-day battery life with Wi-Fi 6E support"
        );
        Product p5 = new Product(
                "prod_ipadairm2",
                "iPad Air 11-inch M2",
                "Apple",
                "Smartphones", // Or Tablets
                "Fresh design, incredible M2 speed, and versatile performance for work, creativity, and play.",
                59900.0,
                4.8f,
                610,
                R.drawable.ic_product_tablet,
                ipadFeatures,
                ipadVariants,
                laptopEmiPlans,
                true
        );

        // Product 6: Sony Bravia 4K TV
        List<ProductVariant> tvVariants = Arrays.asList(
                new ProductVariant("var_55in", "Screen Size", "55-inch 4K HDR", 0.0, true),
                new ProductVariant("var_65in", "Screen Size", "65-inch 4K HDR", 25000.0, true)
        );
        List<String> tvFeatures = Arrays.asList(
                "4K Processor X1 delivering intense contrast & realistic picture",
                "TRILUMINOS PRO technology with over a billion vivid colors",
                "Google TV OS with voice search hands-free Assistant",
                "Motionflow XR 240 for blur-free fast action scenes",
                "Dolby Atmos & X-Balanced Speaker for immersive acoustics"
        );
        Product p6 = new Product(
                "prod_braviatv",
                "Sony Bravia 4K Ultra HD Smart TV",
                "Sony",
                "TV & Display",
                "Transform your living room into a cinematic haven with vivid 4K HDR visuals and Google TV.",
                64990.0,
                4.7f,
                1890,
                R.drawable.ic_product_tv,
                tvFeatures,
                tvVariants,
                laptopEmiPlans,
                true
        );

        mockCatalog.addAll(Arrays.asList(p1, p2, p3, p4, p5, p6));
    }

    @Override
    public void getProducts(Callback<List<Product>> callback) {
        mainHandler.postDelayed(() -> {
            if (forceErrorState) {
                callback.onError(new RuntimeException("Simulated 1Fi network connection failure"));
            } else {
                callback.onSuccess(new ArrayList<>(mockCatalog));
            }
        }, SIMULATED_LATENCY_MS);
    }

    @Override
    public void getProductById(String id, Callback<Product> callback) {
        mainHandler.postDelayed(() -> {
            if (forceErrorState) {
                callback.onError(new RuntimeException("Simulated error loading product details"));
                return;
            }
            for (Product product : mockCatalog) {
                if (product.getId().equalsIgnoreCase(id)) {
                    callback.onSuccess(product);
                    return;
                }
            }
            callback.onError(new IllegalArgumentException("Product not found with ID: " + id));
        }, SIMULATED_LATENCY_MS);
    }

    @Override
    public void searchProducts(String query, String category, Callback<List<Product>> callback) {
        mainHandler.postDelayed(() -> {
            if (forceErrorState) {
                callback.onError(new RuntimeException("Search operation failed"));
                return;
            }

            List<Product> results = new ArrayList<>();
            String cleanQuery = query != null ? query.trim().toLowerCase() : "";
            String cleanCat = category != null ? category.trim() : "";

            for (Product p : mockCatalog) {
                boolean matchesCategory = cleanCat.isEmpty() || cleanCat.equalsIgnoreCase("All")
                        || p.getCategory().equalsIgnoreCase(cleanCat)
                        || (cleanCat.equalsIgnoreCase("Wearables") && p.getCategory().equalsIgnoreCase("Wearables"))
                        || (cleanCat.equalsIgnoreCase("Audio") && p.getCategory().equalsIgnoreCase("Audio"))
                        || (cleanCat.equalsIgnoreCase("Smartphones") && (p.getCategory().equalsIgnoreCase("Smartphones") || p.getName().toLowerCase().contains("ipad")))
                        || (cleanCat.equalsIgnoreCase("Laptops") && p.getCategory().equalsIgnoreCase("Laptops"))
                        || (cleanCat.equalsIgnoreCase("TV & Display") && p.getCategory().equalsIgnoreCase("TV & Display"));

                boolean matchesQuery = cleanQuery.isEmpty()
                        || p.getName().toLowerCase().contains(cleanQuery)
                        || p.getBrand().toLowerCase().contains(cleanQuery)
                        || p.getCategory().toLowerCase().contains(cleanQuery)
                        || p.getDescription().toLowerCase().contains(cleanQuery);

                if (matchesCategory && matchesQuery) {
                    results.add(p);
                }
            }

            callback.onSuccess(results);
        }, SIMULATED_LATENCY_MS);
    }
}
