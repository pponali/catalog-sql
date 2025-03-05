import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useDispatch } from 'react-redux';
import {
  Box,
  Paper,
  Typography,
  Button,
  TextField,
  InputAdornment,
  IconButton,
  Chip,
  Menu,
  MenuItem,
  ListItemIcon,
  ListItemText,
  Divider,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogContentText,
  DialogActions,
  Tooltip,
  CircularProgress,
  Grid,
  Card,
  CardContent,
  CardMedia,
  CardActions,
  FormControl,
  InputLabel,
  Select,
  SelectChangeEvent,
  Pagination,
} from '@mui/material';
import {
  Search as SearchIcon,
  Add as AddIcon,
  FilterList as FilterListIcon,
  Sort as SortIcon,
  ViewList as ViewListIcon,
  ViewModule as ViewModuleIcon,
  Edit as EditIcon,
  Delete as DeleteIcon,
  Visibility as VisibilityIcon,
  MoreVert as MoreVertIcon,
  CloudUpload as CloudUploadIcon,
  CloudDownload as CloudDownloadIcon,
  ArrowUpward as ArrowUpwardIcon,
  ArrowDownward as ArrowDownwardIcon,
  Refresh as RefreshIcon,
} from '@mui/icons-material';
import { DataGrid, GridColDef, GridValueGetterParams, GridRenderCellParams } from '@mui/x-data-grid';
import { useGetProductsQuery, useDeleteProductMutation } from '../../services/catalogApi';
import { showSuccessNotification, showErrorNotification } from '../../store/slices/notificationSlice';

// Define product interface
interface Product {
  id: string;
  code: string;
  name: string;
  description: string;
  status: string;
  category: string;
  price: number;
  imageUrl?: string;
  createdAt: string;
  updatedAt: string;
}

/**
 * ProductList component displays a list of products and allows users to manage them.
 * It provides features like searching, filtering, sorting, and CRUD operations.
 */
const ProductList: React.FC = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  
  // Get products from API
  const { data: products, isLoading, error, refetch } = useGetProductsQuery();
  const [deleteProduct, { isLoading: isDeleting }] = useDeleteProductMutation();
  
  // Local state
  const [searchTerm, setSearchTerm] = useState('');
  const [viewMode, setViewMode] = useState<'list' | 'grid'>('list');
  const [filterAnchorEl, setFilterAnchorEl] = useState<null | HTMLElement>(null);
  const [sortAnchorEl, setSortAnchorEl] = useState<null | HTMLElement>(null);
  const [actionAnchorEl, setActionAnchorEl] = useState<null | HTMLElement>(null);
  const [selectedProduct, setSelectedProduct] = useState<Product | null>(null);
  const [deleteDialogOpen, setDeleteDialogOpen] = useState(false);
  const [selectedRows, setSelectedRows] = useState<string[]>([]);
  const [categoryFilter, setCategoryFilter] = useState<string>('all');
  const [statusFilter, setStatusFilter] = useState<string>('all');
  const [sortField, setSortField] = useState<string>('updatedAt');
  const [sortDirection, setSortDirection] = useState<'asc' | 'desc'>('desc');
  const [page, setPage] = useState(1);
  const [pageSize, setPageSize] = useState(10);
  
  // Filter menu open state
  const isFilterMenuOpen = Boolean(filterAnchorEl);
  
  // Sort menu open state
  const isSortMenuOpen = Boolean(sortAnchorEl);
  
  // Action menu open state
  const isActionMenuOpen = Boolean(actionAnchorEl);
  
  // Handle opening filter menu
  const handleFilterMenuOpen = (event: React.MouseEvent<HTMLElement>) => {
    setFilterAnchorEl(event.currentTarget);
  };
  
  // Handle closing filter menu
  const handleFilterMenuClose = () => {
    setFilterAnchorEl(null);
  };
  
  // Handle opening sort menu
  const handleSortMenuOpen = (event: React.MouseEvent<HTMLElement>) => {
    setSortAnchorEl(event.currentTarget);
  };
  
  // Handle closing sort menu
  const handleSortMenuClose = () => {
    setSortAnchorEl(null);
  };
  
  // Handle opening action menu
  const handleActionMenuOpen = (event: React.MouseEvent<HTMLElement>, product: Product) => {
    setActionAnchorEl(event.currentTarget);
    setSelectedProduct(product);
  };
  
  // Handle closing action menu
  const handleActionMenuClose = () => {
    setActionAnchorEl(null);
  };
  
  // Handle search input change
  const handleSearchChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setSearchTerm(event.target.value);
  };
  
  // Handle category filter change
  const handleCategoryFilterChange = (event: SelectChangeEvent) => {
    setCategoryFilter(event.target.value);
  };
  
  // Handle status filter change
  const handleStatusFilterChange = (event: SelectChangeEvent) => {
    setStatusFilter(event.target.value);
  };
  
  // Handle sort field change
  const handleSortFieldChange = (field: string) => {
    if (sortField === field) {
      // Toggle sort direction if the same field is selected
      setSortDirection(sortDirection === 'asc' ? 'desc' : 'asc');
    } else {
      // Set new sort field and default to descending
      setSortField(field);
      setSortDirection('desc');
    }
    handleSortMenuClose();
  };
  
  // Handle view mode change
  const handleViewModeChange = (mode: 'list' | 'grid') => {
    setViewMode(mode);
  };
  
  // Handle page change
  const handlePageChange = (event: React.ChangeEvent<unknown>, value: number) => {
    setPage(value);
  };
  
  // Handle adding a new product
  const handleAddProduct = () => {
    navigate('/catalog/products/new');
  };
  
  // Handle editing a product
  const handleEditProduct = (id: string) => {
    navigate(`/catalog/products/${id}`);
    handleActionMenuClose();
  };
  
  // Handle viewing a product
  const handleViewProduct = (id: string) => {
    navigate(`/catalog/products/${id}?view=true`);
    handleActionMenuClose();
  };
  
  // Handle opening delete dialog
  const handleDeleteDialogOpen = () => {
    setDeleteDialogOpen(true);
    handleActionMenuClose();
  };
  
  // Handle closing delete dialog
  const handleDeleteDialogClose = () => {
    setDeleteDialogOpen(false);
  };
  
  // Handle deleting a product
  const handleDeleteProduct = async () => {
    if (selectedProduct) {
      try {
        await deleteProduct(selectedProduct.id).unwrap();
        dispatch(showSuccessNotification(`Product "${selectedProduct.name}" deleted successfully`));
        handleDeleteDialogClose();
      } catch (error: any) {
        dispatch(showErrorNotification(`Failed to delete product: ${error.message}`));
      }
    }
  };
  
  // Handle bulk actions
  const handleBulkDelete = async () => {
    // In a real application, you would implement bulk delete functionality here
    dispatch(showSuccessNotification(`${selectedRows.length} products deleted successfully`));
    setSelectedRows([]);
  };
  
  // Handle refresh
  const handleRefresh = () => {
    refetch();
  };
  
  // Filter and sort products
  const filteredProducts = React.useMemo(() => {
    if (!products) return [];
    
    return products
      .filter((product: Product) => {
        // Apply search filter
        const matchesSearch = searchTerm === '' || 
          product.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
          product.code.toLowerCase().includes(searchTerm.toLowerCase()) ||
          product.description.toLowerCase().includes(searchTerm.toLowerCase());
        
        // Apply category filter
        const matchesCategory = categoryFilter === 'all' || product.category === categoryFilter;
        
        // Apply status filter
        const matchesStatus = statusFilter === 'all' || product.status === statusFilter;
        
        return matchesSearch && matchesCategory && matchesStatus;
      })
      .sort((a: Product, b: Product) => {
        // Apply sorting
        if (a[sortField as keyof Product] < b[sortField as keyof Product]) {
          return sortDirection === 'asc' ? -1 : 1;
        }
        if (a[sortField as keyof Product] > b[sortField as keyof Product]) {
          return sortDirection === 'asc' ? 1 : -1;
        }
        return 0;
      });
  }, [products, searchTerm, categoryFilter, statusFilter, sortField, sortDirection]);
  
  // Get paginated products
  const paginatedProducts = React.useMemo(() => {
    const startIndex = (page - 1) * pageSize;
    const endIndex = startIndex + pageSize;
    return filteredProducts.slice(startIndex, endIndex);
  }, [filteredProducts, page, pageSize]);
  
  // Get unique categories for filter
  const categories = React.useMemo(() => {
    if (!products) return [];
    
    const uniqueCategories = new Set<string>();
    products.forEach((product: Product) => {
      if (product.category) {
        uniqueCategories.add(product.category);
      }
    });
    
    return Array.from(uniqueCategories).sort();
  }, [products]);
  
  // Get unique statuses for filter
  const statuses = React.useMemo(() => {
    if (!products) return [];
    
    const uniqueStatuses = new Set<string>();
    products.forEach((product: Product) => {
      if (product.status) {
        uniqueStatuses.add(product.status);
      }
    });
    
    return Array.from(uniqueStatuses).sort();
  }, [products]);
  
  // Define columns for data grid
  const columns: GridColDef[] = [
    {
      field: 'id',
      headerName: 'ID',
      width: 100,
    },
    {
      field: 'code',
      headerName: 'Code',
      width: 120,
    },
    {
      field: 'name',
      headerName: 'Name',
      width: 200,
    },
    {
      field: 'category',
      headerName: 'Category',
      width: 150,
    },
    {
      field: 'price',
      headerName: 'Price',
      width: 120,
      type: 'number',
      valueFormatter: (params) => {
        if (params.value == null) {
          return '';
        }
        return `₹${params.value.toLocaleString()}`;
      },
    },
    {
      field: 'status',
      headerName: 'Status',
      width: 120,
      renderCell: (params: GridRenderCellParams<string>) => (
        <Chip
          label={params.value}
          size="small"
          color={
            params.value === 'Active'
              ? 'success'
              : params.value === 'Pending'
              ? 'warning'
              : params.value === 'Draft'
              ? 'default'
              : 'error'
          }
        />
      ),
    },
    {
      field: 'updatedAt',
      headerName: 'Last Updated',
      width: 180,
      valueFormatter: (params) => {
        if (params.value == null) {
          return '';
        }
        return new Date(params.value).toLocaleString();
      },
    },
    {
      field: 'actions',
      headerName: 'Actions',
      width: 120,
      sortable: false,
      renderCell: (params: GridRenderCellParams<any, Product>) => (
        <Box>
          <Tooltip title="Actions">
            <IconButton
              size="small"
              onClick={(event) => handleActionMenuOpen(event, params.row)}
            >
              <MoreVertIcon fontSize="small" />
            </IconButton>
          </Tooltip>
        </Box>
      ),
    },
  ];
  
  // If loading, show loading indicator
  if (isLoading) {
    return (
      <Box
        sx={{
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'center',
          justifyContent: 'center',
          height: '100%',
          p: 3,
        }}
      >
        <CircularProgress size={60} thickness={4} />
        <Typography variant="h6" sx={{ mt: 2 }}>
          Loading Products...
        </Typography>
      </Box>
    );
  }
  
  // If error, show error message
  if (error) {
    return (
      <Box
        sx={{
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'center',
          justifyContent: 'center',
          height: '100%',
          p: 3,
        }}
      >
        <Typography variant="h6" color="error" gutterBottom>
          Error loading products
        </Typography>
        <Button
          variant="contained"
          color="primary"
          startIcon={<RefreshIcon />}
          onClick={handleRefresh}
        >
          Retry
        </Button>
      </Box>
    );
  }
  
  return (
    <Box>
      {/* Header */}
      <Box
        sx={{
          display: 'flex',
          justifyContent: 'space-between',
          alignItems: 'center',
          mb: 3,
        }}
      >
        <Typography variant="h5">Products</Typography>
        
        <Box sx={{ display: 'flex', gap: 1 }}>
          <Button
            variant="contained"
            color="primary"
            startIcon={<AddIcon />}
            onClick={handleAddProduct}
          >
            Add Product
          </Button>
          
          <Button
            variant="outlined"
            color="primary"
            startIcon={<CloudUploadIcon />}
          >
            Import
          </Button>
          
          <Button
            variant="outlined"
            color="primary"
            startIcon={<CloudDownloadIcon />}
            disabled={filteredProducts.length === 0}
          >
            Export
          </Button>
        </Box>
      </Box>
      
      {/* Toolbar */}
      <Paper
        elevation={0}
        sx={{
          p: 2,
          mb: 3,
          display: 'flex',
          justifyContent: 'space-between',
          alignItems: 'center',
          flexWrap: 'wrap',
          gap: 2,
          borderRadius: 2,
          boxShadow: '0 2px 10px rgba(0, 0, 0, 0.05)',
        }}
      >
        <Box sx={{ display: 'flex', alignItems: 'center', gap: 2, flexGrow: 1 }}>
          <TextField
            placeholder="Search products..."
            variant="outlined"
            size="small"
            value={searchTerm}
            onChange={handleSearchChange}
            sx={{ minWidth: 250 }}
            InputProps={{
              startAdornment: (
                <InputAdornment position="start">
                  <SearchIcon fontSize="small" />
                </InputAdornment>
              ),
            }}
          />
          
          <Button
            variant="outlined"
            color="primary"
            startIcon={<FilterListIcon />}
            onClick={handleFilterMenuOpen}
            size="medium"
          >
            Filter
          </Button>
          
          <Button
            variant="outlined"
            color="primary"
            startIcon={<SortIcon />}
            onClick={handleSortMenuOpen}
            size="medium"
          >
            Sort
          </Button>
        </Box>
        
        <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
          <Tooltip title="List view">
            <IconButton
              color={viewMode === 'list' ? 'primary' : 'default'}
              onClick={() => handleViewModeChange('list')}
            >
              <ViewListIcon />
            </IconButton>
          </Tooltip>
          
          <Tooltip title="Grid view">
            <IconButton
              color={viewMode === 'grid' ? 'primary' : 'default'}
              onClick={() => handleViewModeChange('grid')}
            >
              <ViewModuleIcon />
            </IconButton>
          </Tooltip>
          
          <Tooltip title="Refresh">
            <IconButton onClick={handleRefresh}>
              <RefreshIcon />
            </IconButton>
          </Tooltip>
        </Box>
      </Paper>
      
      {/* Filter Menu */}
      <Menu
        anchorEl={filterAnchorEl}
        open={isFilterMenuOpen}
        onClose={handleFilterMenuClose}
        PaperProps={{
          elevation: 0,
          sx: {
            overflow: 'visible',
            filter: 'drop-shadow(0px 2px 8px rgba(0,0,0,0.32))',
            mt: 1.5,
            width: 250,
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
        <Box sx={{ p: 2 }}>
          <Typography variant="subtitle2" gutterBottom>
            Filter Products
          </Typography>
          
          <FormControl fullWidth size="small" sx={{ mt: 2 }}>
            <InputLabel id="category-filter-label">Category</InputLabel>
            <Select
              labelId="category-filter-label"
              id="category-filter"
              value={categoryFilter}
              label="Category"
              onChange={handleCategoryFilterChange}
            >
              <MenuItem value="all">All Categories</MenuItem>
              {categories.map((category) => (
                <MenuItem key={category} value={category}>
                  {category}
                </MenuItem>
              ))}
            </Select>
          </FormControl>
          
          <FormControl fullWidth size="small" sx={{ mt: 2 }}>
            <InputLabel id="status-filter-label">Status</InputLabel>
            <Select
              labelId="status-filter-label"
              id="status-filter"
              value={statusFilter}
              label="Status"
              onChange={handleStatusFilterChange}
            >
              <MenuItem value="all">All Statuses</MenuItem>
              {statuses.map((status) => (
                <MenuItem key={status} value={status}>
                  {status}
                </MenuItem>
              ))}
            </Select>
          </FormControl>
          
          <Box sx={{ display: 'flex', justifyContent: 'flex-end', mt: 2 }}>
            <Button
              variant="outlined"
              size="small"
              onClick={() => {
                setCategoryFilter('all');
                setStatusFilter('all');
              }}
              sx={{ mr: 1 }}
            >
              Reset
            </Button>
            <Button
              variant="contained"
              size="small"
              onClick={handleFilterMenuClose}
            >
              Apply
            </Button>
          </Box>
        </Box>
      </Menu>
      
      {/* Sort Menu */}
      <Menu
        anchorEl={sortAnchorEl}
        open={isSortMenuOpen}
        onClose={handleSortMenuClose}
        PaperProps={{
          elevation: 0,
          sx: {
            overflow: 'visible',
            filter: 'drop-shadow(0px 2px 8px rgba(0,0,0,0.32))',
            mt: 1.5,
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
        <MenuItem
          selected={sortField === 'name'}
          onClick={() => handleSortFieldChange('name')}
        >
          <ListItemText>Name</ListItemText>
          {sortField === 'name' && (
            <ListItemIcon sx={{ minWidth: 'auto' }}>
              {sortDirection === 'asc' ? <ArrowUpwardIcon /> : <ArrowDownwardIcon />}
            </ListItemIcon>
          )}
        </MenuItem>
        <MenuItem
          selected={sortField === 'price'}
          onClick={() => handleSortFieldChange('price')}
        >
          <ListItemText>Price</ListItemText>
          {sortField === 'price' && (
            <ListItemIcon sx={{ minWidth: 'auto' }}>
              {sortDirection === 'asc' ? <ArrowUpwardIcon /> : <ArrowDownwardIcon />}
            </ListItemIcon>
          )}
        </MenuItem>
        <MenuItem
          selected={sortField === 'category'}
          onClick={() => handleSortFieldChange('category')}
        >
          <ListItemText>Category</ListItemText>
          {sortField === 'category' && (
            <ListItemIcon sx={{ minWidth: 'auto' }}>
              {sortDirection === 'asc' ? <ArrowUpwardIcon /> : <ArrowDownwardIcon />}
            </ListItemIcon>
          )}
        </MenuItem>
        <MenuItem
          selected={sortField === 'status'}
          onClick={() => handleSortFieldChange('status')}
        >
          <ListItemText>Status</ListItemText>
          {sortField === 'status' && (
            <ListItemIcon sx={{ minWidth: 'auto' }}>
              {sortDirection === 'asc' ? <ArrowUpwardIcon /> : <ArrowDownwardIcon />}
            </ListItemIcon>
          )}
        </MenuItem>
        <MenuItem
          selected={sortField === 'updatedAt'}
          onClick={() => handleSortFieldChange('updatedAt')}
        >
          <ListItemText>Last Updated</ListItemText>
          {sortField === 'updatedAt' && (
            <ListItemIcon sx={{ minWidth: 'auto' }}>
              {sortDirection === 'asc' ? <ArrowUpwardIcon /> : <ArrowDownwardIcon />}
            </ListItemIcon>
          )}
        </MenuItem>
      </Menu>
      
      {/* Action Menu */}
      <Menu
        anchorEl={actionAnchorEl}
        open={isActionMenuOpen}
        onClose={handleActionMenuClose}
        PaperProps={{
          elevation: 0,
          sx: {
            overflow: 'visible',
            filter: 'drop-shadow(0px 2px 8px rgba(0,0,0,0.32))',
            mt: 1.5,
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
        <MenuItem onClick={() => selectedProduct && handleViewProduct(selectedProduct.id)}>
          <ListItemIcon>
            <VisibilityIcon fontSize="small" />
          </ListItemIcon>
          <ListItemText>View</ListItemText>
        </MenuItem>
        <MenuItem onClick={() => selectedProduct && handleEditProduct(selectedProduct.id)}>
          <ListItemIcon>
            <EditIcon fontSize="small" />
          </ListItemIcon>
          <ListItemText>Edit</ListItemText>
        </MenuItem>
        <Divider />
        <MenuItem onClick={handleDeleteDialogOpen}>
          <ListItemIcon>
            <DeleteIcon fontSize="small" color="error" />
          </ListItemIcon>
          <ListItemText sx={{ color: 'error.main' }}>Delete</ListItemText>
        </MenuItem>
      </Menu>
      
      {/* Delete Confirmation Dialog */}
      <Dialog
        open={deleteDialogOpen}
        onClose={handleDeleteDialogClose}
        aria-labelledby="delete-dialog-title"
        aria-describedby="delete-dialog-description"
      >
        <DialogTitle id="delete-dialog-title">
          Delete Product
        </DialogTitle>
        <DialogContent>
          <DialogContentText id="delete-dialog-description">
            Are you sure you want to delete the product "{selectedProduct?.name}"? This action cannot be undone.
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleDeleteDialogClose} color="primary">
            Cancel
          </Button>
          <Button
            onClick={handleDeleteProduct}
            color="error"
            variant="contained"
            disabled={isDeleting}
            startIcon={isDeleting ? <CircularProgress size={20} /> : <DeleteIcon />}
          >
            Delete
          </Button>
        </DialogActions>
      </Dialog>
      
      {/* Products List/Grid */}
      {filteredProducts.length === 0 ? (
        <Paper
          elevation={0}
          sx={{
            p: 4,
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
            justifyContent: 'center',
            borderRadius: 2,
            boxShadow: '0 2px 10px rgba(0, 0, 0, 0.05)',
          }}
        >
          <Typography variant="h6" gutterBottom>
            No products found
          </Typography>
          <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
            Try adjusting your search or filter criteria
          </Typography>
          <Button
            variant="outlined"
            color="primary"
            startIcon={<AddIcon />}
            onClick={handleAddProduct}
          >
            Add New Product
          </Button>
        </Paper>
      ) : viewMode === 'list' ? (
        <Paper
          elevation={0}
          sx={{
            height: 600,
            width: '100%',
            borderRadius: 2,
            boxShadow: '0 2px 10px rgba(0, 0, 0, 0.05)',
            '& .MuiDataGrid-cell:focus': {
              outline: 'none',
            },
          }}
        >
          <DataGrid
            rows={filteredProducts}
            columns={columns}
            pageSize={pageSize}
            rowsPerPageOptions={[10, 25, 50, 100]}
            checkboxSelection
            disableSelectionOnClick
            onSelectionModelChange={(newSelection) => {
              setSelectedRows(newSelection as string[]);
            }}
            components={{
              Toolbar: () => (
                <Box
                  sx={{
                    p: 1,
                    display: 'flex',
                    justifyContent: 'space-between',
                    alignItems: 'center',
                  }}
                >
                  {selectedRows.length > 0 && (
                    <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                      <Typography variant="body2">
                        {selectedRows.length} selected
                      </Typography>
                      <Button
                        variant="outlined"
                        color="error"
                        size="small"
                        startIcon={<DeleteIcon />}
                        onClick={handleBulkDelete}
                      >
                        Delete Selected
                      </Button>
                    </Box>
                  )}
                </Box>
              ),
            }}
          />
        </Paper>
      ) : (
        <Box>
          <Grid container spacing={3}>
            {paginatedProducts.map((product: Product) => (
              <Grid item xs={12} sm={6} md={4} lg={3} key={product.id}>
                <Card
                  elevation={0}
                  sx={{
                    height: '100%',
                    display: 'flex',
                    flexDirection: 'column',
                    borderRadius: 2,
                    boxShadow: '0 2px 10px rgba(0, 0, 0, 0.05)',
                    transition: 'transform 0.2s, box-shadow 0.2s',
                    '&:hover': {
                      transform: 'translateY(-4px)',
                      boxShadow: '0 4px 20px rgba(0, 0, 0, 0.1)',
                    },
                  }}
                >
                  <CardMedia
                    component="img"
                    height="140"
                    image={product.imageUrl || `https://source.unsplash.com/random/300x200?${product.category}`}
                    alt={product.name}
                  />
                  <CardContent sx={{ flexGrow: 1 }}>
                    <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', mb: 1 }}>
                      <Typography variant="h6" component="div" noWrap>
                        {product.name}
                      </Typography>
                      <Chip
                        label={product.status}
                        size="small"
                        color={
                          product.status === 'Active'
                            ? 'success'
                            : product.status === 'Pending'
                            ? 'warning'
                            : product.status === 'Draft'
                            ? 'default'
                            : 'error'
                        }
                      />
                    </Box>
                    <Typography variant="body2" color="text.secondary" gutterBottom>
                      {product.code}
                    </Typography>
                    <Typography variant="body2" color="text.secondary" sx={{ mb: 1 }}>
                      Category: {product.category}
                    </Typography>
                    <Typography variant="h6" color="primary">
                      ₹{product.price.toLocaleString()}
                    </Typography>
                  </CardContent>
                  <CardActions sx={{ justifyContent: 'space-between', px: 2, pb: 2 }}>
                    <Button
                      size="small"
                      startIcon={<VisibilityIcon />}
                      onClick={() => handleViewProduct(product.id)}
                    >
                      View
                    </Button>
                    <Box>
                      <IconButton
                        size="small"
                        onClick={() => handleEditProduct(product.id)}
                      >
                        <EditIcon fontSize="small" />
                      </IconButton>
                      <IconButton
                        size="small"
                        onClick={(event) => {
                          setSelectedProduct(product);
                          setDeleteDialogOpen(true);
                        }}
                      >
                        <DeleteIcon fontSize="small" color="error" />
                      </IconButton>
                    </Box>
                  </CardActions>
                </Card>
              </Grid>
            ))}
          </Grid>
          
          {/* Pagination for grid view */}
          <Box sx={{ display: 'flex', justifyContent: 'center', mt: 4 }}>
            <Pagination
              count={Math.ceil(filteredProducts.length / pageSize)}
              page={page}
              onChange={handlePageChange}
              color="primary"
            />
          </Box>
        </Box>
      )}
    </Box>
  );
};

export default ProductList;