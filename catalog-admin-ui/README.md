# Tata Commerce Catalog Admin UI

A comprehensive admin interface for managing the unified commerce catalog system across Tata Group's various commerce platforms including Tata Cliq, Tata 1mg, Bigbasket, Tata Croma, and others.

## Overview

This application serves as a centralized platform for catalog management, providing tools for managing products, categories, attributes, and more across all Tata Group commerce platforms. It's designed to be intuitive, efficient, and scalable to handle the diverse needs of different product categories.

## Features

- **Unified Catalog Management**: Manage products across all Tata Group commerce platforms
- **Product Management**: Create, edit, view, and delete products with rich media support
- **Category Management**: Hierarchical category structure with inheritance capabilities
- **Attribute Management**: Flexible attribute system with various data types and validation
- **Role-Based Access Control**: Different permission levels for various user roles
- **Responsive Design**: Works seamlessly across desktop and mobile devices
- **Dark/Light Mode**: Support for both dark and light themes
- **Advanced Filtering & Sorting**: Powerful tools to find and organize catalog data
- **Bulk Operations**: Efficiently manage multiple items at once

## Tech Stack

- **Frontend Framework**: React with TypeScript
- **UI Component Library**: Material-UI (MUI)
- **State Management**: Redux Toolkit with RTK Query
- **Routing**: React Router
- **Form Handling**: Formik with Yup validation
- **API Integration**: RESTful API consumption with RTK Query
- **Styling**: CSS-in-JS with MUI's styling system
- **Containerization**: Docker and Docker Compose

## Project Structure

```
catalog-admin-ui/
├── public/                  # Static files
├── src/
│   ├── assets/              # Images, styles
│   ├── components/          # Reusable components
│   │   ├── common/          # Common components
│   │   ├── layout/          # Layout components
│   │   └── modules/         # Module-specific components
│   ├── config/              # Configuration files
│   ├── hooks/               # Custom React hooks
│   ├── pages/               # Page components
│   │   ├── auth/            # Authentication pages
│   │   ├── catalog/         # Catalog management pages
│   │   ├── price/           # Price management pages
│   │   ├── promotion/       # Promotion management pages
│   │   ├── user/            # User management pages
│   │   └── reports/         # Reporting pages
│   ├── services/            # API services
│   ├── store/               # Redux store
│   │   └── slices/          # Redux slices
│   ├── types/               # TypeScript type definitions
│   └── utils/               # Utility functions
├── docker-compose.yml       # Docker Compose configuration
├── Dockerfile               # Docker configuration
└── nginx/                   # Nginx configuration
```

## Getting Started

### Prerequisites

- Node.js (v14 or later)
- npm or yarn
- Docker and Docker Compose (for containerized deployment)

### Development Setup

1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd catalog-admin-ui
   ```

2. Install dependencies:
   ```bash
   npm install
   # or
   yarn install
   ```

3. Create a `.env` file in the root directory with the following content:
   ```
   REACT_APP_API_URL=http://localhost:8080/api
   SKIP_PREFLIGHT_CHECK=true
   REACT_APP_THEME_STORAGE_KEY=tata_commerce_theme
   ```

4. Start the development server:
   ```bash
   npm start
   # or
   yarn start
   ```

5. The application will be available at http://localhost:3000

### Production Deployment

#### Using Docker

1. Build and run the Docker container:
   ```bash
   docker-compose up -d
   ```

2. The application will be available at http://localhost:80

## API Integration

The application integrates with the following backend services:

- **Catalog Service**: For product, category, and attribute management
- **Price Service**: For price management
- **Promotion Service**: For promotion management
- **User Service**: For user and merchant management

All API interactions are handled through RTK Query, which provides automatic caching, refetching, and optimistic updates.

## Authentication

The application uses JWT-based authentication with token refresh capabilities. Protected routes require authentication, and the application will automatically redirect to the login page if the user is not authenticated.

## Contributing

1. Follow the coding standards and best practices
2. Write clear, concise commit messages
3. Include appropriate tests for new features
4. Document any new features or changes

## License

Proprietary - All rights reserved by Tata Group.

## Contact

For any questions or support, please contact the development team at [email@example.com](mailto:email@example.com).