import { createSlice, PayloadAction } from '@reduxjs/toolkit';
import { RootState } from '../store';

// Define breadcrumb item interface
export interface BreadcrumbItem {
  text: string;
  link?: string;
}

// Define the state interface
interface UiState {
  darkMode: boolean;
  sidebarOpen: boolean;
  language: string;
  breadcrumbs: BreadcrumbItem[];
  isMobileView: boolean;
  isLoading: boolean;
}

// Define the initial state
const initialState: UiState = {
  darkMode: false,
  sidebarOpen: true,
  language: 'en',
  breadcrumbs: [],
  isMobileView: false,
  isLoading: false,
};

// Create the UI slice
const uiSlice = createSlice({
  name: 'ui',
  initialState,
  reducers: {
    // Toggle dark mode
    toggleDarkMode: (state) => {
      state.darkMode = !state.darkMode;
    },
    
    // Set dark mode
    setDarkMode: (state, action: PayloadAction<boolean>) => {
      state.darkMode = action.payload;
    },
    
    // Toggle sidebar
    toggleSidebar: (state) => {
      state.sidebarOpen = !state.sidebarOpen;
    },
    
    // Set sidebar open state
    setSidebarOpen: (state, action: PayloadAction<boolean>) => {
      state.sidebarOpen = action.payload;
    },
    
    // Set language
    setLanguage: (state, action: PayloadAction<string>) => {
      state.language = action.payload;
    },
    
    // Set breadcrumbs
    setBreadcrumbs: (state, action: PayloadAction<BreadcrumbItem[]>) => {
      state.breadcrumbs = action.payload;
    },
    
    // Add breadcrumb
    addBreadcrumb: (state, action: PayloadAction<BreadcrumbItem>) => {
      state.breadcrumbs.push(action.payload);
    },
    
    // Remove last breadcrumb
    removeLastBreadcrumb: (state) => {
      state.breadcrumbs.pop();
    },
    
    // Clear breadcrumbs
    clearBreadcrumbs: (state) => {
      state.breadcrumbs = [];
    },
    
    // Set mobile view
    setMobileView: (state, action: PayloadAction<boolean>) => {
      state.isMobileView = action.payload;
      
      // If switching to mobile view, close the sidebar
      if (action.payload) {
        state.sidebarOpen = false;
      }
    },
    
    // Set loading state
    setLoading: (state, action: PayloadAction<boolean>) => {
      state.isLoading = action.payload;
    },
  },
});

// Export actions
export const {
  toggleDarkMode,
  setDarkMode,
  toggleSidebar,
  setSidebarOpen,
  setLanguage,
  setBreadcrumbs,
  addBreadcrumb,
  removeLastBreadcrumb,
  clearBreadcrumbs,
  setMobileView,
  setLoading,
} = uiSlice.actions;

// Export selectors
export const selectDarkMode = (state: RootState) => state.ui.darkMode;
export const selectSidebarOpen = (state: RootState) => state.ui.sidebarOpen;
export const selectLanguage = (state: RootState) => state.ui.language;
export const selectBreadcrumbs = (state: RootState) => state.ui.breadcrumbs;
export const selectIsMobileView = (state: RootState) => state.ui.isMobileView;
export const selectIsLoading = (state: RootState) => state.ui.isLoading;

// Export reducer
export default uiSlice.reducer;