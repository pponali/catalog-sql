import React, { useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { Snackbar, Alert, AlertTitle, Stack } from '@mui/material';
import { 
  selectNotifications, 
  removeNotification, 
  Notification 
} from '../../store/slices/notificationSlice';

/**
 * NotificationCenter component displays notifications as snackbars.
 * It automatically removes notifications after their autoHideDuration.
 */
const NotificationCenter: React.FC = () => {
  const dispatch = useDispatch();
  const notifications = useSelector(selectNotifications);
  
  // Handle closing a notification
  const handleClose = (id: string) => {
    dispatch(removeNotification(id));
  };
  
  // Auto-close notifications after their duration
  useEffect(() => {
    if (notifications.length > 0) {
      const notification = notifications[0];
      
      if (notification.autoHideDuration) {
        const timer = setTimeout(() => {
          dispatch(removeNotification(notification.id));
        }, notification.autoHideDuration);
        
        return () => clearTimeout(timer);
      }
    }
  }, [notifications, dispatch]);
  
  // If there are no notifications, don't render anything
  if (notifications.length === 0) {
    return null;
  }
  
  return (
    <Stack spacing={2} sx={{ position: 'fixed', bottom: 24, right: 24, zIndex: 2000, maxWidth: '100%', width: 400 }}>
      {notifications.map((notification: Notification) => (
        <Snackbar
          key={notification.id}
          open={true}
          anchorOrigin={{ vertical: 'bottom', horizontal: 'right' }}
          sx={{ position: 'static', mb: 1 }}
        >
          <Alert
            severity={notification.type}
            variant="filled"
            onClose={() => handleClose(notification.id)}
            sx={{
              width: '100%',
              boxShadow: 3,
              '& .MuiAlert-message': {
                width: '100%',
              },
            }}
          >
            {notification.title && (
              <AlertTitle>{notification.title}</AlertTitle>
            )}
            {notification.message}
          </Alert>
        </Snackbar>
      ))}
    </Stack>
  );
};

export default NotificationCenter;