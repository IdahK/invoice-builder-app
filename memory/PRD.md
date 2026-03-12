# Invoice Builder React Application - PRD

## Project Overview
A professional invoice builder frontend application built with React, TypeScript, and TailwindCSS. The application allows users to create, manage, and track invoices with full CRUD operations for invoices, customers, and senders.

## Original Problem Statement
Create an invoice builder frontend using React based on the UI images in the requirements directory. Create the React frontend in the frontend/invoice-builder-react folder within the repo. Use React, TypeScript and TailwindCSS.

### User Requirements
- Mock API with in-memory data (simulated backend)
- Client-side and server-side PDF generation (configurable)
- Search/filter functionality for tables
- Dark mode toggle
- Invoice status tracking (Draft, Sent, Paid, Overdue, Cancelled)

## Technology Stack
- **Frontend Framework**: React 19 with TypeScript
- **Build Tool**: Vite
- **Styling**: TailwindCSS v4
- **State Management**: Zustand (for app config)
- **Routing**: React Router DOM
- **PDF Generation**: jsPDF
- **Icons**: Lucide React
- **ID Generation**: UUID

## Core Features Implemented

### 1. Invoices Module
- [x] List paged invoices in a table view
- [x] Create new invoices with line items
- [x] View, Edit, Delete (with confirmation) invoices
- [x] Download PDF (client/server-side modes)
- [x] Status tracking (Draft, Sent, Paid, Overdue, Cancelled)
- [x] Status filtering dropdown
- [x] Search functionality

### 2. Customers Module
- [x] List paged customers in a table view
- [x] Create new customers via modal
- [x] View, Edit, Delete (with confirmation) customers
- [x] Search functionality

### 3. Senders Module
- [x] List paged senders in a table view  
- [x] Create new senders via modal
- [x] View, Edit, Delete (with confirmation) senders
- [x] Bank details field
- [x] Search functionality

### 4. New Invoice Form
- [x] Auto-generated invoice number (INV-YYYY-XXX)
- [x] Invoice date and due date (30 days default)
- [x] Customer and sender selection dropdowns
- [x] Currency selection (USD, EUR, GBP, CAD, AUD)
- [x] Tax rate input with real-time calculation
- [x] Notes field
- [x] Status selection
- [x] Line items with add/remove functionality
- [x] Real-time summary calculations (subtotal, tax, total)

### 5. PDF Generation
- [x] Client-side PDF generation using jsPDF
- [x] Server-side PDF generation (simulated with watermark)
- [x] Configurable via settings dropdown
- [x] Professional invoice layout with colors

### 6. UI/UX Features
- [x] Dark mode toggle (toggles class, styling needs TailwindCSS v4 config)
- [x] Responsive design
- [x] Status badges with color coding
- [x] Modal dialogs for CRUD operations
- [x] Confirmation dialogs for delete actions
- [x] Loading spinners
- [x] Empty states
- [x] Pagination

## File Structure
```
/app/frontend/invoice-builder-react/
├── src/
│   ├── components/
│   │   ├── Layout.tsx
│   │   └── ui/
│   │       ├── Button.tsx
│   │       ├── Input.tsx
│   │       ├── Select.tsx
│   │       ├── TextArea.tsx
│   │       ├── Modal.tsx
│   │       ├── ConfirmDialog.tsx
│   │       ├── SearchInput.tsx
│   │       ├── StatusBadge.tsx
│   │       ├── Pagination.tsx
│   │       ├── LoadingSpinner.tsx
│   │       └── EmptyState.tsx
│   ├── pages/
│   │   ├── InvoicesPage.tsx
│   │   ├── CustomersPage.tsx
│   │   ├── SendersPage.tsx
│   │   ├── NewInvoicePage.tsx
│   │   ├── EditInvoicePage.tsx
│   │   └── ViewInvoicePage.tsx
│   ├── services/
│   │   ├── mockApi.ts (mock CRUD services)
│   │   └── pdfService.ts (PDF generation)
│   ├── store/
│   │   └── appStore.ts (Zustand store)
│   ├── types/
│   │   └── index.ts (TypeScript interfaces)
│   ├── App.tsx
│   ├── main.tsx
│   └── index.css
├── tailwind.config.js
├── tsconfig.json
└── vite.config.ts
```

## Test Status
- **Frontend Tests**: 100% passing
- **All CRUD operations**: Working
- **PDF Generation**: Working
- **Search/Filter**: Working
- **Navigation**: Working
- **Form Calculations**: Working

## Known Issues / Backlog

### P0 - Critical
- None

### P1 - High Priority
- [ ] Dark mode styling - TailwindCSS v4 custom variant configuration needs refinement for full visual dark mode support

### P2 - Medium Priority
- [ ] Backend integration - Replace mock API with real backend endpoints
- [ ] User authentication
- [ ] Invoice preview before download
- [ ] Email invoice feature
- [ ] Recurring invoices

### P3 - Low Priority / Enhancements
- [ ] Export invoices to CSV/Excel
- [ ] Invoice templates
- [ ] Multi-language support
- [ ] Print functionality
- [ ] Drag-and-drop line item reordering
- [ ] Invoice duplication

## Next Action Items
1. Fix dark mode styling with TailwindCSS v4 proper configuration
2. Connect to real backend API when available
3. Add form validation error messages
4. Implement invoice preview modal
5. Add email sending functionality

## Last Updated
March 12, 2026
