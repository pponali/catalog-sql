import React, { useState, useEffect } from 'react';
import { useParams, useNavigate, useLocation } from 'react-router-dom';
import { useDispatch } from 'react-redux';
import {
  Box,
  Paper,
  Typography,
  Button,
  TextField,
  Grid,
  Tabs,
  Tab,
  Divider,
  Chip,
  CircularProgress,
  IconButton,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  FormHelperText,
  Breadcrumbs,
  Link,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogContentText,
  DialogActions,
  Card,
  CardMedia,
  CardContent,
  Tooltip,
  Alert,
  Autocomplete,
} from '@mui/material';
import {
  Save as SaveIcon,
  Cancel as CancelIcon,
  Delete as DeleteIcon,
  ArrowBack as ArrowBackIcon,
  Image as ImageIcon,
  Add as AddIcon,
  Edit as EditIcon,
  Visibility as VisibilityIcon,
  CloudUpload as CloudUploadIcon,
} from '@mui/icons-material';
import { Formik, Form, Field, ErrorMessage } from 'formik';
import * as Yup from 'yup';
import { 
  useGetCategoryQuery, 
  useGetCategoriesQuery,
  useCreateCategoryMutation, 
  useUpdateCategoryMutation, 
  useDeleteCategoryMutation,
  Category,
  CreateCategoryRequest,
  UpdateCategoryRequest,
} from '../../services/catalogApi';
import { showSuccessNotification, showErrorNotification } from '../../store/slices/notificationSlice';

// Define interface for tab panel props
interface TabPanelProps {
  children?: React.ReactNode;
  index: number;
  value: number;
}

// Tab Panel component
const TabPanel: React.FC<TabPanelProps> = (props) => {
  const { children, value, index, ...other } = props;

  return (
    <div
      role="tabpanel"
      hidden={value !== index}
      id={`category-tabpanel-${index}`}
      aria-labelledby={`category-tab-${index}`}
      {...other}
    >
      {value === index && (
        <Box sx={{ p: 3 }}>
          {children}
        </Box>
      )}
    </div>
  );
};

// Helper function for tab accessibility
const a11yProps = (index: number) => {
  return {
    id: `category-tab-${index}`,
    'aria-controls': `category-tabpanel-${index}`,
  };
};

// Validation schema for category form
const categoryValidationSchema = Yup.object().shape({
  name: Yup.string().required('Name is required').max(100, 'Name must be at most 100 characters'),
  description: Yup.string().max(500, 'Description must be at most 500 characters'),
  parentId: Yup.string(),
  status: Yup.string().required('Status is required'),
});

/**
 * CategoryDetail component displays and allows editing of a category's details.
 * It handles both creating new categories and editing existing ones.
 */
const CategoryDetail: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const location = useLocation();
  const dispatch = useDispatch();
  
  // Check if we're in view-only mode
  const isViewMode = new URLSearchParams(location.search).get('view') === 'true';
  
  // Check if we're creating a new category
  const isNewCategory = id === 'new';
  
  // State for the active tab
  const [activeTab, setActiveTab] = useState(0);
  
  // State for delete confirmation dialog
  const [deleteDialogOpen, setDeleteDialogOpen] = useState(false);
  
  // Get category data from API
  const { data: category, isLoading: isLoadingCategory, error: categoryError } = 
    useGetCategoryQuery(id as string, { skip: isNewCategory });
  
  // Get categories for parent dropdown
  const { data: categories, isLoading: isLoadingCategories } = useGetCategoriesQuery();
  
  // Mutations for CRUD operations
  const [createCategory, { isLoading: isCreating }] = useCreateCategoryMutation();
  const [updateCategory, { isLoading: isUpdating }] = useUpdateCategoryMutation();
  const [deleteCategory, { isLoading: isDeleting }] = useDeleteCategoryMutation();
  
  // Handle tab change
  const handleTabChange = (event: React.SyntheticEvent, newValue: number) => {
    setActiveTab(newValue);
  };
  
  // Handle form submission
  const handleSubmit = async (values: any) => {
    try {
      if (isNewCategory) {
        // Create new category
        const createRequest: CreateCategoryRequest = {
          name: values.name,
          description: values.description || '',
          parentId: values.parentId || undefined,
          status: values.status,
          imageUrl: values.imageUrl,
        };
        
        await createCategory(createRequest).unwrap();
        dispatch(showSuccessNotification('Category created successfully'));
        navigate('/catalog/categories');
      } else {
        // Update existing category
        const updateRequest: UpdateCategoryRequest = {
          name: values.name,
          description: values.description || '',
          parentId: values.parentId || undefined,
          status: values.status,
          imageUrl: values.imageUrl,
        };
        
        await updateCategory({ id: id as string, category: updateRequest }).unwrap();
        dispatch(showSuccessNotification('Category updated successfully'));
        
        if (isViewMode) {
          // If we were in view mode, stay in view mode
          navigate(`/catalog/categories/${id}?view=true`);
        }
      }
    } catch (error: any) {
      dispatch(showErrorNotification(`Failed to ${isNewCategory ? 'create' : 'update'} category: ${error.message}`));
    }
  };
  
  // Handle delete button click
  const handleDeleteClick = () => {
    setDeleteDialogOpen(true);
  };
  
  // Handle delete confirmation
  const handleDeleteConfirm = async () => {
    try {
      await deleteCategory(id as string).unwrap();
      dispatch(showSuccessNotification('Category deleted successfully'));
      navigate('/catalog/categories');
    } catch (error: any) {
      dispatch(showErrorNotification(`Failed to delete category: ${error.message}`));
    } finally {
      setDeleteDialogOpen(false);
    }
  };
  
  // Handle cancel button click
  const handleCancel = () => {
    if (isNewCategory) {
      navigate('/catalog/categories');
    } else {
      navigate(`/catalog/categories/${id}${isViewMode ? '?view=true' : ''}`);
    }
  };
  
  // Handle edit button click (switch from view to edit mode)
  const handleEdit = () => {
    navigate(`/catalog/categories/${id}`);
  };
  
  // Filter out the current category from parent options to prevent circular references
  const parentOptions = React.useMemo(() => {
    if (!categories) return [];
    
    return categories.filter((cat) => cat.id !== id);
  }, [categories, id]);
  
  // Initial form values
  const initialValues = isNewCategory
    ? {
        name: '',
        description: '',
        parentId: '',
        status: 'Active',
        imageUrl: '',
      }
    : {
        name: category?.name || '',
        description: category?.description || '',
        parentId: category?.parentId || '',
        status: category?.status || 'Active',
        imageUrl: category?.imageUrl || '',
      };
  
  // Show loading state
  if ((isLoadingCategory && !isNewCategory) || isLoadingCategories) {
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
          {isNewCategory ? 'Preparing new category form...' : 'Loading category details...'}
        </Typography>
      </Box>
    );
  }
  
  // Show error state
  if (categoryError && !isNewCategory) {
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
          Error loading category details. Please try again.
        </Alert>
        <Button
          variant="contained"
          color="primary"
          startIcon={<ArrowBackIcon />}
          onClick={() => navigate('/catalog/categories')}
        >
          Back to Categories
        </Button>
      </Box>
    );
  }
  
  return (
    <Box>
      {/* Breadcrumbs */}
      <Breadcrumbs aria-label="breadcrumb" sx={{ mb: 3 }}>
        <Link
          underline="hover"
          color="inherit"
          onClick={() => navigate('/catalog/categories')}
          sx={{ cursor: 'pointer' }}
        >
          Categories
        </Link>
        <Typography color="text.primary">
          {isNewCategory ? 'New Category' : category?.name || 'Category Details'}
        </Typography>
      </Breadcrumbs>
      
      {/* Header */}
      <Box
        sx={{
          display: 'flex',
          justifyContent: 'space-between',
          alignItems: 'center',
          mb: 3,
        }}
      >
        <Typography variant="h5">
          {isNewCategory ? 'Create New Category' : isViewMode ? 'Category Details' : 'Edit Category'}
        </Typography>
        
        <Box sx={{ display: 'flex', gap: 1 }}>
          {isViewMode ? (
            <>
              <Button
                variant="outlined"
                color="primary"
                startIcon={<EditIcon />}
                onClick={handleEdit}
              >
                Edit
              </Button>
              <Button
                variant="outlined"
                color="error"
                startIcon={<DeleteIcon />}
                onClick={handleDeleteClick}
              >
                Delete
              </Button>
            </>
          ) : (
            <Button
              variant="outlined"
              color="primary"
              startIcon={<ArrowBackIcon />}
              onClick={() => navigate('/catalog/categories')}
            >
              Back to Categories
            </Button>
          )}
        </Box>
      </Box>
      
      {/* Category Form */}
      <Formik
        initialValues={initialValues}
        validationSchema={categoryValidationSchema}
        onSubmit={handleSubmit}
        enableReinitialize
      >
        {({ values, errors, touched, handleChange, handleBlur, isValid, dirty }) => (
          <Form>
            <Paper
              elevation={0}
              sx={{
                borderRadius: 2,
                boxShadow: '0 2px 10px rgba(0, 0, 0, 0.05)',
                overflow: 'hidden',
              }}
            >
              {/* Tabs */}
              <Box sx={{ borderBottom: 1, borderColor: 'divider' }}>
                <Tabs
                  value={activeTab}
                  onChange={handleTabChange}
                  aria-label="category tabs"
                  variant="scrollable"
                  scrollButtons="auto"
                >
                  <Tab label="Basic Information" {...a11yProps(0)} />
                  <Tab label="Attributes" {...a11yProps(1)} />
                  <Tab label="Products" {...a11yProps(2)} />
                </Tabs>
              </Box>
              
              {/* Basic Information Tab */}
              <TabPanel value={activeTab} index={0}>
                <Grid container spacing={3}>
                  {/* Category Image */}
                  <Grid item xs={12} md={4}>
                    <Card
                      elevation={0}
                      sx={{
                        height: '100%',
                        display: 'flex',
                        flexDirection: 'column',
                        borderRadius: 2,
                        boxShadow: '0 2px 10px rgba(0, 0, 0, 0.05)',
                      }}
                    >
                      {values.imageUrl ? (
                        <CardMedia
                          component="img"
                          height="200"
                          image={values.imageUrl}
                          alt={values.name}
                        />
                      ) : (
                        <Box
                          sx={{
                            height: 200,
                            display: 'flex',
                            alignItems: 'center',
                            justifyContent: 'center',
                            bgcolor: 'action.hover',
                          }}
                        >
                          <ImageIcon sx={{ fontSize: 60, color: 'text.secondary' }} />
                        </Box>
                      )}
                      <CardContent>
                        <Typography variant="body2" color="text.secondary" gutterBottom>
                          Category Image
                        </Typography>
                        {!isViewMode && (
                          <Button
                            variant="outlined"
                            startIcon={<CloudUploadIcon />}
                            fullWidth
                            sx={{ mt: 1 }}
                            disabled
                          >
                            Upload Image
                          </Button>
                        )}
                      </CardContent>
                    </Card>
                  </Grid>
                  
                  {/* Category Details */}
                  <Grid item xs={12} md={8}>
                    <Grid container spacing={2}>
                      <Grid item xs={12}>
                        <Field
                          as={TextField}
                          name="name"
                          label="Category Name"
                          fullWidth
                          required
                          disabled={isViewMode}
                          error={touched.name && Boolean(errors.name)}
                          helperText={touched.name && errors.name}
                          onChange={handleChange}
                          onBlur={handleBlur}
                          value={values.name}
                        />
                      </Grid>
                      <Grid item xs={12}>
                        <Field
                          as={TextField}
                          name="description"
                          label="Description"
                          fullWidth
                          multiline
                          rows={4}
                          disabled={isViewMode}
                          error={touched.description && Boolean(errors.description)}
                          helperText={touched.description && errors.description}
                          onChange={handleChange}
                          onBlur={handleBlur}
                          value={values.description}
                        />
                      </Grid>
                      <Grid item xs={12} sm={6}>
                        <FormControl
                          fullWidth
                          error={touched.parentId && Boolean(errors.parentId)}
                          disabled={isViewMode}
                        >
                          <InputLabel id="parent-category-label">Parent Category</InputLabel>
                          <Field
                            as={Select}
                            labelId="parent-category-label"
                            name="parentId"
                            label="Parent Category"
                            onChange={handleChange}
                            onBlur={handleBlur}
                            value={values.parentId}
                          >
                            <MenuItem value="">
                              <em>None (Top Level)</em>
                            </MenuItem>
                            {parentOptions.map((category) => (
                              <MenuItem key={category.id} value={category.id}>
                                {category.name}
                              </MenuItem>
                            ))}
                          </Field>
                          {touched.parentId && errors.parentId && (
                            <FormHelperText>{errors.parentId}</FormHelperText>
                          )}
                        </FormControl>
                      </Grid>
                      <Grid item xs={12} sm={6}>
                        <FormControl
                          fullWidth
                          required
                          error={touched.status && Boolean(errors.status)}
                          disabled={isViewMode}
                        >
                          <InputLabel id="status-label">Status</InputLabel>
                          <Field
                            as={Select}
                            labelId="status-label"
                            name="status"
                            label="Status"
                            onChange={handleChange}
                            onBlur={handleBlur}
                            value={values.status}
                          >
                            <MenuItem value="Active">Active</MenuItem>
                            <MenuItem value="Inactive">Inactive</MenuItem>
                          </Field>
                          {touched.status && errors.status && (
                            <FormHelperText>{errors.status}</FormHelperText>
                          )}
                        </FormControl>
                      </Grid>
                      <Grid item xs={12}>
                        <Field
                          as={TextField}
                          name="imageUrl"
                          label="Image URL"
                          fullWidth
                          disabled={isViewMode}
                          error={touched.imageUrl && Boolean(errors.imageUrl)}
                          helperText={touched.imageUrl && errors.imageUrl}
                          onChange={handleChange}
                          onBlur={handleBlur}
                          value={values.imageUrl}
                        />
                      </Grid>
                    </Grid>
                  </Grid>
                </Grid>
              </TabPanel>
              
              {/* Attributes Tab */}
              <TabPanel value={activeTab} index={1}>
                <Typography variant="body1" color="text.secondary" paragraph>
                  Category attributes will be displayed here. This feature is under development.
                </Typography>
              </TabPanel>
              
              {/* Products Tab */}
              <TabPanel value={activeTab} index={2}>
                <Typography variant="body1" color="text.secondary" paragraph>
                  Products in this category will be displayed here. This feature is under development.
                </Typography>
              </TabPanel>
              
              {/* Form Actions */}
              {!isViewMode && (
                <Box
                  sx={{
                    display: 'flex',
                    justifyContent: 'flex-end',
                    p: 2,
                    borderTop: 1,
                    borderColor: 'divider',
                  }}
                >
                  <Button
                    variant="outlined"
                    color="inherit"
                    onClick={handleCancel}
                    sx={{ mr: 1 }}
                    startIcon={<CancelIcon />}
                  >
                    Cancel
                  </Button>
                  <Button
                    type="submit"
                    variant="contained"
                    color="primary"
                    startIcon={<SaveIcon />}
                    disabled={isCreating || isUpdating || (!isValid && dirty)}
                  >
                    {isCreating || isUpdating ? (
                      <>
                        <CircularProgress size={24} sx={{ mr: 1 }} />
                        {isNewCategory ? 'Creating...' : 'Saving...'}
                      </>
                    ) : (
                      isNewCategory ? 'Create Category' : 'Save Changes'
                    )}
                  </Button>
                </Box>
              )}
            </Paper>
          </Form>
        )}
      </Formik>
      
      {/* Delete Confirmation Dialog */}
      <Dialog
        open={deleteDialogOpen}
        onClose={() => setDeleteDialogOpen(false)}
        aria-labelledby="delete-dialog-title"
        aria-describedby="delete-dialog-description"
      >
        <DialogTitle id="delete-dialog-title">
          Delete Category
        </DialogTitle>
        <DialogContent>
          <DialogContentText id="delete-dialog-description">
            Are you sure you want to delete this category? This action cannot be undone.
            <Typography variant="body2" color="error" sx={{ mt: 2 }}>
              Warning: Deleting this category may affect products assigned to it.
            </Typography>
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setDeleteDialogOpen(false)} color="primary">
            Cancel
          </Button>
          <Button
            onClick={handleDeleteConfirm}
            color="error"
            variant="contained"
            disabled={isDeleting}
            startIcon={isDeleting ? <CircularProgress size={20} /> : <DeleteIcon />}
          >
            {isDeleting ? 'Deleting...' : 'Delete'}
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
};

export default CategoryDetail;