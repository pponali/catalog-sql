import React, { useState, useEffect } from 'react';
import { Outlet, useNavigate, useLocation } from 'react-router-dom';
import { useSelector, useDispatch } from 'react-redux';
import {
  Box,
  AppBar,
  Toolbar,
  IconButton,
  Typography,
  Drawer,
  List,
  ListItem,
  ListItemButton,
  ListItemIcon,
  ListItemText,
  Divider,
  Avatar,
  Menu,
  MenuItem,
  Tooltip,
  useMediaQuery,
  useTheme,
  Badge,
  Collapse,
} from '@mui/material';
import {
  Menu as MenuIcon,
  ChevronLeft as ChevronLeftIcon,
  Dashboard as DashboardIcon,
  Inventory as InventoryIcon,
  Category as CategoryIcon,
  AttachMoney as AttachMoneyIcon,
  LocalOffer as LocalOfferIcon,
  People as PeopleIcon,
  Assessment as AssessmentIcon,
  Settings as SettingsIcon,
  Notifications as NotificationsIcon,
  AccountCircle as AccountCircleIcon,
  Brightness4 as Brightness4Icon,
  Brightness7 as Brightness7Icon,
  ExpandLess as ExpandLessIcon,
  ExpandMore as ExpandMoreIcon,
  Logout as LogoutIcon,
  Person as PersonIcon,
  Store as StoreIcon,
  Tune as TuneIcon,
} from '@mui/icons-material';
import { selectDarkMode, toggleDarkMode, setSidebarOpen, selectSidebarOpen } from '../../store/slices/uiSlice';
import { selectCurrentUser, logout } from '../../store/slices/authSlice';
import Breadcrumbs from '../common/Breadcrumbs';
import NotificationCenter from '../common/NotificationCenter';

// Define the drawer width
const drawerWidth = 260;

/**
 * MainLayout component provides the main layout structure for the application.
 * It includes the app bar, navigation drawer, and main content area.
 */
const MainLayout: React.FC = () => {
  const theme = useTheme();
  const navigate = useNavigate();
  const location = useLocation();
  const dispatch = useDispatch();
  
  // Get state from Redux
  const darkMode = useSelector(selectDarkMode);
  const sidebarOpen = useSelector(selectSidebarOpen);
  const user = useSelector(selectCurrentUser);
  
  // Local state
  const [userMenuAnchor, setUserMenuAnchor] = useState<null | HTMLElement>(null);
  const [notificationMenuAnchor, setNotificationMenuAnchor] = useState<null | HTMLElement>(null);
  const [expandedMenus, setExpandedMenus] = useState<Record<string, boolean>>({
    catalog: location.pathname.startsWith('/catalog'),
    user: location.pathname.startsWith('/user'),
  });
  
  // Check if we're on a mobile device
  const isMobile = useMediaQuery(theme.breakpoints.down('md'));
  
  // Close the sidebar on mobile when the route changes
  useEffect(() => {
    if (isMobile && sidebarOpen) {
      dispatch(setSidebarOpen(false));
    }
  }, [location.pathname, isMobile, dispatch, sidebarOpen]);
  
  // Handle toggling the sidebar
  const handleToggleSidebar = () => {
    dispatch(setSidebarOpen(!sidebarOpen));
  };
  
  // Handle toggling dark mode
  const handleToggleDarkMode = () => {
    dispatch(toggleDarkMode());
  };
  
  // Handle opening the user menu
  const handleUserMenuOpen = (event: React.MouseEvent<HTMLElement>) => {
    setUserMenuAnchor(event.currentTarget);
  };
  
  // Handle closing the user menu
  const handleUserMenuClose = () => {
    setUserMenuAnchor(null);
  };
  
  // Handle opening the notification menu
  const handleNotificationMenuOpen = (event: React.MouseEvent<HTMLElement>) => {
    setNotificationMenuAnchor(event.currentTarget);
  };
  
  // Handle closing the notification menu
  const handleNotificationMenuClose = () => {
    setNotificationMenuAnchor(null);
  };
  
  // Handle logging out
  const handleLogout = () => {
    dispatch(logout());
    navigate('/login');
  };
  
  // Handle navigating to a route
  const handleNavigate = (route: string) => {
    navigate(route);
    if (isMobile) {
      dispatch(setSidebarOpen(false));
    }
  };
  
  // Handle toggling a submenu
  const handleToggleSubmenu = (menu: string) => {
    setExpandedMenus({
      ...expandedMenus,
      [menu]: !expandedMenus[menu],
    });
  };
  
  // Define the navigation items
  const navigationItems = [
    {
      text: 'Dashboard',
      icon: <DashboardIcon />,
      route: '/dashboard',
    },
    {
      text: 'Catalog Management',
      icon: <InventoryIcon />,
      submenu: true,
      key: 'catalog',
      items: [
        {
          text: 'Products',
          icon: <InventoryIcon />,
          route: '/catalog/products',
        },
        {
          text: 'Categories',
          icon: <CategoryIcon />,
          route: '/catalog/categories',
        },
        {
          text: 'Attributes',
          icon: <TuneIcon />,
          route: '/catalog/attributes',
        },
      ],
    },
    {
      text: 'Price Management',
      icon: <AttachMoneyIcon />,
      route: '/price',
    },
    {
      text: 'Promotion Management',
      icon: <LocalOfferIcon />,
      route: '/promotion',
    },
    {
      text: 'User Management',
      icon: <PeopleIcon />,
      submenu: true,
      key: 'user',
      items: [
        {
          text: 'Users',
          icon: <PersonIcon />,
          route: '/user/users',
        },
        {
          text: 'Merchants',
          icon: <StoreIcon />,
          route: '/user/merchants',
        },
      ],
    },
    {
      text: 'Reports & Analytics',
      icon: <AssessmentIcon />,
      route: '/reports',
    },
    {
      text: 'System Settings',
      icon: <SettingsIcon />,
      route: '/system',
    },
  ];
  
  // Render the drawer content
  const drawerContent = (
    <>
      <Box
        sx={{
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'space-between',
          p: 2,
        }}
      >
        <Typography variant="h6" noWrap component="div">
          Tata Commerce
        </Typography>
        <IconButton onClick={handleToggleSidebar}>
          <ChevronLeftIcon />
        </IconButton>
      </Box>
      <Divider />
      <List>
        {navigationItems.map((item) => (
          item.submenu ? (
            <React.Fragment key={item.text}>
              <ListItem disablePadding>
                <ListItemButton onClick={() => handleToggleSubmenu(item.key)}>
                  <ListItemIcon>{item.icon}</ListItemIcon>
                  <ListItemText primary={item.text} />
                  {expandedMenus[item.key] ? <ExpandLessIcon /> : <ExpandMoreIcon />}
                </ListItemButton>
              </ListItem>
              <Collapse in={expandedMenus[item.key]} timeout="auto" unmountOnExit>
                <List component="div" disablePadding>
                  {item.items?.map((subItem) => (
                    <ListItem key={subItem.text} disablePadding>
                      <ListItemButton
                        sx={{ pl: 4 }}
                        selected={location.pathname === subItem.route}
                        onClick={() => handleNavigate(subItem.route)}
                      >
                        <ListItemIcon>{subItem.icon}</ListItemIcon>
                        <ListItemText primary={subItem.text} />
                      </ListItemButton>
                    </ListItem>
                  ))}
                </List>
              </Collapse>
            </React.Fragment>
          ) : (
            <ListItem key={item.text} disablePadding>
              <ListItemButton
                selected={location.pathname === item.route}
                onClick={() => handleNavigate(item.route)}
              >
                <ListItemIcon>{item.icon}</ListItemIcon>
                <ListItemText primary={item.text} />
              </ListItemButton>
            </ListItem>
          )
        ))}
      </List>
    </>
  );
  
  return (
    <Box sx={{ display: 'flex', height: '100vh' }}>
      {/* App Bar */}
      <AppBar
        position="fixed"
        sx={{
          zIndex: (theme) => theme.zIndex.drawer + 1,
          transition: theme.transitions.create(['width', 'margin'], {
            easing: theme.transitions.easing.sharp,
            duration: theme.transitions.duration.leavingScreen,
          }),
          ...(sidebarOpen && {
            marginLeft: drawerWidth,
            width: `calc(100% - ${drawerWidth}px)`,
            transition: theme.transitions.create(['width', 'margin'], {
              easing: theme.transitions.easing.sharp,
              duration: theme.transitions.duration.enteringScreen,
            }),
          }),
        }}
      >
        <Toolbar>
          <IconButton
            color="inherit"
            aria-label="open drawer"
            edge="start"
            onClick={handleToggleSidebar}
            sx={{ mr: 2 }}
          >
            <MenuIcon />
          </IconButton>
          
          <Typography variant="h6" noWrap component="div" sx={{ flexGrow: 1 }}>
            Catalog Admin
          </Typography>
          
          {/* Dark Mode Toggle */}
          <Tooltip title={darkMode ? 'Switch to Light Mode' : 'Switch to Dark Mode'}>
            <IconButton color="inherit" onClick={handleToggleDarkMode}>
              {darkMode ? <Brightness7Icon /> : <Brightness4Icon />}
            </IconButton>
          </Tooltip>
          
          {/* Notifications */}
          <Tooltip title="Notifications">
            <IconButton color="inherit" onClick={handleNotificationMenuOpen}>
              <Badge badgeContent={3} color="error">
                <NotificationsIcon />
              </Badge>
            </IconButton>
          </Tooltip>
          
          {/* User Menu */}
          <Tooltip title="Account settings">
            <IconButton
              color="inherit"
              edge="end"
              onClick={handleUserMenuOpen}
              sx={{ ml: 1 }}
            >
              <Avatar
                alt={user?.name || 'User'}
                src={user?.avatar || ''}
                sx={{ width: 32, height: 32 }}
              >
                {user?.name ? user.name.charAt(0).toUpperCase() : <AccountCircleIcon />}
              </Avatar>
            </IconButton>
          </Tooltip>
        </Toolbar>
      </AppBar>
      
      {/* Notification Menu */}
      <Menu
        anchorEl={notificationMenuAnchor}
        open={Boolean(notificationMenuAnchor)}
        onClose={handleNotificationMenuClose}
        PaperProps={{
          elevation: 0,
          sx: {
            overflow: 'visible',
            filter: 'drop-shadow(0px 2px 8px rgba(0,0,0,0.32))',
            mt: 1.5,
            '& .MuiAvatar-root': {
              width: 32,
              height: 32,
              ml: -0.5,
              mr: 1,
            },
            '&:before': {
              content: '""',
              display: 'block',
              position: 'absolute',
              top: 0,
              right: 14,
              width: 10,
              height: 10,
              bgcolor: 'background.paper',
              transform: 'translateY(-50%) rotate(45deg)',
              zIndex: 0,
            },
          },
        }}
        transformOrigin={{ horizontal: 'right', vertical: 'top' }}
        anchorOrigin={{ horizontal: 'right', vertical: 'bottom' }}
      >
        <MenuItem onClick={handleNotificationMenuClose}>
          <Typography variant="body2">New product added: Tata Tea Gold</Typography>
        </MenuItem>
        <MenuItem onClick={handleNotificationMenuClose}>
          <Typography variant="body2">Price update for 5 products</Typography>
        </MenuItem>
        <MenuItem onClick={handleNotificationMenuClose}>
          <Typography variant="body2">New promotion created: Summer Sale</Typography>
        </MenuItem>
        <Divider />
        <MenuItem onClick={handleNotificationMenuClose}>
          <Typography variant="body2" color="primary">View all notifications</Typography>
        </MenuItem>
      </Menu>
      
      {/* User Menu */}
      <Menu
        anchorEl={userMenuAnchor}
        open={Boolean(userMenuAnchor)}
        onClose={handleUserMenuClose}
        PaperProps={{
          elevation: 0,
          sx: {
            overflow: 'visible',
            filter: 'drop-shadow(0px 2px 8px rgba(0,0,0,0.32))',
            mt: 1.5,
            '& .MuiAvatar-root': {
              width: 32,
              height: 32,
              ml: -0.5,
              mr: 1,
            },
            '&:before': {
              content: '""',
              display: 'block',
              position: 'absolute',
              top: 0,
              right: 14,
              width: 10,
              height: 10,
              bgcolor: 'background.paper',
              transform: 'translateY(-50%) rotate(45deg)',
              zIndex: 0,
            },
          },
        }}
        transformOrigin={{ horizontal: 'right', vertical: 'top' }}
        anchorOrigin={{ horizontal: 'right', vertical: 'bottom' }}
      >
        <MenuItem onClick={handleUserMenuClose}>
          <Avatar /> Profile
        </MenuItem>
        <MenuItem onClick={handleUserMenuClose}>
          <Avatar /> My account
        </MenuItem>
        <Divider />
        <MenuItem onClick={handleLogout}>
          <ListItemIcon>
            <LogoutIcon fontSize="small" />
          </ListItemIcon>
          Logout
        </MenuItem>
      </Menu>
      
      {/* Sidebar Drawer */}
      <Drawer
        variant={isMobile ? 'temporary' : 'persistent'}
        open={sidebarOpen}
        onClose={handleToggleSidebar}
        sx={{
          width: drawerWidth,
          flexShrink: 0,
          '& .MuiDrawer-paper': {
            width: drawerWidth,
            boxSizing: 'border-box',
          },
        }}
      >
        <Toolbar />
        {drawerContent}
      </Drawer>
      
      {/* Main Content */}
      <Box
        component="main"
        sx={{
          flexGrow: 1,
          display: 'flex',
          flexDirection: 'column',
          overflow: 'hidden',
          bgcolor: 'background.default',
        }}
      >
        <Toolbar />
        <Breadcrumbs />
        
        <Box
          sx={{
            p: 3,
            flexGrow: 1,
            overflow: 'auto',
          }}
        >
          <Outlet />
        </Box>
      </Box>
      
      {/* Notification Center */}
      <NotificationCenter />
    </Box>
  );
};

export default MainLayout;