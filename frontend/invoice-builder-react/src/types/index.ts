export type InvoiceStatus = 'draft' | 'sent' | 'paid' | 'overdue' | 'cancelled';

export interface LineItem {
  id: string;
  description: string;
  quantity: number;
  unitPrice: number;
  total: number;
}

export interface Customer {
  id: string;
  name: string;
  contactPerson: string;
  email: string;
  phone: string;
  address: string;
  createdAt: Date;
  updatedAt: Date;
}

export interface Sender {
  id: string;
  companyName: string;
  contactPerson: string;
  email: string;
  phone: string;
  address: string;
  bankDetails?: string;
  createdAt: Date;
  updatedAt: Date;
}

export interface Invoice {
  id: string;
  invoiceNumber: string;
  currency: string;
  invoiceDate: Date;
  dueDate: Date;
  customerId: string;
  senderId: string;
  taxRate: number;
  notes: string;
  lineItems: LineItem[];
  subtotal: number;
  taxAmount: number;
  total: number;
  status: InvoiceStatus;
  createdAt: Date;
  updatedAt: Date;
}

export interface PaginatedResponse<T> {
  data: T[];
  total: number;
  page: number;
  pageSize: number;
  totalPages: number;
}

export interface AppConfig {
  pdfGenerationMode: 'client' | 'server';
  darkMode: boolean;
}
