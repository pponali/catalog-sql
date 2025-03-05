import React from 'react';
import { Navigate, useLocation } from 'react-router-dom';
import { useSelector } from 'react-redux';
import { CircularProgress, Box } from '@mui/material';
import { selectIsAuthenticated, selectIsLoading } from '../../store/slices/authSlice';

interface ProtectedRouteProps {
  children: React.ReactNode;
  requiredRoles?: string[];
}

/**
 * ProtectedRoute component ensures that only authenticated users can access certain routes.
 * It can also check for specific user roles if provided.
 * 
 * @param {React.ReactNode} children - The components to render if the user is authenticated
 * @param {string[]} requiredRoles - Optional array of roles required to access the route
 */
const ProtectedRoute: React.FC<ProtectedRouteProps> = ({ children, requiredRoles }) => {
  const location = useLocation();
  const isAuthenticated = useSelector(selectIsAuthenticated);
  const isLoading = useSelector(selectIsLoading);
  
  // Get user from Redux store
  const user = useSelector((state: any) => state.auth.user);
  
  // Show loading spinner while checking authentication
  if (isLoading) {
    return (
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
  }
  
  // If user is not authenticated, redirect to login page
  if (!isAuthenticated) {
    // Save the location the user was trying to access for redirect after login
    return <Navigate to="/login" state={{ from: location }} replace />;
  }
  
  // If specific roles are required, check if the user has at least one of them
  if (requiredRoles && requiredRoles.length > 0) {
    const userRoles = user?.roles || [];
    const hasRequiredRole = requiredRoles.some(role => userRoles.includes(role));
    
    if (!hasRequiredRole) {
      // User doesn't have the required role, redirect to unauthorized page
      return <Navigate to="/unauthorized" replace />;
    }
  }
  
  // User is authenticated and has the required roles (if any), render the protected content
  return <>{children}</>;
};

export default ProtectedRoute;