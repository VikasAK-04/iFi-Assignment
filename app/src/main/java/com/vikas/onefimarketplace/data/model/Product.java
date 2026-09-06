package com.vikas.onefimarketplace.data.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Core product domain model for 1Fi Marketplace.
 */
public class Product implements Serializable {
    private final String id;
    private final String name;
    private final String brand;
    private final String category;
    private final String description;
    private final double basePrice;
    private final float rating;
    private final int reviewCount;
    private final int imageResId;
    private final List<String> features;
    private final List<ProductVariant> variants;
    private final List<EmiPlan> emiPlans;
    private final boolean isAvailable;

    public Product(String id, String name, String brand, String category, String description,
                   double basePrice, float rating, int reviewCount, int imageResId,
                   List<String> features, List<ProductVariant> variants, List<EmiPlan> emiPlans,
                   boolean isAvailable) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.category = category;
        this.description = description;
        this.basePrice = basePrice;
        this.rating = rating;
        this.reviewCount = reviewCount;
        this.imageResId = imageResId;
        this.features = features != null ? new ArrayList<>(features) : Collections.emptyList();
        this.variants = variants != null ? new ArrayList<>(variants) : Collections.emptyList();
        this.emiPlans = emiPlans != null ? new ArrayList<>(emiPlans) : Collections.emptyList();
        this.isAvailable = isAvailable;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBrand() {
        return brand;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public float getRating() {
        return rating;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public int getImageResId() {
        return imageResId;
    }

    public List<String> getFeatures() {
        return Collections.unmodifiableList(features);
    }

    public List<ProductVariant> getVariants() {
        return Collections.unmodifiableList(variants);
    }

    public List<EmiPlan> getEmiPlans() {
        return Collections.unmodifiableList(emiPlans);
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    /**
     * Helper to retrieve the lowest starting monthly EMI for display in product cards.
     */
    public double getStartingMonthlyEmi() {
        if (emiPlans.isEmpty()) return basePrice;
        double lowestMonthly = Double.MAX_VALUE;
        for (EmiPlan plan : emiPlans) {
            double monthly = plan.calculateMonthlyAmount(basePrice);
            if (monthly < lowestMonthly) {
                lowestMonthly = monthly;
            }
        }
        return lowestMonthly != Double.MAX_VALUE ? lowestMonthly : basePrice;
    }

    /**
     * Check if product offers No-Cost EMI options.
     */
    public boolean hasNoCostEmi() {
        for (EmiPlan plan : emiPlans) {
            if (plan.isNoCostEmi()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
