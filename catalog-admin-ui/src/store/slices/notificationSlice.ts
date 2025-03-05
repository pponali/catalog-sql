import { createSlice, PayloadAction } from '@reduxjs/toolkit';
import { v4 as uuidv4 } from 'uuid';
import { RootState } from '../store';

// Define notification type
export type NotificationType = 'success' | 'error' | 'warning' | 'info';

// Define notification interface
export interface Notification {
  id: string;
  message: string;
  type: NotificationType;
  title?: string;
  autoHideDuration?: number;
  createdAt: number;
}

// Define the state interface
interface NotificationState {
  notifications: Notification[];
}

// Define the initial state
const initialState: NotificationState = {
  notifications: [],
};

// Create the notification slice
const notificationSlice = createSlice({
  name: 'notification',
  initialState,
  reducers: {
    // Add a notification
    addNotification: (state, action: PayloadAction<Omit<Notification, 'id' | 'createdAt'>>) => {
      const notification: Notification = {
        id: uuidv4(),
        createdAt: Date.now(),
        ...action.payload,
      };
      
      state.notifications.push(notification);
    },
    
    // Remove a notification by ID
    removeNotification: (state, action: PayloadAction<string>) => {
      state.notifications = state.notifications.filter(
        (notification) => notification.id !== action.payload
      );
    },
    
    // Clear all notifications
    clearNotifications: (state) => {
      state.notifications = [];
    },
  },
});

// Export actions
export const {
  addNotification,
  removeNotification,
  clearNotifications,
} = notificationSlice.actions;

// Export selectors
export const selectNotifications = (state: RootState) => state.notification.notifications;

// Helper functions to create specific notification types
export const showSuccessNotification = (message: string, title?: string, autoHideDuration = 5000) => {
  return addNotification({
    message,
    title,
    type: 'success',
    autoHideDuration,
  });
};

export const showErrorNotification = (message: string, title?: string, autoHideDuration = 8000) => {
  return addNotification({
    message,
    title,
    type: 'error',
    autoHideDuration,
  });
};

export const showWarningNotification = (message: string, title?: string, autoHideDuration = 6000) => {
  return addNotification({
    message,
    title,
    type: 'warning',
    autoHideDuration,
  });
};

export const showInfoNotification = (message: string, title?: string, autoHideDuration = 4000) => {
  return addNotification({
    message,
    title,
    type: 'info',
    autoHideDuration,
  });
};

// Export reducer
export default notificationSlice.reducer;