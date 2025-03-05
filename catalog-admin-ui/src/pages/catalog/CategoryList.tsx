import React, { useState } from 'react';
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
  Alert,
} from '@mui/material';
import {
  Search as SearchIcon,
  Add as AddIcon,
  FilterList as FilterListIcon,
  ExpandMore as ExpandMoreIcon,
  ChevronRight as ChevronRightIcon,
  Edit as EditIcon,
  Delete as DeleteIcon,
  Visibility as VisibilityIcon,
  MoreVert as MoreVertIcon,
  Refresh as RefreshIcon,
} from '@mui/icons-material';
import { TreeView } from '@mui/x-tree-view/TreeView';
import { TreeItem } from '@mui/x-tree-view/TreeItem';
import { 
  useGetCategoriesQuery, 
  useDeleteCategoryMutation,
  Category,
} from '../../services/catalogApi';
import { showSuccessNotification, showErrorNotification } from '../../store/slices/notificationSlice';

/**
 * CategoryList component displays a hierarchical list of categories and allows users to manage them.
 * It provides features like searching, filtering, and CRUD operations.
 */
const CategoryList: React.FC = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  
  // Get categories from API
  const { data: categories, isLoading, error, refetch } = useGetCategoriesQuery();
  const [deleteCategory, { isLoading: isDeleting }] = useDeleteCategoryMutation();
  
  // Local state
  const [searchTerm, setSearchTerm] = useState('');
  const [filterAnchorEl, setFilterAnchorEl] = useState<null | HTMLElement>(null);
  const [actionAnchorEl, setActionAnchorEl] = useState<null | HTMLElement>(null);
  const [selectedCategory, setSelectedCategory] = useState<Category | null>(null);
  const [deleteDialogOpen, setDeleteDialogOpen] = useState(false);
  const [expanded, setExpanded] = useState<string[]>([]);
  const [selected, setSelected] = useState<string>('');
  
  // Filter menu open state
  const isFilterMenuOpen = Boolean(filterAnchorEl);
  
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
  
  // Handle opening action menu
  const handleActionMenuOpen = (event: React.MouseEvent<HTMLElement>, category: Category) => {
    event.stopPropagation();
    setActionAnchorEl(event.currentTarget);
    setSelectedCategory(category);
  };
  
  // Handle closing action menu
  const handleActionMenuClose = () => {
    setActionAnchorEl(null);
  };
  
  // Handle search input change
  const handleSearchChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setSearchTerm(event.target.value);
  };
  
  // Handle tree node expansion
  const handleToggle = (event: React.SyntheticEvent, nodeIds: string[]) => {
    setExpanded(nodeIds);
  };
  
  // Handle tree node selection
  const handleSelect = (event: React.SyntheticEvent, nodeId: string) => {
    setSelected(nodeId);
  };
  
  // Handle adding a new category
  const handleAddCategory = () => {
    navigate('/catalog/categories/new');
  };
  
  // Handle editing a category
  const handleEditCategory = (id: string) => {
    navigate(`/catalog/categories/${id}`);
    handleActionMenuClose();
  };
  
  // Handle viewing a category
  const handleViewCategory = (id: string) => {
    navigate(`/catalog/categories/${id}?view=true`);
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
  
  // Handle deleting a category
  const handleDeleteCategory = async () => {
    if (selectedCategory) {
      try {
        await deleteCategory(selectedCategory.id).unwrap();
        dispatch(showSuccessNotification(`Category "${selectedCategory.name}" deleted successfully`));
        handleDeleteDialogClose();
      } catch (error: any) {
        dispatch(showErrorNotification(`Failed to delete category: ${error.message}`));
      }
    }
  };
  
  // Handle refresh
  const handleRefresh = () => {
    refetch();
  };
  
  // Filter categories based on search term
  const filteredCategories = React.useMemo(() => {
    if (!categories) return [];
    
    if (!searchTerm) return categories;
    
    return categories.filter((category) =>
      category.name.toLowerCase().includes(searchTerm.toLowerCase())
    );
  }, [categories, searchTerm]);
  
  // Build category tree
  const buildCategoryTree = (categories: Category[], parentId?: string): React.ReactNode => {
    const filteredByParent = categories.filter(
      (category) => category.parentId === parentId
    );
    
    return filteredByParent.map((category) => (
      <TreeItem
        key={category.id}
        nodeId={category.id}
        label={
          <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', py: 1 }}>
            <Box sx={{ display: 'flex', alignItems: 'center' }}>
              <Typography variant="body1">{category.name}</Typography>
              <Chip
                label={category.status}
                size="small"
                color={category.status === 'Active' ? 'success' : 'default'}
                sx={{ ml: 1 }}
              />
            </Box>
            <IconButton
              size="small"
              onClick={(event) => handleActionMenuOpen(event, category)}
            >
              <MoreVertIcon fontSize="small" />
            </IconButton>
          </Box>
        }
      >
        {buildCategoryTree(categories, category.id)}
      </TreeItem>
    ));
  };
  
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
          Loading Categories...
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
        <Alert severity="error" sx={{ width: '100%', maxWidth: 600, mb: 2 }}>
          Error loading categories. Please try again.
        </Alert>
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
        <Typography variant="h5">Categories</Typography>
        
        <Box sx={{ display: 'flex', gap: 1 }}>
          <Button
            variant="contained"
            color="primary"
            startIcon={<AddIcon />}
            onClick={handleAddCategory}
          >
            Add Category
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
            placeholder="Search categories..."
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
        </Box>
        
        <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
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
        <MenuItem onClick={handleFilterMenuClose}>
          <ListItemText>All Categories</ListItemText>
        </MenuItem>
        <MenuItem onClick={handleFilterMenuClose}>
          <ListItemText>Active Categories</ListItemText>
        </MenuItem>
        <MenuItem onClick={handleFilterMenuClose}>
          <ListItemText>Inactive Categories</ListItemText>
        </MenuItem>
        <Divider />
        <MenuItem onClick={handleFilterMenuClose}>
          <ListItemText>Top-Level Categories</ListItemText>
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
        <MenuItem onClick={() => selectedCategory && handleViewCategory(selectedCategory.id)}>
          <ListItemIcon>
            <VisibilityIcon fontSize="small" />
          </ListItemIcon>
          <ListItemText>View</ListItemText>
        </MenuItem>
        <MenuItem onClick={() => selectedCategory && handleEditCategory(selectedCategory.id)}>
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
          Delete Category
        </DialogTitle>
        <DialogContent>
          <DialogContentText id="delete-dialog-description">
            Are you sure you want to delete the category "{selectedCategory?.name}"? This action cannot be undone.
            {selectedCategory && (
              <Typography variant="body2" color="error" sx={{ mt: 2 }}>
                Warning: Deleting this category may affect products assigned to it.
              </Typography>
            )}
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleDeleteDialogClose} color="primary">
            Cancel
          </Button>
          <Button
            onClick={handleDeleteCategory}
            color="error"
            variant="contained"
            disabled={isDeleting}
            startIcon={isDeleting ? <CircularProgress size={20} /> : <DeleteIcon />}
          >
            Delete
          </Button>
        </DialogActions>
      </Dialog>
      
      {/* Categories Tree */}
      <Paper
        elevation={0}
        sx={{
          p: 2,
          borderRadius: 2,
          boxShadow: '0 2px 10px rgba(0, 0, 0, 0.05)',
          minHeight: 400,
        }}
      >
        {filteredCategories.length === 0 ? (
          <Box
            sx={{
              display: 'flex',
              flexDirection: 'column',
              alignItems: 'center',
              justifyContent: 'center',
              height: 300,
            }}
          >
            <Typography variant="h6" gutterBottom>
              No categories found
            </Typography>
            <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
              {searchTerm ? 'Try adjusting your search criteria' : 'Start by adding a new category'}
            </Typography>
            <Button
              variant="outlined"
              color="primary"
              startIcon={<AddIcon />}
              onClick={handleAddCategory}
            >
              Add New Category
            </Button>
          </Box>
        ) : (
          <TreeView
            aria-label="category tree"
            defaultCollapseIcon={<ExpandMoreIcon />}
            defaultExpandIcon={<ChevronRightIcon />}
            expanded={expanded}
            selected={selected}
            onNodeToggle={handleToggle}
            onNodeSelect={handleSelect}
            sx={{ flexGrow: 1, overflowY: 'auto' }}
          >
            {buildCategoryTree(filteredCategories)}
          </TreeView>
        )}
      </Paper>
    </Box>
  );
};

export default CategoryList;