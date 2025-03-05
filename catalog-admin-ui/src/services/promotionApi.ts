import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query/react';
import { RootState } from '../store/store';

// Define types for our API
export interface Promotion {
  id: string;
  name: string;
  description: string;
  type: 'PERCENTAGE' | 'FIXED_AMOUNT' | 'BUY_X_GET_Y' | 'BUNDLE';
  value: number;
  startDate: string;
  endDate: string;
  status: 'ACTIVE' | 'INACTIVE' | 'SCHEDULED' | 'EXPIRED';
  conditions: PromotionCondition[];
  channels: string[];
  createdAt: string;
  updatedAt: string;
}

export interface PromotionCondition {
  id: string;
  promotionId: string;
  type: 'PRODUCT' | 'CATEGORY' | 'CART_VALUE' | 'CUSTOMER_GROUP';
  value: string;
  operator: 'EQUALS' | 'NOT_EQUALS' | 'GREATER_THAN' | 'LESS_THAN' | 'IN' | 'NOT_IN';
}

// Define pagination params
export interface PaginationParams {
  page?: number;
  size?: number;
  sort?: string;
}

// Define filter params
export interface PromotionFilterParams extends PaginationParams {
  name?: string;
  type?: string;
  status?: string;
  channelId?: string;
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

export const promotionApi = createApi({
  reducerPath: 'promotionApi',
  baseQuery: fetchBaseQuery({
    baseUrl: `${process.env.REACT_APP_API_URL || '/api'}/promotion`,
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
  tagTypes: ['Promotion'],
  endpoints: (builder) => ({
    // Promotion endpoints
    getPromotions: builder.query<PaginatedResponse<Promotion>, PromotionFilterParams>({
      query: (params) => ({
        url: '/promotions',
        params,
      }),
      providesTags: (result) =>
        result
          ? [
              ...result.content.map(({ id }) => ({ type: 'Promotion' as const, id })),
              { type: 'Promotion', id: 'LIST' },
            ]
          : [{ type: 'Promotion', id: 'LIST' }],
    }),
    getPromotionById: builder.query<Promotion, string>({
      query: (id) => `/promotions/${id}`,
      providesTags: (result, error, id) => [{ type: 'Promotion', id }],
    }),
    getActivePromotions: builder.query<Promotion[], void>({
      query: () => '/promotions/active',
      providesTags: [{ type: 'Promotion', id: 'ACTIVE' }],
    }),
    getPromotionsByChannel: builder.query<Promotion[], string>({
      query: (channelId) => `/promotions/channel/${channelId}`,
      providesTags: [{ type: 'Promotion', id: 'LIST' }],
    }),
    createPromotion: builder.mutation<Promotion, Partial<Promotion>>({
      query: (promotion) => ({
        url: '/promotions',
        method: 'POST',
        body: promotion,
      }),
      invalidatesTags: [
        { type: 'Promotion', id: 'LIST' },
        { type: 'Promotion', id: 'ACTIVE' },
      ],
    }),
    updatePromotion: builder.mutation<Promotion, { id: string; promotion: Partial<Promotion> }>({
      query: ({ id, promotion }) => ({
        url: `/promotions/${id}`,
        method: 'PUT',
        body: promotion,
      }),
      invalidatesTags: (result, error, { id }) => [
        { type: 'Promotion', id },
        { type: 'Promotion', id: 'LIST' },
        { type: 'Promotion', id: 'ACTIVE' },
      ],
    }),
    deletePromotion: builder.mutation<void, string>({
      query: (id) => ({
        url: `/promotions/${id}`,
        method: 'DELETE',
      }),
      invalidatesTags: [
        { type: 'Promotion', id: 'LIST' },
        { type: 'Promotion', id: 'ACTIVE' },
      ],
    }),
    activatePromotion: builder.mutation<void, string>({
      query: (id) => ({
        url: `/promotions/${id}/activate`,
        method: 'PUT',
      }),
      invalidatesTags: (result, error, id) => [
        { type: 'Promotion', id },
        { type: 'Promotion', id: 'LIST' },
        { type: 'Promotion', id: 'ACTIVE' },
      ],
    }),
    deactivatePromotion: builder.mutation<void, string>({
      query: (id) => ({
        url: `/promotions/${id}/deactivate`,
        method: 'PUT',
      }),
      invalidatesTags: (result, error, id) => [
        { type: 'Promotion', id },
        { type: 'Promotion', id: 'LIST' },
        { type: 'Promotion', id: 'ACTIVE' },
      ],
    }),
  }),
});

export const {
  useGetPromotionsQuery,
  useGetPromotionByIdQuery,
  useGetActivePromotionsQuery,
  useGetPromotionsByChannelQuery,
  useCreatePromotionMutation,
  useUpdatePromotionMutation,
  useDeletePromotionMutation,
  useActivatePromotionMutation,
  useDeactivatePromotionMutation,
} = promotionApi;