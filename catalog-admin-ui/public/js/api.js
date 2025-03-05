/**
 * API Service for Tata Commerce Catalog Admin UI
 * This file contains functions to interact with the backend API
 */

const API_BASE_URL = 'http://localhost:8080/api';

// Mock data for fallback when API is unavailable
const MOCK_DATA = {
    productCategories: [
        {
            id: "c15f770f-7d02-4a38-9d54-aeb0a6a7baaa",
            product: { id: "740a9b6a-8131-4b4c-85c1-c4bf5363d7a5", name: "iPhone 13 Pro 128GB" },
            category: { id: "31bd267a-b546-4c24-b717-49ec686a5ac0", name: "Electronics > Smartphones" },
            merchant: { id: "eb601597-9c17-41e2-a454-6a668f00d10c", name: "Tata Cliq" },
            isPrimary: false,
            displayOrder: 1,
            effectiveFrom: "2025-03-05T16:32:31.388209",
            createdDate: "05-03-2025 04:32:31 pm",
            lastModifiedDate: "05-03-2025 04:32:31 pm",
            createdBy: "system",
            lastModifiedBy: "system"
        },
        {
            id: "b75ab8ed-8014-4778-81e4-43249410c6af",
            product: { id: "7f3442bb-ed44-4d5b-aba1-2a5e5a6004dd", name: "Samsung Galaxy S22 Ultra" },
            category: { id: "31bd267a-b546-4c24-b717-49ec686a5ac0", name: "Electronics > Smartphones" },
            merchant: { id: "eb601597-9c17-41e2-a454-6a668f00d10c", name: "Tata Cliq" },
            isPrimary: false,
            displayOrder: 1,
            effectiveFrom: "2025-03-05T16:32:31.388504",
            createdDate: "05-03-2025 04:32:31 pm",
            lastModifiedDate: "05-03-2025 04:32:31 pm",
            createdBy: "system",
            lastModifiedBy: "system"
        },
        {
            id: "1d624e2a-12a7-4fa1-a100-34f07698db71",
            product: { id: "cde6b792-4f5e-4e00-b991-beb2ec724845", name: "Organic Bananas (1 dozen)" },
            category: { id: "918dab3e-8222-4c75-9784-59d34c5d4aee", name: "Grocery > Fresh Fruits" },
            merchant: { id: "eb601597-9c17-41e2-a454-6a668f00d10c", name: "BigBasket" },
            isPrimary: false,
            displayOrder: 1,
            effectiveFrom: "2025-03-05T16:32:31.38869",
            createdDate: "05-03-2025 04:32:31 pm",
            lastModifiedDate: "05-03-2025 04:32:31 pm",
            createdBy: "system",
            lastModifiedBy: "system"
        },
        {
            id: "5ab80a9a-c8f9-4546-aea5-b66eab7af2f9",
            product: { id: "9518fb2b-0afc-4ce8-8612-55e415ea246a", name: "Multivitamin Tablets (60 count)" },
            category: { id: "98f2ef78-9300-45b3-af7d-f7ba45bf2b74", name: "Health > Supplements" },
            merchant: { id: "33f63519-48b4-41ac-853d-81a2690ff032", name: "Tata 1mg" },
            isPrimary: false,
            displayOrder: 1,
            effectiveFrom: "2025-03-05T16:32:31.38889",
            createdDate: "05-03-2025 04:32:31 pm",
            lastModifiedDate: "05-03-2025 04:32:31 pm",
            createdBy: "system",
            lastModifiedBy: "system"
        },
        {
            id: "2d0e5fb9-77c3-4367-8845-d5130e09336c",
            product: { id: "96dbafaf-96e4-4a2d-b38c-0388c8b874ee", name: "Gold Necklace with Diamond Pendant" },
            category: { id: "f244feb6-19fc-47f2-a16e-3b67c2ffe7c2", name: "Jewelry > Necklaces" },
            merchant: { id: "33f63519-48b4-41ac-853d-81a2690ff032", name: "Tata Jewelry" },
            isPrimary: false,
            displayOrder: 1,
            effectiveFrom: "2025-03-05T16:32:31.389074",
            createdDate: "05-03-2025 04:32:31 pm",
            lastModifiedDate: "05-03-2025 04:32:31 pm",
            createdBy: "system",
            lastModifiedBy: "system"
        }
    ],
    sellerProducts: [
        {
            product: { id: "740a9b6a-8131-4b4c-85c1-c4bf5363d7a5", name: "iPhone 13 Pro 128GB" },
            seller: { id: "a0526e66-17f0-4341-b601-7da8420e8dde", name: "Apple Store" },
            merchant: { id: "eb601597-9c17-41e2-a454-6a668f00d10c", name: "Tata Cliq" },
            id: "54f1354d-7de1-4b33-b63e-9857e182e720",
            createdDate: "05-03-2025 04:32:31 pm",
            lastModifiedDate: "05-03-2025 04:32:31 pm",
            createdBy: "SYSTEM"
        },
        {
            product: { id: "7f3442bb-ed44-4d5b-aba1-2a5e5a6004dd", name: "Samsung Galaxy S22 Ultra" },
            seller: { id: "da57205b-6008-4888-8a59-a8c803c755d6", name: "Samsung Official" },
            merchant: { id: "f2c03df4-f946-4005-b17e-d73de73bb4db", name: "Tata Croma" },
            id: "94031f65-f0fc-4b2c-80e3-6ae912259bc1",
            createdDate: "05-03-2025 04:32:31 pm",
            lastModifiedDate: "05-03-2025 04:32:31 pm",
            createdBy: "SYSTEM"
        }
    ],
    products: [
        {
            id: "740a9b6a-8131-4b4c-85c1-c4bf5363d7a5",
            name: "iPhone 13 Pro 128GB",
            description: "Apple iPhone 13 Pro with 128GB storage, Space Gray",
            sku: "APPL-IP13-128-GRY",
            price: 89999,
            status: "active",
            categories: ["Electronics", "Smartphones"],
            channels: ["Tata Cliq", "Croma"]
        },
        {
            id: "7f3442bb-ed44-4d5b-aba1-2a5e5a6004dd",
            name: "Samsung Galaxy S22 Ultra",
            description: "Samsung Galaxy S22 Ultra with 256GB storage, Phantom Black",
            sku: "SMSNG-S22U-256-BLK",
            price: 92999,
            status: "active",
            categories: ["Electronics", "Smartphones"],
            channels: ["Tata Cliq", "Croma"]
        },
        {
            id: "cde6b792-4f5e-4e00-b991-beb2ec724845",
            name: "Organic Bananas (1 dozen)",
            description: "Fresh organic bananas, pack of 12",
            sku: "BB-FR-BAN-ORG-12",
            price: 80,
            status: "active",
            categories: ["Grocery", "Fresh Fruits"],
            channels: ["BigBasket"]
        },
        {
            id: "9518fb2b-0afc-4ce8-8612-55e415ea246a",
            name: "Multivitamin Tablets (60 count)",
            description: "Daily multivitamin supplement, 60 tablets",
            sku: "1MG-VIT-MULTI-60",
            price: 599,
            status: "draft",
            categories: ["Health", "Supplements"],
            channels: ["Tata 1mg"]
        },
        {
            id: "96dbafaf-96e4-4a2d-b38c-0388c8b874ee",
            name: "Gold Necklace with Diamond Pendant",
            description: "22K gold necklace with diamond pendant",
            sku: "TJ-GLD-NCK-DIA-001",
            price: 45999,
            status: "active",
            categories: ["Jewelry", "Necklaces"],
            channels: ["Tata Cliq"]
        }
    ],
    categories: [
        {
            id: "31bd267a-b546-4c24-b717-49ec686a5ac0",
            name: "Electronics",
            description: "Electronic devices and gadgets",
            parentId: null,
            level: 1,
            path: "Electronics",
            children: [
                {
                    id: "a1b2c3d4-e5f6-4a5b-8c7d-9e8f7a6b5c4d",
                    name: "Smartphones",
                    description: "Mobile phones and smartphones",
                    parentId: "31bd267a-b546-4c24-b717-49ec686a5ac0",
                    level: 2,
                    path: "Electronics > Smartphones"
                }
            ]
        },
        {
            id: "918dab3e-8222-4c75-9784-59d34c5d4aee",
            name: "Grocery",
            description: "Food and grocery items",
            parentId: null,
            level: 1,
            path: "Grocery",
            children: [
                {
                    id: "b2c3d4e5-f6a5-4b8c-7d9e-8f7a6b5c4d3e",
                    name: "Fresh Fruits",
                    description: "Fresh fruits and vegetables",
                    parentId: "918dab3e-8222-4c75-9784-59d34c5d4aee",
                    level: 2,
                    path: "Grocery > Fresh Fruits"
                }
            ]
        }
    ]
};

/**
 * Generic function to make API requests with fallback to mock data
 * @param {string} endpoint - API endpoint
 * @param {Object} options - Fetch options
 * @param {string} mockDataKey - Key to access mock data if API fails
 * @returns {Promise<any>} - Response data
 */
async function fetchApi(endpoint, options = {}, mockDataKey = null) {
    try {
        console.log(`Fetching data from: ${API_BASE_URL}${endpoint}`);
        const response = await fetch(`${API_BASE_URL}${endpoint}`, {
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json',
                ...options.headers
            },
            ...options
        });

        if (!response.ok) {
            throw new Error(`API request failed with status ${response.status}`);
        }

        return await response.json();
    } catch (error) {
        console.error(`API Error (${endpoint}):`, error);
        
        // If mock data key is provided and exists, return mock data
        if (mockDataKey && MOCK_DATA[mockDataKey]) {
            console.log(`Falling back to mock data for ${mockDataKey}`);
            return MOCK_DATA[mockDataKey];
        }
        
        throw error;
    }
}

/**
 * Product Category API
 */
const productCategoryApi = {
    /**
     * Get all product categories
     * @returns {Promise<Array>} - List of product categories
     */
    getAllProductCategories: () => fetchApi('/product-category/query', {}, 'productCategories'),

    /**
     * Get product category by ID
     * @param {string} id - Product category ID
     * @returns {Promise<Object>} - Product category
     */
    getProductCategoryById: (id) => fetchApi(`/product-category/query/${id}`),

    /**
     * Get product categories by product ID
     * @param {string} productId - Product ID
     * @returns {Promise<Array>} - List of product categories
     */
    getProductCategoriesByProductId: (productId) => fetchApi(`/product-category/query/product/${productId}`),

    /**
     * Get product categories by category ID
     * @param {string} categoryId - Category ID
     * @returns {Promise<Array>} - List of product categories
     */
    getProductCategoriesByCategoryId: (categoryId) => fetchApi(`/product-category/query/category/${categoryId}`),

    /**
     * Get categories by product ID
     * @param {string} productId - Product ID
     * @returns {Promise<Array>} - List of categories
     */
    getCategoriesByProductId: (productId) => fetchApi(`/product-category/query/product/${productId}/categories`),

    /**
     * Get products by category ID
     * @param {string} categoryId - Category ID
     * @returns {Promise<Array>} - List of products
     */
    getProductsByCategoryId: (categoryId) => fetchApi(`/product-category/query/category/${categoryId}/products`)
};

/**
 * Seller Product API
 */
const sellerProductApi = {
    /**
     * Get all seller products
     * @returns {Promise<Array>} - List of seller products
     */
    getAllSellerProducts: () => fetchApi('/seller-product/query', {}, 'sellerProducts'),

    /**
     * Get seller product by ID
     * @param {string} id - Seller product ID
     * @returns {Promise<Object>} - Seller product
     */
    getSellerProductById: (id) => fetchApi(`/seller-product/query/${id}`),

    /**
     * Get seller products by product ID
     * @param {string} productId - Product ID
     * @returns {Promise<Array>} - List of seller products
     */
    getSellerProductsByProductId: (productId) => fetchApi(`/seller-product/query/product/${productId}`),

    /**
     * Get seller products by seller ID
     * @param {string} sellerId - Seller ID
     * @returns {Promise<Array>} - List of seller products
     */
    getSellerProductsBySellerId: (sellerId) => fetchApi(`/seller-product/query/seller/${sellerId}`),

    /**
     * Get sellers by product ID
     * @param {string} productId - Product ID
     * @returns {Promise<Array>} - List of sellers
     */
    getSellersByProductId: (productId) => fetchApi(`/seller-product/query/product/${productId}/sellers`),

    /**
     * Get products by seller ID
     * @param {string} sellerId - Seller ID
     * @returns {Promise<Array>} - List of products
     */
    getProductsBySellerId: (sellerId) => fetchApi(`/seller-product/query/seller/${sellerId}/products`)
};

/**
 * Product API
 */
const productApi = {
    /**
     * Get all products
     * @returns {Promise<Array>} - List of products
     */
    getAllProducts: () => fetchApi('/product/query', {}, 'products'),

    /**
     * Get product by ID
     * @param {string} id - Product ID
     * @returns {Promise<Object>} - Product
     */
    getProductById: (id) => fetchApi(`/product/query/${id}`)
};

/**
 * Category API
 */
const categoryApi = {
    /**
     * Get all categories
     * @returns {Promise<Array>} - List of categories
     */
    getAllCategories: () => fetchApi('/category/query', {}, 'categories'),

    /**
     * Get category by ID
     * @param {string} id - Category ID
     * @returns {Promise<Object>} - Category
     */
    getCategoryById: (id) => fetchApi(`/category/query/${id}`)
};

/**
 * Product Channel API
 */
const productChannelApi = {
    /**
     * Get all product channels
     * @returns {Promise<Array>} - List of product channels
     */
    getAllProductChannels: () => fetchApi('/product-channel/query'),

    /**
     * Get product channel by ID
     * @param {string} id - Product channel ID
     * @returns {Promise<Object>} - Product channel
     */
    getProductChannelById: (id) => fetchApi(`/product-channel/query/${id}`),

    /**
     * Get product channels by product ID
     * @param {string} productId - Product ID
     * @returns {Promise<Array>} - List of product channels
     */
    getProductChannelsByProductId: (productId) => fetchApi(`/product-channel/query/product/${productId}`),

    /**
     * Get product channels by channel ID
     * @param {string} channelId - Channel ID
     * @returns {Promise<Array>} - List of product channels
     */
    getProductChannelsByChannelId: (channelId) => fetchApi(`/product-channel/query/channel/${channelId}`)
};

/**
 * Channel Catalog API
 */
const channelCatalogApi = {
    /**
     * Get all channel catalogs
     * @returns {Promise<Array>} - List of channel catalogs
     */
    getAllChannelCatalogs: () => fetchApi('/channel-catalog/query'),

    /**
     * Get channel catalog by ID
     * @param {string} id - Channel catalog ID
     * @returns {Promise<Object>} - Channel catalog
     */
    getChannelCatalogById: (id) => fetchApi(`/channel-catalog/query/${id}`),

    /**
     * Get channel catalogs by channel ID
     * @param {string} channelId - Channel ID
     * @returns {Promise<Array>} - List of channel catalogs
     */
    getChannelCatalogsByChannelId: (channelId) => fetchApi(`/channel-catalog/query/channel/${channelId}`)
};

/**
 * Product Platform API
 */
const productPlatformApi = {
    /**
     * Get all product platforms
     * @returns {Promise<Array>} - List of product platforms
     */
    getAllProductPlatforms: () => fetchApi('/product-platform/query'),

    /**
     * Get product platform by ID
     * @param {string} id - Product platform ID
     * @returns {Promise<Object>} - Product platform
     */
    getProductPlatformById: (id) => fetchApi(`/product-platform/query/${id}`),

    /**
     * Get product platforms by product ID
     * @param {string} productId - Product ID
     * @returns {Promise<Array>} - List of product platforms
     */
    getProductPlatformsByProductId: (productId) => fetchApi(`/product-platform/query/product/${productId}`)
};

/**
 * Product Feature Value Mapping API
 */
const productFeatureValueMappingApi = {
    /**
     * Get all product feature value mappings
     * @returns {Promise<Array>} - List of product feature value mappings
     */
    getAllProductFeatureValueMappings: () => fetchApi('/product-feature-value-mapping/query'),

    /**
     * Get product feature value mapping by ID
     * @param {string} id - Product feature value mapping ID
     * @returns {Promise<Object>} - Product feature value mapping
     */
    getProductFeatureValueMappingById: (id) => fetchApi(`/product-feature-value-mapping/query/${id}`),

    /**
     * Get product feature value mappings by product ID
     * @param {string} productId - Product ID
     * @returns {Promise<Array>} - List of product feature value mappings
     */
    getProductFeatureValueMappingsByProductId: (productId) => fetchApi(`/product-feature-value-mapping/query/product/${productId}`)
};

// Export all API services
window.api = {
    productCategory: productCategoryApi,
    sellerProduct: sellerProductApi,
    product: productApi,
    category: categoryApi,
    productChannel: productChannelApi,
    channelCatalog: channelCatalogApi,
    productPlatform: productPlatformApi,
    productFeatureValueMapping: productFeatureValueMappingApi
};