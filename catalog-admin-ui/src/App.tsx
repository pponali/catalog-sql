import React, { useState, useEffect, Suspense } from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import { useSelector, useDispatch } from 'react-redux';
import { CircularProgress, Box } from '@mui/material';

import { theme, toggleColorMode } from './config/theme';
import { selectIsAuthenticated, selectCurrentUser } from './store/slices/authSlice';
import { setDarkMode, selectDarkMode } from './store/slices/uiSlice';

// Layout components
import MainLayout from './components/layout/MainLayout';
import ErrorBoundary from './components/common/ErrorBoundary';
import ProtectedRoute from './components/common/ProtectedRoute';

// Lazy load page components for better performance
const Login = React.lazy(() => import('./pages/auth/Login'));
const ForgotPassword = React.lazy(() => import('./pages/auth/ForgotPassword'));
const ResetPassword = React.lazy(() => import('./pages/auth/ResetPassword'));
const Dashboard = React.lazy(() => import('./pages/Dashboard'));
const NotFound = React.lazy(() => import('./pages/NotFound'));

// Catalog module
const ProductList = React.lazy(() => import('./pages/catalog/ProductList'));
const ProductDetail = React.lazy(() => import('./pages/catalog/ProductDetail'));
const CategoryList = React.lazy(() => import('./pages/catalog/CategoryList'));
const CategoryDetail = React.lazy(() => import('./pages/catalog/CategoryDetail'));
const AttributeList = React.lazy(() => import('./pages/catalog/AttributeList'));
const AttributeDetail = React.lazy(() => import('./pages/catalog/AttributeDetail'));

// Price module
const PriceList = React.lazy(() => import('./pages/price/PriceList'));
const PriceDetail = React.lazy(() => import('./pages/price/PriceDetail'));

// Promotion module
const PromotionList = React.lazy(() => import('./pages/promotion/PromotionList'));
const PromotionDetail = React.lazy(() => import('./pages/promotion/PromotionDetail'));

// User module
const UserList = React.lazy(() => import('./pages/user/UserList'));
const UserDetail = React.lazy(() => import('./pages/user/UserDetail'));
const MerchantList = React.lazy(() => import('./pages/user/MerchantList'));
const MerchantDetail = React.lazy(() => import('./pages/user/MerchantDetail'));

// Reports module
const Reports = React.lazy(() => import('./pages/reports/Reports'));

// System module
const Settings = React.lazy(() => import('./pages/system/Settings'));

// Loading component for suspense fallback
const LoadingFallback = () => (
  <Box
    sx={{
      display: 'flex',
      justifyContent: 'center',
      alignItems: 'center',
      height: '100vh',
    }}
  >
    <CircularProgress size={60} thickness={4} />
  </Box>
);

/**
 * Main App component that defines the routes and layout of the application.
 * It handles authentication, theme switching, and routing.
 */
const App: React.FC = () => {
  const dispatch = useDispatch();
  const isAuthenticated = useSelector(selectIsAuthenticated);
  const user = useSelector(selectCurrentUser);
  const darkMode = useSelector(selectDarkMode);
  
  // State for the current theme
  const [currentTheme, setCurrentTheme] = useState(theme);
  
  // Effect to handle theme changes
  useEffect(() => {
    if (darkMode !== currentTheme.palette.mode === 'dark') {
      const newTheme = toggleColorMode(currentTheme);
      setCurrentTheme(newTheme);
    }
  }, [darkMode, currentTheme]);
  
  // Effect to initialize dark mode from localStorage or system preference
  useEffect(() => {
    const storedTheme = localStorage.getItem(process.env.REACT_APP_THEME_STORAGE_KEY || 'tata_commerce_theme');
    
    if (storedTheme) {
      dispatch(setDarkMode(storedTheme === 'dark'));
    } else if (window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches) {
      dispatch(setDarkMode(true));
    }
  }, [dispatch]);

  return (
    <ErrorBoundary>
      <Suspense fallback={<LoadingFallback />}>
        <Routes>
          {/* Auth routes */}
          <Route
            path="/login"
            element={
              isAuthenticated ? (
                <Navigate to="/dashboard" replace />
              ) : (
                <Login />
              )
            }
          />
          <Route path="/forgot-password" element={<ForgotPassword />} />
          <Route path="/reset-password" element={<ResetPassword />} />
          
          {/* Protected routes */}
          <Route
            path="/"
            element={
              <ProtectedRoute>
                <MainLayout />
              </ProtectedRoute>
            }
          >
            <Route index element={<Navigate to="/dashboard" replace />} />
            <Route path="dashboard" element={<Dashboard />} />
            
            {/* Catalog routes */}
            <Route path="catalog">
              <Route index element={<Navigate to="/catalog/products" replace />} />
              <Route path="products" element={<ProductList />} />
              <Route path="products/:id" element={<ProductDetail />} />
              <Route path="categories" element={<CategoryList />} />
              <Route path="categories/:id" element={<CategoryDetail />} />
              <Route path="attributes" element={<AttributeList />} />
              <Route path="attributes/:id" element={<AttributeDetail />} />
            </Route>
            
            {/* Price routes */}
            <Route path="price">
              <Route index element={<PriceList />} />
              <Route path=":id" element={<PriceDetail />} />
            </Route>
            
            {/* Promotion routes */}
            <Route path="promotion">
              <Route index element={<PromotionList />} />
              <Route path=":id" element={<PromotionDetail />} />
            </Route>
            
            {/* User routes */}
            <Route path="user">
              <Route index element={<Navigate to="/user/users" replace />} />
              <Route path="users" element={<UserList />} />
              <Route path="users/:id" element={<UserDetail />} />
              <Route path="merchants" element={<MerchantList />} />
              <Route path="merchants/:id" element={<MerchantDetail />} />
            </Route>
            
            {/* Reports routes */}
            <Route path="reports" element={<Reports />} />
            
            {/* System routes */}
            <Route path="system" element={<Settings />} />
          </Route>
          
          {/* 404 route */}
          <Route path="*" element={<NotFound />} />
        </Routes>
      </Suspense>
    </ErrorBoundary>
  );
};

export default App;