package com.vikas.onefimarketplace.data.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Model representing a product variant (e.g. Storage capacity or Color option).
 */
public class ProductVariant implements Serializable {
    private final String id;
    private final String name;
    private final String value;
    private final double additionalPrice;
    private final boolean isAvailable;

    public ProductVariant(String id, String name, String value, double additionalPrice, boolean isAvailable) {
        this.id = id;
        this.name = name;
        this.value = value;
        this.additionalPrice = additionalPrice;
        this.isAvailable = isAvailable;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public double getAdditionalPrice() {
        return additionalPrice;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductVariant that = (ProductVariant) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
