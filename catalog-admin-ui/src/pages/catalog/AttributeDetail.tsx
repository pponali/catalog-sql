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
  CardContent,
  Tooltip,
  Alert,
  Switch,
  FormControlLabel,
  List,
  ListItem,
  ListItemText,
  ListItemSecondaryAction,
} from '@mui/material';
import {
  Save as SaveIcon,
  Cancel as CancelIcon,
  Delete as DeleteIcon,
  ArrowBack as ArrowBackIcon,
  Add as AddIcon,
  Edit as EditIcon,
  Visibility as VisibilityIcon,
  Close as CloseIcon,
} from '@mui/icons-material';
import { Formik, Form, Field, FieldArray, ErrorMessage } from 'formik';
import * as Yup from 'yup';
import { 
  useGetAttributeQuery, 
  useCreateAttributeMutation, 
  useUpdateAttributeMutation, 
  useDeleteAttributeMutation,
  Attribute,
  CreateAttributeRequest,
  UpdateAttributeRequest,
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
      id={`attribute-tabpanel-${index}`}
      aria-labelledby={`attribute-tab-${index}`}
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
    id: `attribute-tab-${index}`,
    'aria-controls': `attribute-tabpanel-${index}`,
  };
};

// Validation schema for attribute form
const attributeValidationSchema = Yup.object().shape({
  name: Yup.string().required('Name is required').max(100, 'Name must be at most 100 characters'),
  code: Yup.string().required('Code is required').max(50, 'Code must be at most 50 characters'),
  description: Yup.string().max(500, 'Description must be at most 500 characters'),
  type: Yup.string().required('Type is required'),
  required: Yup.boolean().default(false),
  options: Yup.array().of(
    Yup.object().shape({
      value: Yup.string().required('Option value is required'),
      label: Yup.string().required('Option label is required'),
    })
  ).test('options-required', 'At least one option is required for select/multiselect types', 
    function(options, context) {
      return context.parent.type !== 'select' && context.parent.type !== 'multiselect' || (options && options.length > 0);
    }),
});

/**
 * AttributeDetail component displays and allows editing of an attribute's details.
 * It handles both creating new attributes and editing existing ones.
 */
const AttributeDetail: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const location = useLocation();
  const dispatch = useDispatch();
  
  // Check if we're in view-only mode
  const isViewMode = new URLSearchParams(location.search).get('view') === 'true';
  
  // Check if we're creating a new attribute
  const isNewAttribute = id === 'new';
  
  // State for the active tab
  const [activeTab, setActiveTab] = useState(0);
  
  // State for delete confirmation dialog
  const [deleteDialogOpen, setDeleteDialogOpen] = useState(false);
  
  // State for add option dialog
  const [addOptionDialogOpen, setAddOptionDialogOpen] = useState(false);
  const [newOptionValue, setNewOptionValue] = useState('');
  const [newOptionLabel, setNewOptionLabel] = useState('');
  
  // Get attribute data from API
  const { data: attribute, isLoading: isLoadingAttribute, error: attributeError } = 
    useGetAttributeQuery(id as string, { skip: isNewAttribute });
  
  // Mutations for CRUD operations
  const [createAttribute, { isLoading: isCreating }] = useCreateAttributeMutation();
  const [updateAttribute, { isLoading: isUpdating }] = useUpdateAttributeMutation();
  const [deleteAttribute, { isLoading: isDeleting }] = useDeleteAttributeMutation();
  
  // Handle tab change
  const handleTabChange = (event: React.SyntheticEvent, newValue: number) => {
    setActiveTab(newValue);
  };
  
  // Handle form submission
  const handleSubmit = async (values: any) => {
    try {
      if (isNewAttribute) {
        // Create new attribute
        const createRequest: CreateAttributeRequest = {
          name: values.name,
          code: values.code,
          description: values.description || '',
          type: values.type,
          required: values.required,
          options: values.type === 'select' || values.type === 'multiselect' ? values.options : [],
        };
        
        await createAttribute(createRequest).unwrap();
        dispatch(showSuccessNotification('Attribute created successfully'));
        navigate('/catalog/attributes');
      } else {
        // Update existing attribute
        const updateRequest: UpdateAttributeRequest = {
          name: values.name,
          description: values.description || '',
          type: values.type,
          required: values.required,
          options: values.type === 'select' || values.type === 'multiselect' ? values.options : [],
        };
        
        await updateAttribute({ id: id as string, attribute: updateRequest }).unwrap();
        dispatch(showSuccessNotification('Attribute updated successfully'));
        
        if (isViewMode) {
          // If we were in view mode, stay in view mode
          navigate(`/catalog/attributes/${id}?view=true`);
        }
      }
    } catch (error: any) {
      dispatch(showErrorNotification(`Failed to ${isNewAttribute ? 'create' : 'update'} attribute: ${error.message}`));
    }
  };
  
  // Handle delete button click
  const handleDeleteClick = () => {
    setDeleteDialogOpen(true);
  };
  
  // Handle delete confirmation
  const handleDeleteConfirm = async () => {
    try {
      await deleteAttribute(id as string).unwrap();
      dispatch(showSuccessNotification('Attribute deleted successfully'));
      navigate('/catalog/attributes');
    } catch (error: any) {
      dispatch(showErrorNotification(`Failed to delete attribute: ${error.message}`));
    } finally {
      setDeleteDialogOpen(false);
    }
  };
  
  // Handle cancel button click
  const handleCancel = () => {
    if (isNewAttribute) {
      navigate('/catalog/attributes');
    } else {
      navigate(`/catalog/attributes/${id}${isViewMode ? '?view=true' : ''}`);
    }
  };
  
  // Handle edit button click (switch from view to edit mode)
  const handleEdit = () => {
    navigate(`/catalog/attributes/${id}`);
  };
  
  // Handle add option dialog open
  const handleAddOptionDialogOpen = () => {
    setNewOptionValue('');
    setNewOptionLabel('');
    setAddOptionDialogOpen(true);
  };
  
  // Handle add option dialog close
  const handleAddOptionDialogClose = () => {
    setAddOptionDialogOpen(false);
  };
  
  // Initial form values
  const initialValues = isNewAttribute
    ? {
        name: '',
        code: '',
        description: '',
        type: 'text',
        required: false,
        options: [],
      }
    : {
        name: attribute?.name || '',
        code: attribute?.code || '',
        description: attribute?.description || '',
        type: attribute?.type || 'text',
        required: attribute?.required || false,
        options: attribute?.options || [],
      };
  
  // Show loading state
  if ((isLoadingAttribute && !isNewAttribute)) {
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
          {isNewAttribute ? 'Preparing new attribute form...' : 'Loading attribute details...'}
        </Typography>
      </Box>
    );
  }
  
  // Show error state
  if (attributeError && !isNewAttribute) {
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
          Error loading attribute details. Please try again.
        </Alert>
        <Button
          variant="contained"
          color="primary"
          startIcon={<ArrowBackIcon />}
          onClick={() => navigate('/catalog/attributes')}
        >
          Back to Attributes
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
          onClick={() => navigate('/catalog/attributes')}
          sx={{ cursor: 'pointer' }}
        >
          Attributes
        </Link>
        <Typography color="text.primary">
          {isNewAttribute ? 'New Attribute' : attribute?.name || 'Attribute Details'}
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
          {isNewAttribute ? 'Create New Attribute' : isViewMode ? 'Attribute Details' : 'Edit Attribute'}
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
              onClick={() => navigate('/catalog/attributes')}
            >
              Back to Attributes
            </Button>
          )}
        </Box>
      </Box>
      
      {/* Attribute Form */}
      <Formik
        initialValues={initialValues}
        validationSchema={attributeValidationSchema}
        onSubmit={handleSubmit}
        enableReinitialize
      >
        {({ values, errors, touched, handleChange, handleBlur, isValid, dirty, setFieldValue }) => (
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
                  aria-label="attribute tabs"
                  variant="scrollable"
                  scrollButtons="auto"
                >
                  <Tab label="Basic Information" {...a11yProps(0)} />
                  <Tab 
                    label="Options" 
                    {...a11yProps(1)} 
                    disabled={values.type !== 'select' && values.type !== 'multiselect'}
                  />
                  <Tab label="Usage" {...a11yProps(2)} />
                </Tabs>
              </Box>
              
              {/* Basic Information Tab */}
              <TabPanel value={activeTab} index={0}>
                <Grid container spacing={3}>
                  <Grid item xs={12} sm={6}>
                    <Field
                      as={TextField}
                      name="name"
                      label="Attribute Name"
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
                      label="Attribute Code"
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
                      error={touched.type && Boolean(errors.type)}
                      disabled={isViewMode || !isNewAttribute} // Type cannot be changed after creation
                    >
                      <InputLabel id="type-label">Attribute Type</InputLabel>
                      <Field
                        as={Select}
                        labelId="type-label"
                        name="type"
                        label="Attribute Type"
                        onChange={(e: React.ChangeEvent<HTMLInputElement>) => {
                          handleChange(e);
                          // If changing from select/multiselect to another type, clear options
                          if (e.target.value !== 'select' && e.target.value !== 'multiselect') {
                            setFieldValue('options', []);
                          }
                        }}
                        onBlur={handleBlur}
                        value={values.type}
                      >
                        <MenuItem value="text">Text</MenuItem>
                        <MenuItem value="number">Number</MenuItem>
                        <MenuItem value="boolean">Boolean</MenuItem>
                        <MenuItem value="date">Date</MenuItem>
                        <MenuItem value="select">Select (Single)</MenuItem>
                        <MenuItem value="multiselect">Select (Multiple)</MenuItem>
                      </Field>
                      {touched.type && errors.type && (
                        <FormHelperText>{errors.type}</FormHelperText>
                      )}
                    </FormControl>
                  </Grid>
                  <Grid item xs={12} sm={6}>
                    <FormControlLabel
                      control={
                        <Field
                          as={Switch}
                          name="required"
                          color="primary"
                          disabled={isViewMode}
                          checked={values.required}
                          onChange={handleChange}
                        />
                      }
                      label="Required"
                    />
                  </Grid>
                </Grid>
              </TabPanel>
              
              {/* Options Tab */}
              <TabPanel value={activeTab} index={1}>
                {(values.type === 'select' || values.type === 'multiselect') ? (
                  <Box>
                    <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 2 }}>
                      <Typography variant="h6">Attribute Options</Typography>
                      {!isViewMode && (
                        <Button
                          variant="contained"
                          color="primary"
                          startIcon={<AddIcon />}
                          onClick={handleAddOptionDialogOpen}
                        >
                          Add Option
                        </Button>
                      )}
                    </Box>
                    
                    <FieldArray name="options">
                      {({ remove, push }) => (
                        <>
                          {values.options.length > 0 ? (
                            <List>
                              {values.options.map((option: any, index: number) => (
                                <ListItem
                                  key={index}
                                  divider
                                  secondaryAction={
                                    !isViewMode && (
                                      <IconButton
                                        edge="end"
                                        aria-label="delete"
                                        onClick={() => remove(index)}
                                      >
                                        <DeleteIcon />
                                      </IconButton>
                                    )
                                  }
                                >
                                  <ListItemText
                                    primary={option.label}
                                    secondary={`Value: ${option.value}`}
                                  />
                                </ListItem>
                              ))}
                            </List>
                          ) : (
                            <Box
                              sx={{
                                display: 'flex',
                                flexDirection: 'column',
                                alignItems: 'center',
                                justifyContent: 'center',
                                height: 200,
                                bgcolor: 'action.hover',
                                borderRadius: 1,
                              }}
                            >
                              <Typography variant="body1" color="text.secondary" gutterBottom>
                                No options defined
                              </Typography>
                              {!isViewMode && (
                                <Button
                                  variant="outlined"
                                  color="primary"
                                  startIcon={<AddIcon />}
                                  onClick={handleAddOptionDialogOpen}
                                >
                                  Add Option
                                </Button>
                              )}
                            </Box>
                          )}
                          
                          {/* Add Option Dialog */}
                          <Dialog
                            open={addOptionDialogOpen}
                            onClose={handleAddOptionDialogClose}
                            aria-labelledby="add-option-dialog-title"
                          >
                            <DialogTitle id="add-option-dialog-title">
                              Add Option
                            </DialogTitle>
                            <DialogContent>
                              <Grid container spacing={2} sx={{ mt: 1 }}>
                                <Grid item xs={12}>
                                  <TextField
                                    label="Option Value"
                                    fullWidth
                                    value={newOptionValue}
                                    onChange={(e) => setNewOptionValue(e.target.value)}
                                    required
                                  />
                                </Grid>
                                <Grid item xs={12}>
                                  <TextField
                                    label="Option Label"
                                    fullWidth
                                    value={newOptionLabel}
                                    onChange={(e) => setNewOptionLabel(e.target.value)}
                                    required
                                  />
                                </Grid>
                              </Grid>
                            </DialogContent>
                            <DialogActions>
                              <Button onClick={handleAddOptionDialogClose} color="inherit">
                                Cancel
                              </Button>
                              <Button
                                onClick={() => {
                                  if (newOptionValue && newOptionLabel) {
                                    push({ value: newOptionValue, label: newOptionLabel });
                                    handleAddOptionDialogClose();
                                  }
                                }}
                                color="primary"
                                disabled={!newOptionValue || !newOptionLabel}
                              >
                                Add
                              </Button>
                            </DialogActions>
                          </Dialog>
                        </>
                      )}
                    </FieldArray>
                    
                    {touched.options && errors.options && typeof errors.options === 'string' && (
                      <Alert severity="error" sx={{ mt: 2 }}>
                        {errors.options}
                      </Alert>
                    )}
                  </Box>
                ) : (
                  <Box
                    sx={{
                      display: 'flex',
                      flexDirection: 'column',
                      alignItems: 'center',
                      justifyContent: 'center',
                      height: 200,
                    }}
                  >
                    <Typography variant="body1" color="text.secondary">
                      Options are only available for Select and Multi-select attribute types.
                    </Typography>
                  </Box>
                )}
              </TabPanel>
              
              {/* Usage Tab */}
              <TabPanel value={activeTab} index={2}>
                <Typography variant="body1" color="text.secondary" paragraph>
                  This tab will show where this attribute is being used. This feature is under development.
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
                        {isNewAttribute ? 'Creating...' : 'Saving...'}
                      </>
                    ) : (
                      isNewAttribute ? 'Create Attribute' : 'Save Changes'
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
          Delete Attribute
        </DialogTitle>
        <DialogContent>
          <DialogContentText id="delete-dialog-description">
            Are you sure you want to delete this attribute? This action cannot be undone.
            <Typography variant="body2" color="error" sx={{ mt: 2 }}>
              Warning: Deleting this attribute may affect products using it.
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

export default AttributeDetail;