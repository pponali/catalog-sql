import React, { useState, useEffect } from 'react';
import {
  Box,
  Grid,
  Paper,
  Typography,
  Card,
  CardContent,
  CardHeader,
  Divider,
  List,
  ListItem,
  ListItemText,
  ListItemAvatar,
  Avatar,
  Button,
  IconButton,
  Chip,
  CircularProgress,
  LinearProgress,
  useTheme,
} from '@mui/material';
import {
  Inventory as InventoryIcon,
  Category as CategoryIcon,
  AttachMoney as AttachMoneyIcon,
  LocalOffer as LocalOfferIcon,
  People as PeopleIcon,
  Visibility as VisibilityIcon,
  TrendingUp as TrendingUpIcon,
  TrendingDown as TrendingDownIcon,
  MoreVert as MoreVertIcon,
  Refresh as RefreshIcon,
} from '@mui/icons-material';
import { useNavigate } from 'react-router-dom';
import { useSelector } from 'react-redux';
import { selectCurrentUser } from '../store/slices/authSlice';

// Import chart components (you would typically use a library like recharts or chart.js)
// For simplicity, we'll create placeholder components
const LineChart: React.FC<{ data: any }> = ({ data }) => {
  const theme = useTheme();
  
  return (
    <Box
      sx={{
        height: 200,
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        bgcolor: theme.palette.mode === 'dark' ? 'rgba(255, 255, 255, 0.05)' : 'rgba(0, 0, 0, 0.02)',
        borderRadius: 1,
      }}
    >
      <Typography variant="body2" color="text.secondary">
        Line Chart Placeholder
      </Typography>
    </Box>
  );
};

const BarChart: React.FC<{ data: any }> = ({ data }) => {
  const theme = useTheme();
  
  return (
    <Box
      sx={{
        height: 200,
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        bgcolor: theme.palette.mode === 'dark' ? 'rgba(255, 255, 255, 0.05)' : 'rgba(0, 0, 0, 0.02)',
        borderRadius: 1,
      }}
    >
      <Typography variant="body2" color="text.secondary">
        Bar Chart Placeholder
      </Typography>
    </Box>
  );
};

const PieChart: React.FC<{ data: any }> = ({ data }) => {
  const theme = useTheme();
  
  return (
    <Box
      sx={{
        height: 200,
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        bgcolor: theme.palette.mode === 'dark' ? 'rgba(255, 255, 255, 0.05)' : 'rgba(0, 0, 0, 0.02)',
        borderRadius: 1,
      }}
    >
      <Typography variant="body2" color="text.secondary">
        Pie Chart Placeholder
      </Typography>
    </Box>
  );
};

/**
 * Dashboard component displays an overview of the catalog management system.
 * It includes key metrics, recent activities, and quick access to important features.
 */
const Dashboard: React.FC = () => {
  const navigate = useNavigate();
  const theme = useTheme();
  const user = useSelector(selectCurrentUser);
  
  // State for loading indicators
  const [loading, setLoading] = useState(true);
  const [refreshing, setRefreshing] = useState(false);
  
  // Mock data for dashboard
  const [dashboardData, setDashboardData] = useState({
    metrics: {
      totalProducts: 12458,
      totalCategories: 187,
      activePromotions: 24,
      pendingApprovals: 15,
    },
    recentProducts: [
      { id: 1, name: 'Tata Tea Gold', category: 'Beverages', status: 'Active', timestamp: '2 hours ago' },
      { id: 2, name: 'Croma 55" Smart TV', category: 'Electronics', status: 'Pending', timestamp: '3 hours ago' },
      { id: 3, name: 'Tanishq Diamond Ring', category: 'Jewelry', status: 'Active', timestamp: '5 hours ago' },
      { id: 4, name: 'BigBasket Organic Apples', category: 'Grocery', status: 'Active', timestamp: '6 hours ago' },
      { id: 5, name: '1mg Multivitamin Tablets', category: 'Medicine', status: 'Pending', timestamp: '8 hours ago' },
    ],
    categoryDistribution: [
      { name: 'Electronics', value: 35 },
      { name: 'Apparel', value: 25 },
      { name: 'Grocery', value: 15 },
      { name: 'Jewelry', value: 10 },
      { name: 'Medicine', value: 8 },
      { name: 'Others', value: 7 },
    ],
    monthlySales: [
      { month: 'Jan', sales: 1200 },
      { month: 'Feb', sales: 1900 },
      { month: 'Mar', sales: 1500 },
      { month: 'Apr', sales: 1800 },
      { month: 'May', sales: 2200 },
      { month: 'Jun', sales: 2500 },
    ],
    topCategories: [
      { name: 'Electronics', products: 3245, growth: 12 },
      { name: 'Apparel', products: 2876, growth: 8 },
      { name: 'Grocery', products: 1987, growth: 15 },
      { name: 'Jewelry', products: 1245, growth: -3 },
      { name: 'Medicine', products: 987, growth: 5 },
    ],
  });
  
  // Simulate loading data
  useEffect(() => {
    const timer = setTimeout(() => {
      setLoading(false);
    }, 1500);
    
    return () => clearTimeout(timer);
  }, []);
  
  // Handle refresh
  const handleRefresh = () => {
    setRefreshing(true);
    
    // Simulate refreshing data
    setTimeout(() => {
      setRefreshing(false);
    }, 1000);
  };
  
  // Handle navigation to different sections
  const handleNavigate = (route: string) => {
    navigate(route);
  };
  
  if (loading) {
    return (
      <Box
        sx={{
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'center',
          justifyContent: 'center',
          height: '100%',
        }}
      >
        <CircularProgress size={60} thickness={4} />
        <Typography variant="h6" sx={{ mt: 2 }}>
          Loading Dashboard...
        </Typography>
      </Box>
    );
  }
  
  return (
    <Box>
      {/* Welcome message */}
      <Box sx={{ mb: 4 }}>
        <Typography variant="h4" gutterBottom>
          Welcome back, {user?.name || 'User'}
        </Typography>
        <Typography variant="body1" color="text.secondary">
          Here's what's happening with your catalog today.
        </Typography>
      </Box>
      
      {/* Refresh indicator */}
      {refreshing && <LinearProgress sx={{ mb: 3 }} />}
      
      {/* Key metrics */}
      <Grid container spacing={3} sx={{ mb: 4 }}>
        <Grid item xs={12} sm={6} md={3}>
          <Paper
            elevation={0}
            sx={{
              p: 2,
              display: 'flex',
              flexDirection: 'column',
              borderRadius: 2,
              boxShadow: '0 2px 10px rgba(0, 0, 0, 0.05)',
              height: '100%',
            }}
          >
            <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
              <Avatar
                sx={{
                  bgcolor: 'primary.main',
                  width: 40,
                  height: 40,
                }}
              >
                <InventoryIcon />
              </Avatar>
              <Box sx={{ ml: 2 }}>
                <Typography variant="body2" color="text.secondary">
                  Total Products
                </Typography>
                <Typography variant="h5">
                  {dashboardData.metrics.totalProducts.toLocaleString()}
                </Typography>
              </Box>
            </Box>
            <Button
              variant="text"
              size="small"
              onClick={() => handleNavigate('/catalog/products')}
              sx={{ alignSelf: 'flex-start' }}
            >
              View All Products
            </Button>
          </Paper>
        </Grid>
        
        <Grid item xs={12} sm={6} md={3}>
          <Paper
            elevation={0}
            sx={{
              p: 2,
              display: 'flex',
              flexDirection: 'column',
              borderRadius: 2,
              boxShadow: '0 2px 10px rgba(0, 0, 0, 0.05)',
              height: '100%',
            }}
          >
            <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
              <Avatar
                sx={{
                  bgcolor: 'secondary.main',
                  width: 40,
                  height: 40,
                }}
              >
                <CategoryIcon />
              </Avatar>
              <Box sx={{ ml: 2 }}>
                <Typography variant="body2" color="text.secondary">
                  Total Categories
                </Typography>
                <Typography variant="h5">
                  {dashboardData.metrics.totalCategories.toLocaleString()}
                </Typography>
              </Box>
            </Box>
            <Button
              variant="text"
              size="small"
              onClick={() => handleNavigate('/catalog/categories')}
              sx={{ alignSelf: 'flex-start' }}
            >
              View All Categories
            </Button>
          </Paper>
        </Grid>
        
        <Grid item xs={12} sm={6} md={3}>
          <Paper
            elevation={0}
            sx={{
              p: 2,
              display: 'flex',
              flexDirection: 'column',
              borderRadius: 2,
              boxShadow: '0 2px 10px rgba(0, 0, 0, 0.05)',
              height: '100%',
            }}
          >
            <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
              <Avatar
                sx={{
                  bgcolor: 'success.main',
                  width: 40,
                  height: 40,
                }}
              >
                <LocalOfferIcon />
              </Avatar>
              <Box sx={{ ml: 2 }}>
                <Typography variant="body2" color="text.secondary">
                  Active Promotions
                </Typography>
                <Typography variant="h5">
                  {dashboardData.metrics.activePromotions.toLocaleString()}
                </Typography>
              </Box>
            </Box>
            <Button
              variant="text"
              size="small"
              onClick={() => handleNavigate('/promotion')}
              sx={{ alignSelf: 'flex-start' }}
            >
              View All Promotions
            </Button>
          </Paper>
        </Grid>
        
        <Grid item xs={12} sm={6} md={3}>
          <Paper
            elevation={0}
            sx={{
              p: 2,
              display: 'flex',
              flexDirection: 'column',
              borderRadius: 2,
              boxShadow: '0 2px 10px rgba(0, 0, 0, 0.05)',
              height: '100%',
            }}
          >
            <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
              <Avatar
                sx={{
                  bgcolor: 'warning.main',
                  width: 40,
                  height: 40,
                }}
              >
                <PeopleIcon />
              </Avatar>
              <Box sx={{ ml: 2 }}>
                <Typography variant="body2" color="text.secondary">
                  Pending Approvals
                </Typography>
                <Typography variant="h5">
                  {dashboardData.metrics.pendingApprovals.toLocaleString()}
                </Typography>
              </Box>
            </Box>
            <Button
              variant="text"
              size="small"
              onClick={() => handleNavigate('/catalog/products')}
              sx={{ alignSelf: 'flex-start' }}
            >
              View Pending Items
            </Button>
          </Paper>
        </Grid>
      </Grid>
      
      {/* Charts and data */}
      <Grid container spacing={3}>
        {/* Monthly Sales Trend */}
        <Grid item xs={12} md={8}>
          <Card
            elevation={0}
            sx={{
              borderRadius: 2,
              boxShadow: '0 2px 10px rgba(0, 0, 0, 0.05)',
              height: '100%',
            }}
          >
            <CardHeader
              title="Product Additions Trend"
              action={
                <IconButton aria-label="refresh" onClick={handleRefresh}>
                  <RefreshIcon />
                </IconButton>
              }
            />
            <Divider />
            <CardContent>
              <LineChart data={dashboardData.monthlySales} />
            </CardContent>
          </Card>
        </Grid>
        
        {/* Category Distribution */}
        <Grid item xs={12} md={4}>
          <Card
            elevation={0}
            sx={{
              borderRadius: 2,
              boxShadow: '0 2px 10px rgba(0, 0, 0, 0.05)',
              height: '100%',
            }}
          >
            <CardHeader
              title="Category Distribution"
              action={
                <IconButton aria-label="settings">
                  <MoreVertIcon />
                </IconButton>
              }
            />
            <Divider />
            <CardContent>
              <PieChart data={dashboardData.categoryDistribution} />
            </CardContent>
          </Card>
        </Grid>
        
        {/* Recent Products */}
        <Grid item xs={12} md={6}>
          <Card
            elevation={0}
            sx={{
              borderRadius: 2,
              boxShadow: '0 2px 10px rgba(0, 0, 0, 0.05)',
              height: '100%',
            }}
          >
            <CardHeader
              title="Recently Added Products"
              action={
                <Button
                  size="small"
                  endIcon={<VisibilityIcon />}
                  onClick={() => handleNavigate('/catalog/products')}
                >
                  View All
                </Button>
              }
            />
            <Divider />
            <List sx={{ width: '100%', bgcolor: 'background.paper' }}>
              {dashboardData.recentProducts.map((product) => (
                <React.Fragment key={product.id}>
                  <ListItem
                    alignItems="flex-start"
                    secondaryAction={
                      <Chip
                        label={product.status}
                        size="small"
                        color={product.status === 'Active' ? 'success' : 'warning'}
                      />
                    }
                  >
                    <ListItemAvatar>
                      <Avatar
                        alt={product.name}
                        sx={{
                          bgcolor:
                            product.category === 'Electronics'
                              ? 'primary.main'
                              : product.category === 'Jewelry'
                              ? 'secondary.main'
                              : product.category === 'Grocery'
                              ? 'success.main'
                              : product.category === 'Medicine'
                              ? 'info.main'
                              : 'warning.main',
                        }}
                      >
                        {product.name.charAt(0)}
                      </Avatar>
                    </ListItemAvatar>
                    <ListItemText
                      primary={product.name}
                      secondary={
                        <React.Fragment>
                          <Typography
                            sx={{ display: 'inline' }}
                            component="span"
                            variant="body2"
                            color="text.primary"
                          >
                            {product.category}
                          </Typography>
                          {` — Added ${product.timestamp}`}
                        </React.Fragment>
                      }
                    />
                  </ListItem>
                  <Divider variant="inset" component="li" />
                </React.Fragment>
              ))}
            </List>
          </Card>
        </Grid>
        
        {/* Top Categories */}
        <Grid item xs={12} md={6}>
          <Card
            elevation={0}
            sx={{
              borderRadius: 2,
              boxShadow: '0 2px 10px rgba(0, 0, 0, 0.05)',
              height: '100%',
            }}
          >
            <CardHeader
              title="Top Categories"
              action={
                <Button
                  size="small"
                  endIcon={<VisibilityIcon />}
                  onClick={() => handleNavigate('/catalog/categories')}
                >
                  View All
                </Button>
              }
            />
            <Divider />
            <List sx={{ width: '100%', bgcolor: 'background.paper' }}>
              {dashboardData.topCategories.map((category) => (
                <React.Fragment key={category.name}>
                  <ListItem
                    alignItems="flex-start"
                    secondaryAction={
                      <Box sx={{ display: 'flex', alignItems: 'center' }}>
                        {category.growth > 0 ? (
                          <TrendingUpIcon color="success" fontSize="small" sx={{ mr: 0.5 }} />
                        ) : (
                          <TrendingDownIcon color="error" fontSize="small" sx={{ mr: 0.5 }} />
                        )}
                        <Typography
                          variant="body2"
                          color={category.growth > 0 ? 'success.main' : 'error.main'}
                        >
                          {category.growth > 0 ? '+' : ''}
                          {category.growth}%
                        </Typography>
                      </Box>
                    }
                  >
                    <ListItemAvatar>
                      <Avatar
                        alt={category.name}
                        sx={{
                          bgcolor:
                            category.name === 'Electronics'
                              ? 'primary.main'
                              : category.name === 'Jewelry'
                              ? 'secondary.main'
                              : category.name === 'Grocery'
                              ? 'success.main'
                              : category.name === 'Medicine'
                              ? 'info.main'
                              : 'warning.main',
                        }}
                      >
                        {category.name.charAt(0)}
                      </Avatar>
                    </ListItemAvatar>
                    <ListItemText
                      primary={category.name}
                      secondary={`${category.products.toLocaleString()} products`}
                    />
                  </ListItem>
                  <Divider variant="inset" component="li" />
                </React.Fragment>
              ))}
            </List>
          </Card>
        </Grid>
      </Grid>
    </Box>
  );
};

export default Dashboard;