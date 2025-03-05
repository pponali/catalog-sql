import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query/react';

// Define the base URL for the auth API
const baseUrl = process.env.REACT_APP_API_URL || 'http://localhost:8080/api';

// Define user interface
export interface User {
  id: string;
  name: string;
  email: string;
  roles: string[];
  avatar?: string;
}

// Define login request interface
export interface LoginRequest {
  email: string;
  password: string;
}

// Define login response interface
export interface LoginResponse {
  user: User;
  token: string;
  refreshToken: string;
}

// Define forgot password request interface
export interface ForgotPasswordRequest {
  email: string;
}

// Define reset password request interface
export interface ResetPasswordRequest {
  token: string;
  newPassword: string;
  confirmPassword: string;
}

/**
 * Auth API provides endpoints for authentication-related operations.
 * This includes login, logout, password reset, and user profile management.
 */
export const authApi = createApi({
  reducerPath: 'authApi',
  baseQuery: fetchBaseQuery({
    baseUrl: `${baseUrl}/auth`,
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
  endpoints: (builder) => ({
    // Login endpoint
    login: builder.mutation<LoginResponse, LoginRequest>({
      query: (credentials) => ({
        url: 'login',
        method: 'POST',
        body: credentials,
      }),
    }),
    
    // Logout endpoint
    logout: builder.mutation<void, void>({
      query: () => ({
        url: 'logout',
        method: 'POST',
      }),
    }),
    
    // Get current user endpoint
    getCurrentUser: builder.query<User, void>({
      query: () => 'me',
    }),
    
    // Refresh token endpoint
    refreshToken: builder.mutation<{ token: string; refreshToken: string }, string>({
      query: (refreshToken) => ({
        url: 'refresh-token',
        method: 'POST',
        body: { refreshToken },
      }),
    }),
    
    // Forgot password endpoint
    forgotPassword: builder.mutation<{ message: string }, ForgotPasswordRequest>({
      query: (data) => ({
        url: 'forgot-password',
        method: 'POST',
        body: data,
      }),
    }),
    
    // Reset password endpoint
    resetPassword: builder.mutation<{ message: string }, ResetPasswordRequest>({
      query: (data) => ({
        url: 'reset-password',
        method: 'POST',
        body: data,
      }),
    }),
    
    // Update user profile endpoint
    updateProfile: builder.mutation<User, Partial<User>>({
      query: (data) => ({
        url: 'profile',
        method: 'PATCH',
        body: data,
      }),
    }),
    
    // Change password endpoint
    changePassword: builder.mutation<{ message: string }, { currentPassword: string; newPassword: string; confirmPassword: string }>({
      query: (data) => ({
        url: 'change-password',
        method: 'POST',
        body: data,
      }),
    }),
  }),
});

// Export hooks for using the API endpoints
export const {
  useLoginMutation,
  useLogoutMutation,
  useGetCurrentUserQuery,
  useRefreshTokenMutation,
  useForgotPasswordMutation,
  useResetPasswordMutation,
  useUpdateProfileMutation,
  useChangePasswordMutation,
} = authApi;