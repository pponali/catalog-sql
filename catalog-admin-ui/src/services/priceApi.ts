import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query/react';
import { RootState } from '../store/store';

// Define types for our API
export interface Price {
  id: string;
  productId: string;
  channelId: string;
  merchantId: string;
  basePrice: number;
  salePrice: number;
  currencyCode: string;
  startDate: string;
  endDate: string;
  status: 'ACTIVE' | 'INACTIVE';
  createdAt: string;
  updatedAt: string;
}

// Define pagination params
export interface PaginationParams {
  page?: number;
  size?: number;
  sort?: string;
}

// Define filter params
export interface PriceFilterParams extends PaginationParams {
  productId?: string;
  channelId?: string;
  merchantId?: string;
  status?: string;
}

// Define response types
export interface PaginatedResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
  first: boolean;
  last: boolean;
}

export const priceApi = createApi({
  reducerPath: 'priceApi',
  baseQuery: fetchBaseQuery({
    baseUrl: `${process.env.REACT_APP_API_URL || '/api'}/price`,
    prepareHeaders: (headers, { getState }) => {
      // Get the token from the state
      const token = (getState() as RootState).auth.token;
      
      // If we have a token, add it to the headers
      if (token) {
        headers.set('authorization', `Bearer ${token}`);
      }
      
      return headers;
    },
  }),
  tagTypes: ['Price'],
  endpoints: (builder) => ({
    // Price endpoints
    getPrices: builder.query<PaginatedResponse<Price>, PriceFilterParams>({
      query: (params) => ({
        url: '/prices',
        params,
      }),
      providesTags: (result) =>
        result
          ? [
              ...result.content.map(({ id }) => ({ type: 'Price' as const, id })),
              { type: 'Price', id: 'LIST' },
            ]
          : [{ type: 'Price', id: 'LIST' }],
    }),
    getPriceById: builder.query<Price, string>({
      query: (id) => `/prices/${id}`,
      providesTags: (result, error, id) => [{ type: 'Price', id }],
    }),
    getPricesByProductId: builder.query<Price[], string>({
      query: (productId) => `/prices/product/${productId}`,
      providesTags: (result) =>
        result
          ? [
              ...result.map(({ id }) => ({ type: 'Price' as const, id })),
              { type: 'Price', id: 'LIST' },
            ]
          : [{ type: 'Price', id: 'LIST' }],
    }),
    createPrice: builder.mutation<Price, Partial<Price>>({
      query: (price) => ({
        url: '/prices',
        method: 'POST',
        body: price,
      }),
      invalidatesTags: [{ type: 'Price', id: 'LIST' }],
    }),
    updatePrice: builder.mutation<Price, { id: string; price: Partial<Price> }>({
      query: ({ id, price }) => ({
        url: `/prices/${id}`,
        method: 'PUT',
        body: price,
      }),
      invalidatesTags: (result, error, { id }) => [
        { type: 'Price', id },
        { type: 'Price', id: 'LIST' },
      ],
    }),
    deletePrice: builder.mutation<void, string>({
      query: (id) => ({
        url: `/prices/${id}`,
        method: 'DELETE',
      }),
      invalidatesTags: [{ type: 'Price', id: 'LIST' }],
    }),
    bulkUpdatePrices: builder.mutation<void, Partial<Price>[]>({
      query: (prices) => ({
        url: '/prices/bulk',
        method: 'PUT',
        body: prices,
      }),
      invalidatesTags: [{ type: 'Price', id: 'LIST' }],
    }),
  }),
});

export const {
  useGetPricesQuery,
  useGetPriceByIdQuery,
  useGetPricesByProductIdQuery,
  useCreatePriceMutation,
  useUpdatePriceMutation,
  useDeletePriceMutation,
  useBulkUpdatePricesMutation,
} = priceApi;