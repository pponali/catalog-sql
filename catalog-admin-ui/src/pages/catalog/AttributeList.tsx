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
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  TablePagination,
  Alert,
} from '@mui/material';
import {
  Search as SearchIcon,
  Add as AddIcon,
  FilterList as FilterListIcon,
  Sort as SortIcon,
  Edit as EditIcon,
  Delete as DeleteIcon,
  Visibility as VisibilityIcon,
  MoreVert as MoreVertIcon,
  Refresh as RefreshIcon,
  ArrowUpward as ArrowUpwardIcon,
  ArrowDownward as ArrowDownwardIcon,
} from '@mui/icons-material';
import { 
  useGetAttributesQuery, 
  useDeleteAttributeMutation,
  Attribute,
} from '../../services/catalogApi';
import { showSuccessNotification, showErrorNotification } from '../../store/slices/notificationSlice';

/**
 * AttributeList component displays a list of attributes and allows users to manage them.
 * It provides features like searching, filtering, sorting, and CRUD operations.
 */
const AttributeList: React.FC = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  
  // Get attributes from API
  const { data: attributes, isLoading, error, refetch } = useGetAttributesQuery();
  const [deleteAttribute, { isLoading: isDeleting }] = useDeleteAttributeMutation();
  
  // Local state
  const [searchTerm, setSearchTerm] = useState('');
  const [filterAnchorEl, setFilterAnchorEl] = useState<null | HTMLElement>(null);
  const [sortAnchorEl, setSortAnchorEl] = useState<null | HTMLElement>(null);
  const [actionAnchorEl, setActionAnchorEl] = useState<null | HTMLElement>(null);
  const [selectedAttribute, setSelectedAttribute] = useState<Attribute | null>(null);
  const [deleteDialogOpen, setDeleteDialogOpen] = useState(false);
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(10);
  const [typeFilter, setTypeFilter] = useState<string>('all');
  const [sortField, setSortField] = useState<string>('name');
  const [sortDirection, setSortDirection] = useState<'asc' | 'desc'>('asc');
  
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
  const handleActionMenuOpen = (event: React.MouseEvent<HTMLElement>, attribute: Attribute) => {
    setActionAnchorEl(event.currentTarget);
    setSelectedAttribute(attribute);
  };
  
  // Handle closing action menu
  const handleActionMenuClose = () => {
    setActionAnchorEl(null);
  };
  
  // Handle search input change
  const handleSearchChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setSearchTerm(event.target.value);
  };
  
  // Handle type filter change
  const handleTypeFilterChange = (type: string) => {
    setTypeFilter(type);
    handleFilterMenuClose();
  };
  
  // Handle sort field change
  const handleSortFieldChange = (field: string) => {
    if (sortField === field) {
      // Toggle sort direction if the same field is selected
      setSortDirection(sortDirection === 'asc' ? 'desc' : 'asc');
    } else {
      // Set new sort field and default to ascending
      setSortField(field);
      setSortDirection('asc');
    }
    handleSortMenuClose();
  };
  
  // Handle page change
  const handleChangePage = (event: unknown, newPage: number) => {
    setPage(newPage);
  };
  
  // Handle rows per page change
  const handleChangeRowsPerPage = (event: React.ChangeEvent<HTMLInputElement>) => {
    setRowsPerPage(parseInt(event.target.value, 10));
    setPage(0);
  };
  
  // Handle adding a new attribute
  const handleAddAttribute = () => {
    navigate('/catalog/attributes/new');
  };
  
  // Handle editing an attribute
  const handleEditAttribute = (id: string) => {
    navigate(`/catalog/attributes/${id}`);
    handleActionMenuClose();
  };
  
  // Handle viewing an attribute
  const handleViewAttribute = (id: string) => {
    navigate(`/catalog/attributes/${id}?view=true`);
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
  
  // Handle deleting an attribute
  const handleDeleteAttribute = async () => {
    if (selectedAttribute) {
      try {
        await deleteAttribute(selectedAttribute.id).unwrap();
        dispatch(showSuccessNotification(`Attribute "${selectedAttribute.name}" deleted successfully`));
        handleDeleteDialogClose();
      } catch (error: any) {
        dispatch(showErrorNotification(`Failed to delete attribute: ${error.message}`));
      }
    }
  };
  
  // Handle refresh
  const handleRefresh = () => {
    refetch();
  };
  
  // Filter and sort attributes
  const filteredAttributes = React.useMemo(() => {
    if (!attributes) return [];
    
    return attributes
      .filter((attribute) => {
        // Apply search filter
        const matchesSearch = searchTerm === '' || 
          attribute.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
          attribute.code.toLowerCase().includes(searchTerm.toLowerCase());
        
        // Apply type filter
        const matchesType = typeFilter === 'all' || attribute.type === typeFilter;
        
        return matchesSearch && matchesType;
      })
      .sort((a, b) => {
        // Apply sorting
        if (a[sortField as keyof Attribute] < b[sortField as keyof Attribute]) {
          return sortDirection === 'asc' ? -1 : 1;
        }
        if (a[sortField as keyof Attribute] > b[sortField as keyof Attribute]) {
          return sortDirection === 'asc' ? 1 : -1;
        }
        return 0;
      });
  }, [attributes, searchTerm, typeFilter, sortField, sortDirection]);
  
  // Get paginated attributes
  const paginatedAttributes = React.useMemo(() => {
    return filteredAttributes.slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage);
  }, [filteredAttributes, page, rowsPerPage]);
  
  // Get unique attribute types for filter
  const attributeTypes = React.useMemo(() => {
    if (!attributes) return [];
    
    const uniqueTypes = new Set<string>();
    attributes.forEach((attribute) => {
      uniqueTypes.add(attribute.type);
    });
    
    return Array.from(uniqueTypes).sort();
  }, [attributes]);
  
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
          Loading Attributes...
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
          Error loading attributes. Please try again.
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
        <Typography variant="h5">Attributes</Typography>
        
        <Box sx={{ display: 'flex', gap: 1 }}>
          <Button
            variant="contained"
            color="primary"
            startIcon={<AddIcon />}
            onClick={handleAddAttribute}
          >
            Add Attribute
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
            placeholder="Search attributes..."
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
        <MenuItem
          selected={typeFilter === 'all'}
          onClick={() => handleTypeFilterChange('all')}
        >
          <ListItemText>All Types</ListItemText>
        </MenuItem>
        <Divider />
        {attributeTypes.map((type) => (
          <MenuItem
            key={type}
            selected={typeFilter === type}
            onClick={() => handleTypeFilterChange(type)}
          >
            <ListItemText>{type.charAt(0).toUpperCase() + type.slice(1)}</ListItemText>
          </MenuItem>
        ))}
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
          selected={sortField === 'code'}
          onClick={() => handleSortFieldChange('code')}
        >
          <ListItemText>Code</ListItemText>
          {sortField === 'code' && (
            <ListItemIcon sx={{ minWidth: 'auto' }}>
              {sortDirection === 'asc' ? <ArrowUpwardIcon /> : <ArrowDownwardIcon />}
            </ListItemIcon>
          )}
        </MenuItem>
        <MenuItem
          selected={sortField === 'type'}
          onClick={() => handleSortFieldChange('type')}
        >
          <ListItemText>Type</ListItemText>
          {sortField === 'type' && (
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
        <MenuItem onClick={() => selectedAttribute && handleViewAttribute(selectedAttribute.id)}>
          <ListItemIcon>
            <VisibilityIcon fontSize="small" />
          </ListItemIcon>
          <ListItemText>View</ListItemText>
        </MenuItem>
        <MenuItem onClick={() => selectedAttribute && handleEditAttribute(selectedAttribute.id)}>
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
          Delete Attribute
        </DialogTitle>
        <DialogContent>
          <DialogContentText id="delete-dialog-description">
            Are you sure you want to delete the attribute "{selectedAttribute?.name}"? This action cannot be undone.
            <Typography variant="body2" color="error" sx={{ mt: 2 }}>
              Warning: Deleting this attribute may affect products using it.
            </Typography>
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleDeleteDialogClose} color="primary">
            Cancel
          </Button>
          <Button
            onClick={handleDeleteAttribute}
            color="error"
            variant="contained"
            disabled={isDeleting}
            startIcon={isDeleting ? <CircularProgress size={20} /> : <DeleteIcon />}
          >
            Delete
          </Button>
        </DialogActions>
      </Dialog>
      
      {/* Attributes Table */}
      <Paper
        elevation={0}
        sx={{
          borderRadius: 2,
          boxShadow: '0 2px 10px rgba(0, 0, 0, 0.05)',
          overflow: 'hidden',
        }}
      >
        {filteredAttributes.length === 0 ? (
          <Box
            sx={{
              display: 'flex',
              flexDirection: 'column',
              alignItems: 'center',
              justifyContent: 'center',
              height: 300,
              p: 3,
            }}
          >
            <Typography variant="h6" gutterBottom>
              No attributes found
            </Typography>
            <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
              {searchTerm || typeFilter !== 'all' ? 'Try adjusting your search or filter criteria' : 'Start by adding a new attribute'}
            </Typography>
            <Button
              variant="outlined"
              color="primary"
              startIcon={<AddIcon />}
              onClick={handleAddAttribute}
            >
              Add New Attribute
            </Button>
          </Box>
        ) : (
          <>
            <TableContainer>
              <Table>
                <TableHead>
                  <TableRow>
                    <TableCell>Name</TableCell>
                    <TableCell>Code</TableCell>
                    <TableCell>Type</TableCell>
                    <TableCell>Required</TableCell>
                    <TableCell>Options</TableCell>
                    <TableCell align="right">Actions</TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  {paginatedAttributes.map((attribute) => (
                    <TableRow key={attribute.id}>
                      <TableCell>{attribute.name}</TableCell>
                      <TableCell>{attribute.code}</TableCell>
                      <TableCell>
                        <Chip
                          label={attribute.type.charAt(0).toUpperCase() + attribute.type.slice(1)}
                          size="small"
                          color={
                            attribute.type === 'text'
                              ? 'primary'
                              : attribute.type === 'number'
                              ? 'secondary'
                              : attribute.type === 'boolean'
                              ? 'success'
                              : attribute.type === 'date'
                              ? 'info'
                              : attribute.type === 'select' || attribute.type === 'multiselect'
                              ? 'warning'
                              : 'default'
                          }
                        />
                      </TableCell>
                      <TableCell>
                        {attribute.required ? (
                          <Chip label="Required" size="small" color="error" />
                        ) : (
                          <Chip label="Optional" size="small" variant="outlined" />
                        )}
                      </TableCell>
                      <TableCell>
                        {attribute.options && attribute.options.length > 0 ? (
                          <Chip
                            label={`${attribute.options.length} options`}
                            size="small"
                            color="info"
                          />
                        ) : (
                          <Typography variant="body2" color="text.secondary">
                            -
                          </Typography>
                        )}
                      </TableCell>
                      <TableCell align="right">
                        <Tooltip title="View">
                          <IconButton
                            size="small"
                            onClick={() => handleViewAttribute(attribute.id)}
                          >
                            <VisibilityIcon fontSize="small" />
                          </IconButton>
                        </Tooltip>
                        <Tooltip title="Edit">
                          <IconButton
                            size="small"
                            onClick={() => handleEditAttribute(attribute.id)}
                          >
                            <EditIcon fontSize="small" />
                          </IconButton>
                        </Tooltip>
                        <Tooltip title="More Actions">
                          <IconButton
                            size="small"
                            onClick={(event) => handleActionMenuOpen(event, attribute)}
                          >
                            <MoreVertIcon fontSize="small" />
                          </IconButton>
                        </Tooltip>
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </TableContainer>
            <TablePagination
              rowsPerPageOptions={[5, 10, 25, 50]}
              component="div"
              count={filteredAttributes.length}
              rowsPerPage={rowsPerPage}
              page={page}
              onPageChange={handleChangePage}
              onRowsPerPageChange={handleChangeRowsPerPage}
            />
          </>
        )}
      </Paper>
    </Box>
  );
};

export default AttributeList;