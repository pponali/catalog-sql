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
import { Formik, Form, Field, FieldArray, ErrorMessage } from 'formik';
import * as Yup from 'yup';
import { 
  useGetProductQuery, 
  useCreateProductMutation, 
  useUpdateProductMutation, 
  useDeleteProductMutation,
  useGetCategoriesQuery,
  Product,
  CreateProductRequest,
  UpdateProductRequest,
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
      id={`product-tabpanel-${index}`}
      aria-labelledby={`product-tab-${index}`}
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
    id: `product-tab-${index}`,
    'aria-controls': `product-tabpanel-${index}`,
  };
};

// Validation schema for product form
const productValidationSchema = Yup.object().shape({
  name: Yup.string().required('Name is required').max(100, 'Name must be at most 100 characters'),
  code: Yup.string().required('Code is required').max(50, 'Code must be at most 50 characters'),
  description: Yup.string().required('Description is required'),
  categoryId: Yup.string().required('Category is required'),
  price: Yup.number().required('Price is required').min(0, 'Price must be a positive number'),
  status: Yup.string().required('Status is required'),
  attributes: Yup.object(),
});

/**
 * ProductDetail component displays and allows editing of a product's details.
 * It handles both creating new products and editing existing ones.
 */
const ProductDetail: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const location = useLocation();
  const dispatch = useDispatch();
  
  // Check if we're in view-only mode
  const isViewMode = new URLSearchParams(location.search).get('view') === 'true';
  
  // Check if we're creating a new product
  const isNewProduct = id === 'new';
  
  // State for the active tab
  const [activeTab, setActiveTab] = useState(0);
  
  // State for delete confirmation dialog
  const [deleteDialogOpen, setDeleteDialogOpen] = useState(false);
  
  // Get product data from API
  const { data: product, isLoading: isLoadingProduct, error: productError } = 
    useGetProductQuery(id as string, { skip: isNewProduct });
  
  // Get categories for dropdown
  const { data: categories, isLoading: isLoadingCategories } = useGetCategoriesQuery();
  
  // Mutations for CRUD operations
  const [createProduct, { isLoading: isCreating }] = useCreateProductMutation();
  const [updateProduct, { isLoading: isUpdating }] = useUpdateProductMutation();
  const [deleteProduct, { isLoading: isDeleting }] = useDeleteProductMutation();
  
  // Handle tab change
  const handleTabChange = (event: React.SyntheticEvent, newValue: number) => {
    setActiveTab(newValue);
  };
  
  // Handle form submission
  const handleSubmit = async (values: any) => {
    try {
      if (isNewProduct) {
        // Create new product
        const createRequest: CreateProductRequest = {
          name: values.name,
          code: values.code,
          description: values.description,
          categoryId: values.categoryId,
          price: values.price,
          status: values.status,
          imageUrl: values.imageUrl,
          attributes: values.attributes,
        };
        
        await createProduct(createRequest).unwrap();
        dispatch(showSuccessNotification('Product created successfully'));
        navigate('/catalog/products');
      } else {
        // Update existing product
        const updateRequest: UpdateProductRequest = {
          name: values.name,
          code: values.code,
          description: values.description,
          categoryId: values.categoryId,
          price: values.price,
          status: values.status,
          imageUrl: values.imageUrl,
          attributes: values.attributes,
        };
        
        await updateProduct({ id: id as string, product: updateRequest }).unwrap();
        dispatch(showSuccessNotification('Product updated successfully'));
        
        if (isViewMode) {
          // If we were in view mode, stay in view mode
          navigate(`/catalog/products/${id}?view=true`);
        }
      }
    } catch (error: any) {
      dispatch(showErrorNotification(`Failed to ${isNewProduct ? 'create' : 'update'} product: ${error.message}`));
    }
  };
  
  // Handle delete button click
  const handleDeleteClick = () => {
    setDeleteDialogOpen(true);
  };
  
  // Handle delete confirmation
  const handleDeleteConfirm = async () => {
    try {
      await deleteProduct(id as string).unwrap();
      dispatch(showSuccessNotification('Product deleted successfully'));
      navigate('/catalog/products');
    } catch (error: any) {
      dispatch(showErrorNotification(`Failed to delete product: ${error.message}`));
    } finally {
      setDeleteDialogOpen(false);
    }
  };
  
  // Handle cancel button click
  const handleCancel = () => {
    if (isNewProduct) {
      navigate('/catalog/products');
    } else {
      navigate(`/catalog/products/${id}${isViewMode ? '?view=true' : ''}`);
    }
  };
  
  // Handle edit button click (switch from view to edit mode)
  const handleEdit = () => {
    navigate(`/catalog/products/${id}`);
  };
  
  // Initial form values
  const initialValues = isNewProduct
    ? {
        name: '',
        code: '',
        description: '',
        categoryId: '',
        price: 0,
        status: 'Draft',
        imageUrl: '',
        attributes: {},
      }
    : {
        name: product?.name || '',
        code: product?.code || '',
        description: product?.description || '',
        categoryId: product?.category || '',
        price: product?.price || 0,
        status: product?.status || 'Draft',
        imageUrl: product?.imageUrl || '',
        attributes: product?.attributes || {},
      };
  
  // Show loading state
  if ((isLoadingProduct && !isNewProduct) || isLoadingCategories) {
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
          {isNewProduct ? 'Preparing new product form...' : 'Loading product details...'}
        </Typography>
      </Box>
    );
  }
  
  // Show error state
  if (productError && !isNewProduct) {
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
          Error loading product details. Please try again.
        </Alert>
        <Button
          variant="contained"
          color="primary"
          startIcon={<ArrowBackIcon />}
          onClick={() => navigate('/catalog/products')}
        >
          Back to Products
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
          onClick={() => navigate('/catalog/products')}
          sx={{ cursor: 'pointer' }}
        >
          Products
        </Link>
        <Typography color="text.primary">
          {isNewProduct ? 'New Product' : product?.name || 'Product Details'}
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
          {isNewProduct ? 'Create New Product' : isViewMode ? 'Product Details' : 'Edit Product'}
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
              onClick={() => navigate('/catalog/products')}
            >
              Back to Products
            </Button>
          )}
        </Box>
      </Box>
      
      {/* Product Form */}
      <Formik
        initialValues={initialValues}
        validationSchema={productValidationSchema}
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
                  aria-label="product tabs"
                  variant="scrollable"
                  scrollButtons="auto"
                >
                  <Tab label="Basic Information" {...a11yProps(0)} />
                  <Tab label="Attributes" {...a11yProps(1)} />
                  <Tab label="Images" {...a11yProps(2)} />
                  <Tab label="Variants" {...a11yProps(3)} />
                  <Tab label="Related Products" {...a11yProps(4)} />
                </Tabs>
              </Box>
              
              {/* Basic Information Tab */}
              <TabPanel value={activeTab} index={0}>
                <Grid container spacing={3}>
                  {/* Product Image */}
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
                          Product Image
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
                  
                  {/* Product Details */}
                  <Grid item xs={12} md={8}>
                    <Grid container spacing={2}>
                      <Grid item xs={12} sm={6}>
                        <Field
                          as={TextField}
                          name="name"
                          label="Product Name"
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
                      <Grid item xs={12} sm={6}>
                        <Field
                          as={TextField}
                          name="code"
                          label="Product Code"
                          fullWidth
                          required
                          disabled={isViewMode}
                          error={touched.code && Boolean(errors.code)}
                          helperText={touched.code && errors.code}
                          onChange={handleChange}
                          onBlur={handleBlur}
                          value={values.code}
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
                          required
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
                          required
                          error={touched.categoryId && Boolean(errors.categoryId)}
                          disabled={isViewMode}
                        >
                          <InputLabel id="category-label">Category</InputLabel>
                          <Field
                            as={Select}
                            labelId="category-label"
                            name="categoryId"
                            label="Category"
                            onChange={handleChange}
                            onBlur={handleBlur}
                            value={values.categoryId}
                          >
                            {categories?.map((category) => (
                              <MenuItem key={category.id} value={category.id}>
                                {category.name}
                              </MenuItem>
                            ))}
                          </Field>
                          {touched.categoryId && errors.categoryId && (
                            <FormHelperText>{errors.categoryId}</FormHelperText>
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
                            <MenuItem value="Draft">Draft</MenuItem>
                            <MenuItem value="Pending">Pending</MenuItem>
                            <MenuItem value="Active">Active</MenuItem>
                            <MenuItem value="Inactive">Inactive</MenuItem>
                          </Field>
                          {touched.status && errors.status && (
                            <FormHelperText>{errors.status}</FormHelperText>
                          )}
                        </FormControl>
                      </Grid>
                      <Grid item xs={12} sm={6}>
                        <Field
                          as={TextField}
                          name="price"
                          label="Price"
                          type="number"
                          fullWidth
                          required
                          disabled={isViewMode}
                          InputProps={{
                            startAdornment: <Typography sx={{ mr: 1 }}>₹</Typography>,
                          }}
                          error={touched.price && Boolean(errors.price)}
                          helperText={touched.price && errors.price}
                          onChange={handleChange}
                          onBlur={handleBlur}
                          value={values.price}
                        />
                      </Grid>
                      <Grid item xs={12} sm={6}>
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
                  Product attributes will be displayed here. This feature is under development.
                </Typography>
              </TabPanel>
              
              {/* Images Tab */}
              <TabPanel value={activeTab} index={2}>
                <Typography variant="body1" color="text.secondary" paragraph>
                  Product images will be displayed here. This feature is under development.
                </Typography>
              </TabPanel>
              
              {/* Variants Tab */}
              <TabPanel value={activeTab} index={3}>
                <Typography variant="body1" color="text.secondary" paragraph>
                  Product variants will be displayed here. This feature is under development.
                </Typography>
              </TabPanel>
              
              {/* Related Products Tab */}
              <TabPanel value={activeTab} index={4}>
                <Typography variant="body1" color="text.secondary" paragraph>
                  Related products will be displayed here. This feature is under development.
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
                        {isNewProduct ? 'Creating...' : 'Saving...'}
                      </>
                    ) : (
                      isNewProduct ? 'Create Product' : 'Save Changes'
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
          Delete Product
        </DialogTitle>
        <DialogContent>
          <DialogContentText id="delete-dialog-description">
            Are you sure you want to delete this product? This action cannot be undone.
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

export default ProductDetail;