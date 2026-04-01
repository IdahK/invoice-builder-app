import { v4 as uuidv4 } from 'uuid';
import type { Customer, Sender, Invoice, LineItem, PaginatedResponse, InvoiceStatus } from '../types';

// Mock data storage
let customers: Customer[] = [
  {
    id: uuidv4(),
    name: 'ACME Corp',
    contactPerson: 'John Doe',
    email: 'john.doe@acme.com',
    phone: '+1 555-0100',
    address: '123 Business Ave, New York, NY 10001',
    createdAt: new Date('2025-01-15'),
    updatedAt: new Date('2025-01-15'),
  },
  {
    id: uuidv4(),
    name: 'Tech Solutions Inc',
    contactPerson: 'Jane Smith',
    email: 'jane@techsolutions.com',
    phone: '+1 555-0200',
    address: '456 Innovation Blvd, San Francisco, CA 94102',
    createdAt: new Date('2025-02-01'),
    updatedAt: new Date('2025-02-01'),
  },
  {
    id: uuidv4(),
    name: 'Global Services Ltd',
    contactPerson: 'Mike Johnson',
    email: 'mike@globalservices.com',
    phone: '+1 555-0300',
    address: '789 Corporate Dr, Chicago, IL 60601',
    createdAt: new Date('2025-02-15'),
    updatedAt: new Date('2025-02-15'),
  },
];

let senders: Sender[] = [
  {
    id: uuidv4(),
    companyName: 'My Company LLC',
    contactPerson: 'Alice Smith',
    email: 'alice@mycompany.com',
    phone: '+1 555-1000',
    address: '100 Main St, Boston, MA 02101',
    bankDetails: 'Bank of America, Account: ****1234',
    createdAt: new Date('2025-01-01'),
    updatedAt: new Date('2025-01-01'),
  },
  {
    id: uuidv4(),
    companyName: 'Freelance Services',
    contactPerson: 'Bob Wilson',
    email: 'bob@freelance.com',
    phone: '+1 555-2000',
    address: '200 Oak Lane, Seattle, WA 98101',
    bankDetails: 'Chase Bank, Account: ****5678',
    createdAt: new Date('2025-01-10'),
    updatedAt: new Date('2025-01-10'),
  },
];

let invoices: Invoice[] = [
  {
    id: uuidv4(),
    invoiceNumber: 'INV-2025-001',
    currency: 'USD',
    invoiceDate: new Date('2025-01-20'),
    dueDate: new Date('2025-02-20'),
    customerId: customers[0].id,
    senderId: senders[0].id,
    taxRate: 10,
    notes: 'Thank you for your business!',
    lineItems: [
      { id: uuidv4(), description: 'Web Development', quantity: 40, unitPrice: 75, total: 3000 },
      { id: uuidv4(), description: 'UI/UX Design', quantity: 20, unitPrice: 65, total: 1300 },
    ],
    subtotal: 4300,
    taxAmount: 430,
    total: 4730,
    status: 'paid',
    createdAt: new Date('2025-01-20'),
    updatedAt: new Date('2025-02-15'),
  },
  {
    id: uuidv4(),
    invoiceNumber: 'INV-2025-002',
    currency: 'USD',
    invoiceDate: new Date('2025-02-01'),
    dueDate: new Date('2025-03-01'),
    customerId: customers[1].id,
    senderId: senders[0].id,
    taxRate: 8,
    notes: 'Net 30 payment terms apply.',
    lineItems: [
      { id: uuidv4(), description: 'Consulting Services', quantity: 8, unitPrice: 150, total: 1200 },
    ],
    subtotal: 1200,
    taxAmount: 96,
    total: 1296,
    status: 'sent',
    createdAt: new Date('2025-02-01'),
    updatedAt: new Date('2025-02-01'),
  },
  {
    id: uuidv4(),
    invoiceNumber: 'INV-2025-003',
    currency: 'EUR',
    invoiceDate: new Date('2025-02-10'),
    dueDate: new Date('2025-02-25'),
    customerId: customers[2].id,
    senderId: senders[1].id,
    taxRate: 0,
    notes: '',
    lineItems: [
      { id: uuidv4(), description: '.NET Book', quantity: 1, unitPrice: 29.99, total: 29.99 },
      { id: uuidv4(), description: '.NET Course', quantity: 1, unitPrice: 89.99, total: 89.99 },
    ],
    subtotal: 119.98,
    taxAmount: 0,
    total: 119.98,
    status: 'overdue',
    createdAt: new Date('2025-02-10'),
    updatedAt: new Date('2025-02-10'),
  },
  {
    id: uuidv4(),
    invoiceNumber: 'INV-2025-004',
    currency: 'USD',
    invoiceDate: new Date('2025-03-01'),
    dueDate: new Date('2025-04-01'),
    customerId: customers[0].id,
    senderId: senders[0].id,
    taxRate: 5,
    notes: 'Quarterly maintenance fee',
    lineItems: [
      { id: uuidv4(), description: 'Server Maintenance', quantity: 3, unitPrice: 200, total: 600 },
      { id: uuidv4(), description: 'Security Updates', quantity: 1, unitPrice: 350, total: 350 },
    ],
    subtotal: 950,
    taxAmount: 47.5,
    total: 997.5,
    status: 'draft',
    createdAt: new Date('2025-03-01'),
    updatedAt: new Date('2025-03-01'),
  },
];

// Simulated delay
const delay = (ms: number) => new Promise(resolve => setTimeout(resolve, ms));

// Customer Service
export const customerService = {
  async getAll(page = 1, pageSize = 10, search = ''): Promise<PaginatedResponse<Customer>> {
    await delay(200);
    let filtered = customers;
    if (search) {
      const searchLower = search.toLowerCase();
      filtered = customers.filter(
        c =>
          c.name.toLowerCase().includes(searchLower) ||
          c.contactPerson.toLowerCase().includes(searchLower) ||
          c.email.toLowerCase().includes(searchLower)
      );
    }
    const total = filtered.length;
    const start = (page - 1) * pageSize;
    const data = filtered.slice(start, start + pageSize);
    return {
      data,
      total,
      page,
      pageSize,
      totalPages: Math.ceil(total / pageSize),
    };
  },

  async getById(id: string): Promise<Customer | undefined> {
    await delay(100);
    return customers.find(c => c.id === id);
  },

  async create(customer: Omit<Customer, 'id' | 'createdAt' | 'updatedAt'>): Promise<Customer> {
    await delay(200);
    const newCustomer: Customer = {
      ...customer,
      id: uuidv4(),
      createdAt: new Date(),
      updatedAt: new Date(),
    };
    customers.push(newCustomer);
    return newCustomer;
  },

  async update(id: string, customer: Partial<Customer>): Promise<Customer | undefined> {
    await delay(200);
    const index = customers.findIndex(c => c.id === id);
    if (index === -1) return undefined;
    customers[index] = { ...customers[index], ...customer, updatedAt: new Date() };
    return customers[index];
  },

  async delete(id: string): Promise<boolean> {
    await delay(200);
    const index = customers.findIndex(c => c.id === id);
    if (index === -1) return false;
    customers.splice(index, 1);
    return true;
  },
};

// Sender Service
export const senderService = {
  async getAll(page = 1, pageSize = 10, search = ''): Promise<PaginatedResponse<Sender>> {
    await delay(200);
    let filtered = senders;
    if (search) {
      const searchLower = search.toLowerCase();
      filtered = senders.filter(
        s =>
          s.companyName.toLowerCase().includes(searchLower) ||
          s.contactPerson.toLowerCase().includes(searchLower) ||
          s.email.toLowerCase().includes(searchLower)
      );
    }
    const total = filtered.length;
    const start = (page - 1) * pageSize;
    const data = filtered.slice(start, start + pageSize);
    return {
      data,
      total,
      page,
      pageSize,
      totalPages: Math.ceil(total / pageSize),
    };
  },

  async getById(id: string): Promise<Sender | undefined> {
    await delay(100);
    return senders.find(s => s.id === id);
  },

  async create(sender: Omit<Sender, 'id' | 'createdAt' | 'updatedAt'>): Promise<Sender> {
    await delay(200);
    const newSender: Sender = {
      ...sender,
      id: uuidv4(),
      createdAt: new Date(),
      updatedAt: new Date(),
    };
    senders.push(newSender);
    return newSender;
  },

  async update(id: string, sender: Partial<Sender>): Promise<Sender | undefined> {
    await delay(200);
    const index = senders.findIndex(s => s.id === id);
    if (index === -1) return undefined;
    senders[index] = { ...senders[index], ...sender, updatedAt: new Date() };
    return senders[index];
  },

  async delete(id: string): Promise<boolean> {
    await delay(200);
    const index = senders.findIndex(s => s.id === id);
    if (index === -1) return false;
    senders.splice(index, 1);
    return true;
  },
};

// Invoice Service
export const invoiceService = {
  async getAll(
    page = 1,
    pageSize = 10,
    search = '',
    statusFilter?: InvoiceStatus
  ): Promise<PaginatedResponse<Invoice>> {
    await delay(200);
    let filtered = invoices;
    
    if (search) {
      const searchLower = search.toLowerCase();
      filtered = filtered.filter(inv => {
        const customer = customers.find(c => c.id === inv.customerId);
        return (
          inv.invoiceNumber.toLowerCase().includes(searchLower) ||
          customer?.name.toLowerCase().includes(searchLower) ||
          customer?.contactPerson.toLowerCase().includes(searchLower)
        );
      });
    }
    
    if (statusFilter) {
      filtered = filtered.filter(inv => inv.status === statusFilter);
    }
    
    const total = filtered.length;
    const start = (page - 1) * pageSize;
    const data = filtered.slice(start, start + pageSize);
    return {
      data,
      total,
      page,
      pageSize,
      totalPages: Math.ceil(total / pageSize),
    };
  },

  async getById(id: string): Promise<Invoice | undefined> {
    await delay(100);
    return invoices.find(inv => inv.id === id);
  },

  async create(invoice: Omit<Invoice, 'id' | 'createdAt' | 'updatedAt'>): Promise<Invoice> {
    await delay(200);
    const newInvoice: Invoice = {
      ...invoice,
      id: uuidv4(),
      createdAt: new Date(),
      updatedAt: new Date(),
    };
    invoices.push(newInvoice);
    return newInvoice;
  },

  async update(id: string, invoice: Partial<Invoice>): Promise<Invoice | undefined> {
    await delay(200);
    const index = invoices.findIndex(inv => inv.id === id);
    if (index === -1) return undefined;
    invoices[index] = { ...invoices[index], ...invoice, updatedAt: new Date() };
    return invoices[index];
  },

  async delete(id: string): Promise<boolean> {
    await delay(200);
    const index = invoices.findIndex(inv => inv.id === id);
    if (index === -1) return false;
    invoices.splice(index, 1);
    return true;
  },

  async updateStatus(id: string, status: InvoiceStatus): Promise<Invoice | undefined> {
    await delay(200);
    const index = invoices.findIndex(inv => inv.id === id);
    if (index === -1) return undefined;
    invoices[index] = { ...invoices[index], status, updatedAt: new Date() };
    return invoices[index];
  },

  generateInvoiceNumber(): string {
    const year = new Date().getFullYear();
    const maxNum = invoices
      .filter(inv => inv.invoiceNumber.startsWith(`INV-${year}`))
      .map(inv => parseInt(inv.invoiceNumber.split('-')[2] || '0'))
      .reduce((max, num) => Math.max(max, num), 0);
    return `INV-${year}-${String(maxNum + 1).padStart(3, '0')}`;
  },
};

// Helper to calculate invoice totals
export const calculateInvoiceTotals = (lineItems: LineItem[], taxRate: number) => {
  const subtotal = lineItems.reduce((sum, item) => sum + item.total, 0);
  const taxAmount = subtotal * (taxRate / 100);
  const total = subtotal + taxAmount;
  return { subtotal, taxAmount, total };
};

// Helper to get customer/sender by ID
export const getCustomerById = (id: string) => customers.find(c => c.id === id);
export const getSenderById = (id: string) => senders.find(s => s.id === id);
