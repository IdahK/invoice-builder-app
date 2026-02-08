# Invoice Builder Frontend

Modern React application for the Invoice Builder user interface. Built with Vite for fast development and optimized performance.

## 🎯 Purpose

The frontend provides an intuitive, responsive interface for creating and managing invoices. It connects to the backend API to deliver a seamless invoicing experience with real-time updates and professional invoice generation.

## 🏗️ Architecture

### Component Structure
```
src/
├── components/       # Reusable UI components
├── pages/          # Page-level components
├── hooks/          # Custom React hooks
├── services/       # API integration
├── utils/          # Helper functions
├── styles/         # CSS and styling
└── assets/         # Static assets
```

### Key Features
- **Responsive Design** - Works on desktop, tablet, and mobile
- **Real-time Updates** - Live data synchronization
- **Form Validation** - Client-side input validation
- **PDF Preview** - Invoice preview before download
- **State Management** - Efficient data flow
- **Error Handling** - User-friendly error messages

## 🛠️ Tech Stack

### Core Technologies
- **React 19.2.0** - UI framework with latest features
- **Vite 7.2.4** - Fast build tool and dev server
- **JavaScript ES6+** - Modern JavaScript features
- **CSS3** - Styling with responsive design

### Development Tools
- **ESLint 9.39.1** - Code quality and linting
- **Vite Plugin React** - Fast Refresh and HMR
- **TypeScript Support** - Optional type safety (planned)

### Planned Enhancements
- **TypeScript Migration** - Better type safety
- **Tailwind CSS** - Utility-first styling
- **React Router** - Client-side routing
- **State Management** - Zustand or Redux Toolkit

## 🚀 Getting Started

### Prerequisites
- Node.js 18+
- npm or yarn
- Git

### 1. Installation
```bash
npm install
```

### 2. Development Server
```bash
npm run dev
```

### 3. Build for Production
```bash
npm run build
```

### 4. Preview Production Build
```bash
npm run preview
```

### 5. Lint Code
```bash
npm run lint
```

## 🌐 Access Points

- **Development**: http://localhost:5173
- **Backend API**: http://localhost:8080/api/v1
- **Production Preview**: http://localhost:4173 (after build)

## 🎨 UI Components

### Core Components

#### Invoice Components
- `InvoiceForm` - Create/edit invoice form
- `InvoiceList` - Paginated invoice list
- `InvoicePreview` - PDF preview component
- `LineItemEditor` - Line item management

#### Customer Components
- `CustomerForm` - Customer creation/editing
- `CustomerList` - Customer directory
- `CustomerSearch` - Search and filter

#### Common Components
- `DataTable` - Reusable data table
- `SearchBar` - Search functionality
- `Pagination` - Page navigation
- `Modal` - Dialog windows
- `LoadingSpinner` - Loading states

### Styling Approach
- **Component-based CSS** - Scoped styles per component
- **Responsive Design** - Mobile-first approach
- **CSS Variables** - Consistent theming
- **Modern CSS** - Grid, Flexbox, Custom Properties

## 🔌 API Integration

### Service Layer
```javascript
// Example API service
import { apiClient } from './apiClient';

export const invoiceService = {
  getInvoices: (page = 0, size = 10) => 
    apiClient.get(`/invoices?page=${page}&size=${size}`),
  
  createInvoice: (invoiceData) => 
    apiClient.post('/invoices', invoiceData),
  
  updateInvoice: (id, invoiceData) => 
    apiClient.put(`/invoices/${id}`, invoiceData)
};
```

### Error Handling
- Global error boundary for React errors
- API error handling with user-friendly messages
- Network error detection and retry logic
- Loading states for better UX

## 📱 Responsive Design

### Breakpoints
- **Mobile**: < 768px
- **Tablet**: 768px - 1024px
- **Desktop**: > 1024px

### Mobile Optimizations
- Touch-friendly interface
- Collapsible navigation
- Optimized form layouts
- Swipe gestures for tables

## 🧪 Testing

### Current Setup
```bash
# Run linting (code quality)
npm run lint
```

### Planned Testing
- **Unit Tests** - Jest + React Testing Library
- **Component Tests** - Isolated component testing
- **E2E Tests** - Playwright or Cypress
- **Visual Regression** - Chromatic or Percy

## 🚀 Performance Optimizations

### Current Optimizations
- **Vite HMR** - Fast development refresh
- **Code Splitting** - Lazy loading components
- **Tree Shaking** - Dead code elimination
- **Asset Optimization** - Image and font optimization

### Future Optimizations
- **React.memo** - Component memoization
- **useMemo/useCallback** - Hook optimizations
- **Virtual Scrolling** - Large list performance
- **Service Workers** - Offline capabilities

## 🔧 Development Workflow

### Code Quality
```bash
# Check code quality
npm run lint

# Fix auto-fixable issues
npm run lint -- --fix
```

### Best Practices
- Use functional components with hooks
- Implement proper error boundaries
- Follow React naming conventions
- Write semantic HTML
- Optimize re-renders

### Git Workflow
```bash
# Feature branch naming
feature/invoice-form-validation
feature/customer-search-ui
fix/mobile-responsive-layout

# Commit message format
feat: add invoice form validation
fix: resolve mobile layout issues
docs: update component documentation
```

## 🎨 Design System

### Color Palette
```css
:root {
  --primary-color: #2563eb;
  --secondary-color: #64748b;
  --success-color: #16a34a;
  --warning-color: #d97706;
  --error-color: #dc2626;
  --background-color: #ffffff;
  --text-color: #1f2937;
}
```

### Typography
- **Font Family**: Inter, system-ui, sans-serif
- **Font Sizes**: Responsive scaling (rem units)
- **Line Heights**: 1.5 for readability

### Spacing
- **Base Unit**: 0.25rem (4px)
- **Scale**: 4, 8, 12, 16, 24, 32, 48, 64px

## 📱 Browser Support

### Target Browsers
- **Chrome** 90+
- **Firefox** 88+
- **Safari** 14+
- **Edge** 90+

### Progressive Enhancement
- Core functionality works without JavaScript
- Enhanced experience with modern browsers
- Graceful degradation for older browsers

## 🔒 Security Considerations

### Client-Side Security
- Input sanitization
- XSS prevention
- CSRF token handling
- Secure API communication (HTTPS)

### Data Protection
- No sensitive data in localStorage
- Secure API key management
- User session management

## 📈 Analytics & Monitoring

### Planned Features
- **Error Tracking** - Sentry or similar
- **Performance Monitoring** - Web Vitals
- **User Analytics** - Usage patterns
- **A/B Testing** - Feature flags

## 🚀 Deployment

### Build Process
```bash
# Production build
npm run build

# Preview build locally
npm run preview

# Build analysis
npm run build -- --analyze
```

### Deployment Targets
- **Static Hosting** - Vercel, Netlify, GitHub Pages
- **CDN** - CloudFront, Cloudflare
- **Docker** - Containerized deployment

## 🤝 Contributing

### Adding New Components
1. Create component in appropriate folder
2. Use consistent naming conventions
3. Add PropTypes or TypeScript
4. Include component documentation
5. Add responsive design considerations

### Code Review Checklist
- [ ] Component follows design system
- [ ] Responsive design implemented
- [ ] Error handling added
- [ ] Performance considered
- [ ] Accessibility features included
- [ ] Documentation updated

## 📚 Learning Resources

- [React Documentation](https://react.dev/)
- [Vite Documentation](https://vite.dev/)
- [MDN Web Docs](https://developer.mozilla.org/)
- [CSS Tricks](https://css-tricks.com/)
- [Web.dev](https://web.dev/)

## 🆘 Support

- **Issues**: Report via GitHub Issues
- **Documentation**: Check component comments
- **API Reference**: Backend API documentation

---

**Building a modern, accessible, and performant invoicing experience**
