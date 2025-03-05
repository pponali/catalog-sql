import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query/react';
import { RootState } from '../store/store';

// Define types for our API
export interface User {
  id: string;
  username: string;
  email: string;
  firstName: string;
  lastName: string;
  role: string;
  status: 'ACTIVE' | 'INACTIVE' | 'LOCKED';
  permissions: string[];
  createdAt: string;
  updatedAt: string;
}

export interface Merchant {
  id: string;
  name: string;
  code: string;
  description: string;
  status: 'ACTIVE' | 'INACTIVE' | 'PENDING';
  contactEmail: string;
  contactPhone: string;
  address: Address;
  createdAt: string;
  updatedAt: string;
}

export interface Address {
  street: string;
  city: string;
  state: string;
  country: string;
  postalCode: string;
}

// Define pagination params
export interface PaginationParams {
  page?: number;
  size?: number;
  sort?: string;
}

// Define filter params
export interface UserFilterParams extends PaginationParams {
  username?: string;
  email?: string;
  role?: string;
  status?: string;
}

export interface MerchantFilterParams extends PaginationParams {
  name?: string;
  code?: string;
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

export const userApi = createApi({
  reducerPath: 'userApi',
  baseQuery: fetchBaseQuery({
    baseUrl: `${process.env.REACT_APP_API_URL || '/api'}/user`,
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
  tagTypes: ['User', 'Merchant'],
  endpoints: (builder) => ({
    // User endpoints
    getUsers: builder.query<PaginatedResponse<User>, UserFilterParams>({
      query: (params) => ({
        url: '/users',
        params,
      }),
      providesTags: (result) =>
        result
          ? [
              ...result.content.map(({ id }) => ({ type: 'User' as const, id })),
              { type: 'User', id: 'LIST' },
            ]
          : [{ type: 'User', id: 'LIST' }],
    }),
    getUserById: builder.query<User, string>({
      query: (id) => `/users/${id}`,
      providesTags: (result, error, id) => [{ type: 'User', id }],
    }),
    createUser: builder.mutation<User, Partial<User>>({
      query: (user) => ({
        url: '/users',
        method: 'POST',
        body: user,
      }),
      invalidatesTags: [{ type: 'User', id: 'LIST' }],
    }),
    updateUser: builder.mutation<User, { id: string; user: Partial<User> }>({
      query: ({ id, user }) => ({
        url: `/users/${id}`,
        method: 'PUT',
        body: user,
      }),
      invalidatesTags: (result, error, { id }) => [
        { type: 'User', id },
        { type: 'User', id: 'LIST' },
      ],
    }),
    deleteUser: builder.mutation<void, string>({
      query: (id) => ({
        url: `/users/${id}`,
        method: 'DELETE',
      }),
      invalidatesTags: [{ type: 'User', id: 'LIST' }],
    }),
    
    // Merchant endpoints
    getMerchants: builder.query<PaginatedResponse<Merchant>, MerchantFilterParams>({
      query: (params) => ({
        url: '/merchants',
        params,
      }),
      providesTags: (result) =>
        result
          ? [
              ...result.content.map(({ id }) => ({ type: 'Merchant' as const, id })),
              { type: 'Merchant', id: 'LIST' },
            ]
          : [{ type: 'Merchant', id: 'LIST' }],
    }),
    getMerchantById: builder.query<Merchant, string>({
      query: (id) => `/merchants/${id}`,
      providesTags: (result, error, id) => [{ type: 'Merchant', id }],
    }),
    createMerchant: builder.mutation<Merchant, Partial<Merchant>>({
      query: (merchant) => ({
        url: '/merchants',
        method: 'POST',
        body: merchant,
      }),
      invalidatesTags: [{ type: 'Merchant', id: 'LIST' }],
    }),
    updateMerchant: builder.mutation<Merchant, { id: string; merchant: Partial<Merchant> }>({
      query: ({ id, merchant }) => ({
        url: `/merchants/${id}`,
        method: 'PUT',
        body: merchant,
      }),
      invalidatesTags: (result, error, { id }) => [
        { type: 'Merchant', id },
        { type: 'Merchant', id: 'LIST' },
      ],
    }),
    deleteMerchant: builder.mutation<void, string>({
      query: (id) => ({
        url: `/merchants/${id}`,
        method: 'DELETE',
      }),
      invalidatesTags: [{ type: 'Merchant', id: 'LIST' }],
    }),
    activateMerchant: builder.mutation<void, string>({
      query: (id) => ({
        url: `/merchants/${id}/activate`,
        method: 'PUT',
      }),
      invalidatesTags: (result, error, id) => [
        { type: 'Merchant', id },
        { type: 'Merchant', id: 'LIST' },
      ],
    }),
    deactivateMerchant: builder.mutation<void, string>({
      query: (id) => ({
        url: `/merchants/${id}/deactivate`,
        method: 'PUT',
      }),
      invalidatesTags: (result, error, id) => [
        { type: 'Merchant', id },
        { type: 'Merchant', id: 'LIST' },
      ],
    }),
  }),
});

export const {
  // User hooks
  useGetUsersQuery,
  useGetUserByIdQuery,
  useCreateUserMutation,
  useUpdateUserMutation,
  useDeleteUserMutation,
  
  // Merchant hooks
  useGetMerchantsQuery,
  useGetMerchantByIdQuery,
  useCreateMerchantMutation,
  useUpdateMerchantMutation,
  useDeleteMerchantMutation,
  useActivateMerchantMutation,
  useDeactivateMerchantMutation,
} = userApi;