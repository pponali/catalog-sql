import React, { useEffect } from 'react';
import { useLocation, Link as RouterLink } from 'react-router-dom';
import { useSelector, useDispatch } from 'react-redux';
import {
  Breadcrumbs as MuiBreadcrumbs,
  Link,
  Typography,
  Box,
  Chip,
} from '@mui/material';
import {
  Home as HomeIcon,
  NavigateNext as NavigateNextIcon,
} from '@mui/icons-material';
import { selectBreadcrumbs, setBreadcrumbs, BreadcrumbItem } from '../../store/slices/uiSlice';

// Define route to breadcrumb mapping
interface RouteBreadcrumb {
  path: string;
  breadcrumb: string;
  icon?: React.ReactNode;
}

// Define route to breadcrumb mappings
const routeBreadcrumbMap: RouteBreadcrumb[] = [
  { path: '/dashboard', breadcrumb: 'Dashboard', icon: <HomeIcon fontSize="small" /> },
  { path: '/catalog', breadcrumb: 'Catalog' },
  { path: '/catalog/products', breadcrumb: 'Products' },
  { path: '/catalog/categories', breadcrumb: 'Categories' },
  { path: '/catalog/attributes', breadcrumb: 'Attributes' },
  { path: '/price', breadcrumb: 'Price Management' },
  { path: '/promotion', breadcrumb: 'Promotions' },
  { path: '/user', breadcrumb: 'User Management' },
  { path: '/user/users', breadcrumb: 'Users' },
  { path: '/user/merchants', breadcrumb: 'Merchants' },
  { path: '/reports', breadcrumb: 'Reports & Analytics' },
  { path: '/system', breadcrumb: 'System Settings' },
];

/**
 * Breadcrumbs component displays the navigation path for the current page.
 * It automatically generates breadcrumbs based on the current route.
 */
const Breadcrumbs: React.FC = () => {
  const location = useLocation();
  const dispatch = useDispatch();
  const breadcrumbs = useSelector(selectBreadcrumbs);
  
  // Generate breadcrumbs based on the current route
  useEffect(() => {
    const generateBreadcrumbs = () => {
      const { pathname } = location;
      
      // If we have manually set breadcrumbs, use those
      if (breadcrumbs.length > 0 && breadcrumbs[breadcrumbs.length - 1].link === pathname) {
        return;
      }
      
      // Split the path into segments
      const pathSegments = pathname.split('/').filter(Boolean);
      
      // Start with an empty array of breadcrumbs
      const newBreadcrumbs: BreadcrumbItem[] = [];
      
      // Always add home as the first breadcrumb
      newBreadcrumbs.push({
        text: 'Home',
        link: '/dashboard',
      });
      
      // Build up the path segments and find matching breadcrumbs
      let currentPath = '';
      
      for (let i = 0; i < pathSegments.length; i++) {
        currentPath += `/${pathSegments[i]}`;
        
        // Find a matching route in our map
        const matchingRoute = routeBreadcrumbMap.find(route => route.path === currentPath);
        
        if (matchingRoute) {
          newBreadcrumbs.push({
            text: matchingRoute.breadcrumb,
            link: currentPath,
          });
        } else {
          // For dynamic routes like /products/:id, we need to handle them specially
          // This is a simplified approach - in a real app, you might fetch the entity name
          if (i > 0 && pathSegments[i - 1] === 'products' && !isNaN(Number(pathSegments[i]))) {
            newBreadcrumbs.push({
              text: `Product #${pathSegments[i]}`,
              link: currentPath,
            });
          } else if (i > 0 && pathSegments[i - 1] === 'categories' && !isNaN(Number(pathSegments[i]))) {
            newBreadcrumbs.push({
              text: `Category #${pathSegments[i]}`,
              link: currentPath,
            });
          } else if (i > 0 && pathSegments[i - 1] === 'attributes' && !isNaN(Number(pathSegments[i]))) {
            newBreadcrumbs.push({
              text: `Attribute #${pathSegments[i]}`,
              link: currentPath,
            });
          } else {
            // For any other unrecognized path segment, just capitalize it
            newBreadcrumbs.push({
              text: pathSegments[i].charAt(0).toUpperCase() + pathSegments[i].slice(1),
              link: currentPath,
            });
          }
        }
      }
      
      // Update the breadcrumbs in the store
      dispatch(setBreadcrumbs(newBreadcrumbs));
    };
    
    generateBreadcrumbs();
  }, [location, dispatch, breadcrumbs]);
  
  // If we're on the dashboard, don't show breadcrumbs
  if (location.pathname === '/dashboard') {
    return null;
  }
  
  return (
    <Box
      sx={{
        py: 1,
        px: 3,
        bgcolor: 'background.paper',
        borderBottom: 1,
        borderColor: 'divider',
      }}
    >
      <MuiBreadcrumbs
        separator={<NavigateNextIcon fontSize="small" />}
        aria-label="breadcrumb"
      >
        {breadcrumbs.map((breadcrumb, index) => {
          const isLast = index === breadcrumbs.length - 1;
          
          // For the last item, show a chip instead of a link
          if (isLast) {
            return (
              <Chip
                key={breadcrumb.link}
                label={breadcrumb.text}
                size="small"
                color="primary"
                sx={{ height: 24 }}
              />
            );
          }
          
          // For other items, show a link
          return (
            <Link
              key={breadcrumb.link}
              component={RouterLink}
              to={breadcrumb.link}
              underline="hover"
              color="inherit"
              sx={{
                display: 'flex',
                alignItems: 'center',
                '&:hover': {
                  color: 'primary.main',
                },
              }}
            >
              {index === 0 && <HomeIcon fontSize="small" sx={{ mr: 0.5 }} />}
              <Typography variant="body2">{breadcrumb.text}</Typography>
            </Link>
          );
        })}
      </MuiBreadcrumbs>
    </Box>
  );
};

export default Breadcrumbs;