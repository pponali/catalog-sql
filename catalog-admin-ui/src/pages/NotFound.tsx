import React from 'react';
import { useNavigate } from 'react-router-dom';
import {
  Box,
  Button,
  Typography,
  Container,
  Paper,
} from '@mui/material';
import {
  SentimentDissatisfied as SentimentDissatisfiedIcon,
  Home as HomeIcon,
  ArrowBack as ArrowBackIcon,
} from '@mui/icons-material';

/**
 * NotFound component is displayed when a user navigates to a route that doesn't exist.
 * It provides options to go back or navigate to the dashboard.
 */
const NotFound: React.FC = () => {
  const navigate = useNavigate();
  
  // Handle navigation to dashboard
  const handleGoToDashboard = () => {
    navigate('/dashboard');
  };
  
  // Handle going back
  const handleGoBack = () => {
    navigate(-1);
  };
  
  return (
    <Container maxWidth="md">
      <Box
        sx={{
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'center',
          justifyContent: 'center',
          minHeight: '100vh',
          textAlign: 'center',
          py: 4,
        }}
      >
        <Paper
          elevation={0}
          sx={{
            p: 5,
            borderRadius: 2,
            boxShadow: '0 4px 20px rgba(0, 0, 0, 0.1)',
            width: '100%',
            maxWidth: 600,
          }}
        >
          <SentimentDissatisfiedIcon
            sx={{ fontSize: 100, color: 'text.secondary', mb: 2 }}
          />
          
          <Typography variant="h2" component="h1" gutterBottom>
            404
          </Typography>
          
          <Typography variant="h4" gutterBottom>
            Page Not Found
          </Typography>
          
          <Typography variant="body1" color="text.secondary" paragraph sx={{ mb: 4 }}>
            The page you are looking for doesn't exist or has been moved.
          </Typography>
          
          <Box sx={{ display: 'flex', justifyContent: 'center', gap: 2, flexWrap: 'wrap' }}>
            <Button
              variant="contained"
              color="primary"
              startIcon={<HomeIcon />}
              onClick={handleGoToDashboard}
              size="large"
            >
              Go to Dashboard
            </Button>
            
            <Button
              variant="outlined"
              color="primary"
              startIcon={<ArrowBackIcon />}
              onClick={handleGoBack}
              size="large"
            >
              Go Back
            </Button>
          </Box>
        </Paper>
      </Box>
    </Container>
  );
};

export default NotFound;