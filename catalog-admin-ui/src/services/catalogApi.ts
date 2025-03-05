import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query/react';

// Define the base URL for the catalog API
const baseUrl = process.env.REACT_APP_API_URL || 'http://localhost:8080/api';

// Define product interface
export interface Product {
  id: string;
  code: string;
  name: string;
  description: string;
  status: string;
  category: string;
  price: number;
  imageUrl?: string;
  attributes?: Record<string, any>;
  variants?: Product[];
  createdAt: string;
  updatedAt: string;
}

// Define category interface
export interface Category {
  id: string;
  name: string;
  description?: string;
  parentId?: string;
  level: number;
  path: string;
  status: string;
  attributes?: string[];
  imageUrl?: string;
  createdAt: string;
  updatedAt: string;
}

// Define attribute option interface
export interface AttributeOption {
  value: string;
  label: string;
}

// Define attribute interface
export interface Attribute {
  id: string;
  name: string;
  code: string;
  description?: string;
  type: 'text' | 'number' | 'boolean' | 'date' | 'select' | 'multiselect';
  required: boolean;
  options?: AttributeOption[];
  defaultValue?: any;
  validation?: string;
  createdAt: string;
  updatedAt: string;
}

// Define product creation request interface
export interface CreateProductRequest {
  code: string;
  name: string;
  description: string;
  status: string;
  categoryId: string;
  price: number;
  imageUrl?: string;
  attributes?: Record<string, any>;
}

// Define product update request interface
export interface UpdateProductRequest {
  code?: string;
  name?: string;
  description?: string;
  status?: string;
  categoryId?: string;
  price?: number;
  imageUrl?: string;
  attributes?: Record<string, any>;
}

// Define category creation request interface
export interface CreateCategoryRequest {
  name: string;
  description?: string;
  parentId?: string;
  status: string;
  attributes?: string[];
  imageUrl?: string;
}

// Define category update request interface
export interface UpdateCategoryRequest {
  name?: string;
  description?: string;
  parentId?: string;
  status?: string;
  attributes?: string[];
  imageUrl?: string;
}

// Define attribute creation request interface
export interface CreateAttributeRequest {
  name: string;
  code: string;
  description?: string;
  type: 'text' | 'number' | 'boolean' | 'date' | 'select' | 'multiselect';
  required: boolean;
  options?: AttributeOption[];
  defaultValue?: any;
  validation?: string;
}

// Define attribute update request interface
export interface UpdateAttributeRequest {
  name?: string;
  description?: string;
  type?: 'text' | 'number' | 'boolean' | 'date' | 'select' | 'multiselect';
  required?: boolean;
  options?: AttributeOption[];
  defaultValue?: any;
  validation?: string;
}

/**
 * Catalog API provides endpoints for catalog management operations.
 * This includes products, categories, and attributes management.
 */
export const catalogApi = createApi({
  reducerPath: 'catalogApi',
  baseQuery: fetchBaseQuery({
    baseUrl: `${baseUrl}/catalog`,
    prepareHeaders: (headers, { getState }) => {
      // Get the token from the state
      const token = (getState() as any).auth.token;
      
      // If we have a token, add it to the headers
      if (token) {
        headers.set('Authorization', `Bearer ${token}`);
      }
      
      return headers;
    },
  }),
  tagTypes: ['Product', 'Category', 'Attribute'],
  endpoints: (builder) => ({
    // Product endpoints
    getProducts: builder.query<Product[], void>({
      query: () => 'products',
      providesTags: (result) =>
        result
          ? [
              ...result.map(({ id }) => ({ type: 'Product' as const, id })),
              { type: 'Product', id: 'LIST' },
            ]
          : [{ type: 'Product', id: 'LIST' }],
    }),
    
    getProduct: builder.query<Product, string>({
      query: (id) => `products/${id}`,
      providesTags: (result, error, id) => [{ type: 'Product', id }],
    }),
    
    createProduct: builder.mutation<Product, CreateProductRequest>({
      query: (product) => ({
        url: 'products',
        method: 'POST',
        body: product,
      }),
      invalidatesTags: [{ type: 'Product', id: 'LIST' }],
    }),
    
    updateProduct: builder.mutation<Product, { id: string; product: UpdateProductRequest }>({
      query: ({ id, product }) => ({
        url: `products/${id}`,
        method: 'PUT',
        body: product,
      }),
      invalidatesTags: (result, error, { id }) => [{ type: 'Product', id }],
    }),
    
    deleteProduct: builder.mutation<void, string>({
      query: (id) => ({
        url: `products/${id}`,
        method: 'DELETE',
      }),
      invalidatesTags: (result, error, id) => [
        { type: 'Product', id },
        { type: 'Product', id: 'LIST' },
      ],
    }),
    
    // Category endpoints
    getCategories: builder.query<Category[], void>({
      query: () => 'categories',
      providesTags: (result) =>
        result
          ? [
              ...result.map(({ id }) => ({ type: 'Category' as const, id })),
              { type: 'Category', id: 'LIST' },
            ]
          : [{ type: 'Category', id: 'LIST' }],
    }),
    
    getCategory: builder.query<Category, string>({
      query: (id) => `categories/${id}`,
      providesTags: (result, error, id) => [{ type: 'Category', id }],
    }),
    
    createCategory: builder.mutation<Category, CreateCategoryRequest>({
      query: (category) => ({
        url: 'categories',
        method: 'POST',
        body: category,
      }),
      invalidatesTags: [{ type: 'Category', id: 'LIST' }],
    }),
    
    updateCategory: builder.mutation<Category, { id: string; category: UpdateCategoryRequest }>({
      query: ({ id, category }) => ({
        url: `categories/${id}`,
        method: 'PUT',
        body: category,
      }),
      invalidatesTags: (result, error, { id }) => [{ type: 'Category', id }],
    }),
    
    deleteCategory: builder.mutation<void, string>({
      query: (id) => ({
        url: `categories/${id}`,
        method: 'DELETE',
      }),
      invalidatesTags: (result, error, id) => [
        { type: 'Category', id },
        { type: 'Category', id: 'LIST' },
      ],
    }),
    
    // Attribute endpoints
    getAttributes: builder.query<Attribute[], void>({
      query: () => 'attributes',
      providesTags: (result) =>
        result
          ? [
              ...result.map(({ id }) => ({ type: 'Attribute' as const, id })),
              { type: 'Attribute', id: 'LIST' },
            ]
          : [{ type: 'Attribute', id: 'LIST' }],
    }),
    
    getAttribute: builder.query<Attribute, string>({
      query: (id) => `attributes/${id}`,
      providesTags: (result, error, id) => [{ type: 'Attribute', id }],
    }),
    
    createAttribute: builder.mutation<Attribute, CreateAttributeRequest>({
      query: (attribute) => ({
        url: 'attributes',
        method: 'POST',
        body: attribute,
      }),
      invalidatesTags: [{ type: 'Attribute', id: 'LIST' }],
    }),
    
    updateAttribute: builder.mutation<Attribute, { id: string; attribute: UpdateAttributeRequest }>({
      query: ({ id, attribute }) => ({
        url: `attributes/${id}`,
        method: 'PUT',
        body: attribute,
      }),
      invalidatesTags: (result, error, { id }) => [{ type: 'Attribute', id }],
    }),
    
    deleteAttribute: builder.mutation<void, string>({
      query: (id) => ({
        url: `attributes/${id}`,
        method: 'DELETE',
      }),
      invalidatesTags: (result, error, id) => [
        { type: 'Attribute', id },
        { type: 'Attribute', id: 'LIST' },
      ],
    }),
    
    // Product bulk operations
    bulkCreateProducts: builder.mutation<Product[], CreateProductRequest[]>({
      query: (products) => ({
        url: 'products/bulk',
        method: 'POST',
        body: products,
      }),
      invalidatesTags: [{ type: 'Product', id: 'LIST' }],
    }),
    
    bulkUpdateProducts: builder.mutation<Product[], { id: string; product: UpdateProductRequest }[]>({
      query: (updates) => ({
        url: 'products/bulk',
        method: 'PUT',
        body: updates,
      }),
      invalidatesTags: (result, error, updates) => [
        ...updates.map(({ id }) => ({ type: 'Product' as const, id })),
        { type: 'Product', id: 'LIST' },
      ],
    }),
    
    bulkDeleteProducts: builder.mutation<void, string[]>({
      query: (ids) => ({
        url: 'products/bulk',
        method: 'DELETE',
        body: { ids },
      }),
      invalidatesTags: (result, error, ids) => [
        ...ids.map((id) => ({ type: 'Product' as const, id })),
        { type: 'Product', id: 'LIST' },
      ],
    }),
  }),
});

// Export hooks for using the API endpoints
export const {
  useGetProductsQuery,
  useGetProductQuery,
  useCreateProductMutation,
  useUpdateProductMutation,
  useDeleteProductMutation,
  useGetCategoriesQuery,
  useGetCategoryQuery,
  useCreateCategoryMutation,
  useUpdateCategoryMutation,
  useDeleteCategoryMutation,
  useGetAttributesQuery,
  useGetAttributeQuery,
  useCreateAttributeMutation,
  useUpdateAttributeMutation,
  useDeleteAttributeMutation,
  useBulkCreateProductsMutation,
  useBulkUpdateProductsMutation,
  useBulkDeleteProductsMutation,
} = catalogApi;