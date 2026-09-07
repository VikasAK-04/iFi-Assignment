# 1Fi SDE Intern Assignment — 1Fi Marketplace

An authentic, production-ready implementation of the **1Fi Marketplace** feature built seamlessly within the existing **1Fi Shop** experience for the 1Fi Software Development Engineer (SDE) Intern assessment.

---

## 📱 Project Overview

The objective of this assignment is to extend the 1Fi Android application by introducing the **1Fi Marketplace** inside the existing **Shop** section while preserving 1Fi's design language, typography, and navigation patterns.

### Key Highlights
- **100% Native Android**: Built in **Java** using standard **XML Layouts** and Android Views (minSdk 24, targetSdk 35).
- **Zero Redesign**: Preserves the core 1Fi 5-tab bottom navigation (`Home | Shop | EMI Dues | Limit | Profile`) and 1Fi's purple brand identity (`#6C2BD9`).
- **Complete End-to-End Flow**: Shop → Marketplace Catalog → Product Detail → Variant & EMI Selection → Review Screen → Application Confirmation.

---

## 🏗 Architecture & Design Patterns

The project is structured according to **Clean Architecture** and the **MVVM (Model-View-ViewModel)** pattern:

```
UI Layer (Fragments, Adapters, XML Layouts)
        ↕ (LiveData / Reactive Observers)
ViewModel Layer (MarketplaceViewModel, ProductDetailViewModel)
        ↕ (Repository Pattern)
Repository Layer (ProductRepository, ProductRepositoryImpl)
        ↕
Data Source Layer (ProductDataSource, MockProductDataSource)
```

### Core Engineering Features
1. **Generic State Management (`Resource<T>`)**:
   - `LOADING`: Displays purple indeterminate progress indicator.
   - `SUCCESS`: Renders list or detailed view.
   - `ERROR`: Shows friendly error message with an active **Retry** CTA.
   - `EMPTY`: Shows zero-results illustration with a **Clear Search** action.
2. **Dynamic & Asynchronous Data Fetching**:
   - Simulated realistic network latency via `ExecutorService` without freezing the main thread.
   - Zero hardcoded product or financial data in UI layouts or activities.
3. **Reactive EMI & Pricing Calculations**:
   - Selecting different product variants (e.g. 256GB vs 512GB) or EMI tenures (3m to 24m) dynamically recalculates monthly installments, processing fees, and total payable amounts in real-time.

---

## ✨ Features Implemented

### 1. 3-Tab Shop Screen (`ShopFragment`)
- **Top Brands Tab**: Authentic 1Fi brand partner cards (Air India, Apple Premium Reseller, CaratLane) matching live app screens.
- **Nearby Stores Tab**: Local partner store cards with distance indicators and instant QR approval tags.
- **1Fi Marketplace Tab**: Complete product catalog with real-time search and category filtering chips (*All, Smartphones, Laptops, Wearables, Audio, TV & Display*).

### 2. Product Details (`ProductDetailFragment`)
- High-resolution product images, rating breakdowns, and full descriptions.
- **Key Specifications**: Structured feature bullet points.
- **Variant Selection**: Interactive chip picker for storage/colors.
- **Interactive EMI Plan Picker**:
  - Highlights 0% No-Cost EMI badges.
  - Active visual state (1Fi purple border, surface tint, custom radio indicator).
  - Live breakdown of tenure, monthly installment, and processing fees.

### 3. Order Review & Confirmation
- **Review Screen (`ReviewFragment`)**: Transparent financial summary before applying.
- **Confirmation Dialog (`ConfirmationDialogFragment`)**: Displays a unique 6-digit application reference ID and safely resets navigation back to the Marketplace.

### 4. Layout & UI/UX Polish
- **No Overlap / Clean Scrolling**: Configured `clipToPadding="false"` with `paddingBottom="80dp"` on scrollable containers to ensure the floating 5-tab bottom navigation never overlaps or clips the bottom-most product card.

---

## 🛠 Tech Stack

- **Language**: Java
- **Architecture**: MVVM + Repository Pattern
- **Jetpack Libraries**:
  - `ViewModel` & `LiveData`
  - `ViewBinding`
  - `RecyclerView` & `ConstraintLayout`
  - `Material Components` (MaterialCardView, Chip, BottomNavigationView)
- **Minimum SDK**: API 24 (Android 7.0)
- **Target SDK**: API 35 (Android 15)

---

## 🚀 How to Build & Run

1. Clone this repository:
   ```bash
   git clone https://github.com/VikasAK-04/iFi-Assignment.git
   ```
2. Open the project in **Android Studio** (Ladybug / Jellyfish or newer).
3. Allow Gradle to sync dependencies.
4. Build the debug APK:
   ```bash
   ./gradlew assembleDebug
   ```
5. Run on an Android Emulator or physical device (API 24+).
