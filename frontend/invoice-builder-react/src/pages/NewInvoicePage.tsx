import { useState, useEffect, useMemo } from 'react';
import { useNavigate } from 'react-router-dom';
import { v4 as uuidv4 } from 'uuid';
import { Plus, Trash2, X } from 'lucide-react';
import {
  invoiceService,
  customerService,
  senderService,
  calculateInvoiceTotals,
} from '../services/mockApi';
import type { LineItem, Customer, Sender, InvoiceStatus } from '../types';
import { useAppStore } from '../store/appStore';
import Button from '../components/ui/Button';
import Input from '../components/ui/Input';
import Select from '../components/ui/Select';
import TextArea from '../components/ui/TextArea';

const currencies = [
  { value: 'USD', label: 'USD - US Dollar' },
  { value: 'EUR', label: 'EUR - Euro' },
  { value: 'GBP', label: 'GBP - British Pound' },
  { value: 'CAD', label: 'CAD - Canadian Dollar' },
  { value: 'AUD', label: 'AUD - Australian Dollar' },
];

const statusOptions = [
  { value: 'draft', label: 'Draft' },
  { value: 'sent', label: 'Sent' },
  { value: 'paid', label: 'Paid' },
  { value: 'overdue', label: 'Overdue' },
  { value: 'cancelled', label: 'Cancelled' },
];

export default function NewInvoicePage() {
  const navigate = useNavigate();
  const darkMode = useAppStore((state) => state.darkMode);
  const [saving, setSaving] = useState(false);

  // Form state
  const [invoiceNumber, setInvoiceNumber] = useState('');
  const [currency, setCurrency] = useState('USD');
  const [invoiceDate, setInvoiceDate] = useState(
    new Date().toISOString().split('T')[0]
  );
  const [dueDate, setDueDate] = useState('');
  const [customerId, setCustomerId] = useState('');
  const [senderId, setSenderId] = useState('');
  const [taxRate, setTaxRate] = useState(0);
  const [notes, setNotes] = useState('Thank you for your business!');
  const [status, setStatus] = useState<InvoiceStatus>('draft');
  const [lineItems, setLineItems] = useState<LineItem[]>([
    { id: uuidv4(), description: '', quantity: 1, unitPrice: 0, total: 0 },
  ]);

  // Data for dropdowns
  const [customers, setCustomers] = useState<Customer[]>([]);
  const [senders, setSenders] = useState<Sender[]>([]);

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    const [customersRes, sendersRes] = await Promise.all([
      customerService.getAll(1, 100),
      senderService.getAll(1, 100),
    ]);
    setCustomers(customersRes.data);
    setSenders(sendersRes.data);

    // Set default values
    setInvoiceNumber(invoiceService.generateInvoiceNumber());
    const defaultDueDate = new Date();
    defaultDueDate.setDate(defaultDueDate.getDate() + 30);
    setDueDate(defaultDueDate.toISOString().split('T')[0]);

    if (sendersRes.data.length > 0) {
      setSenderId(sendersRes.data[0].id);
    }
  };

  // Calculate totals
  const { subtotal, taxAmount, total } = useMemo(() => {
    return calculateInvoiceTotals(lineItems, taxRate);
  }, [lineItems, taxRate]);

  // Update line item
  const updateLineItem = (id: string, field: keyof LineItem, value: string | number) => {
    setLineItems((items) =>
      items.map((item) => {
        if (item.id !== id) return item;
        const updated = { ...item, [field]: value };
        if (field === 'quantity' || field === 'unitPrice') {
          updated.total = Number(updated.quantity) * Number(updated.unitPrice);
        }
        return updated;
      })
    );
  };

  // Add line item
  const addLineItem = () => {
    setLineItems((items) => [
      ...items,
      { id: uuidv4(), description: '', quantity: 1, unitPrice: 0, total: 0 },
    ]);
  };

  // Remove line item
  const removeLineItem = (id: string) => {
    if (lineItems.length === 1) return;
    setLineItems((items) => items.filter((item) => item.id !== id));
  };

  // Handle submit
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setSaving(true);
    try {
      await invoiceService.create({
        invoiceNumber,
        currency,
        invoiceDate: new Date(invoiceDate),
        dueDate: new Date(dueDate),
        customerId,
        senderId,
        taxRate,
        notes,
        status,
        lineItems,
        subtotal,
        taxAmount,
        total,
      });
      navigate('/invoices');
    } catch (error) {
      console.error('Failed to create invoice:', error);
    } finally {
      setSaving(false);
    }
  };

  const formatCurrency = (amount: number) => {
    return new Intl.NumberFormat('en-US', {
      minimumFractionDigits: 2,
      maximumFractionDigits: 2,
    }).format(amount);
  };

  return (
    <div className="animate-fade-in">
      <form onSubmit={handleSubmit}>
        {/* Header */}
        <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 mb-6">
          <div>
            <h1 className={`text-2xl font-bold ${darkMode ? 'text-white' : 'text-gray-900'}`}>
              New Invoice
            </h1>
            <p className={`text-sm mt-1 ${darkMode ? 'text-gray-400' : 'text-gray-500'}`}>
              Provide invoice details, select Customer and Sender, and add line items.
            </p>
          </div>
          <div className="flex items-center gap-3">
            <Button
              type="button"
              variant="secondary"
              onClick={() => navigate('/invoices')}
              data-testid="cancel-btn"
            >
              Cancel
            </Button>
            <Button type="submit" loading={saving} data-testid="create-invoice-btn">
              Create Invoice
            </Button>
          </div>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
          {/* Main Form */}
          <div className="lg:col-span-2 space-y-6">
            {/* Invoice Details */}
            <div className={`p-6 rounded-xl ${darkMode ? 'bg-slate-800' : 'bg-white'} shadow-sm`}>
              <h2 className={`text-lg font-semibold mb-4 ${darkMode ? 'text-white' : 'text-gray-900'}`}>
                Invoice Details
              </h2>
              <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                <Input
                  label="Invoice Number"
                  value={invoiceNumber}
                  onChange={(e) => setInvoiceNumber(e.target.value)}
                  data-testid="invoice-number-input"
                  required
                />
                <Select
                  label="Currency"
                  value={currency}
                  onChange={(e) => setCurrency(e.target.value)}
                  options={currencies}
                  data-testid="currency-select"
                />
                <Input
                  label="Invoice Date"
                  type="date"
                  value={invoiceDate}
                  onChange={(e) => setInvoiceDate(e.target.value)}
                  data-testid="invoice-date-input"
                  required
                />
                <Input
                  label="Due Date"
                  type="date"
                  value={dueDate}
                  onChange={(e) => setDueDate(e.target.value)}
                  data-testid="due-date-input"
                  required
                />
                <Select
                  label="Customer"
                  value={customerId}
                  onChange={(e) => setCustomerId(e.target.value)}
                  options={customers.map((c) => ({
                    value: c.id,
                    label: `${c.name} — ${c.contactPerson}`,
                  }))}
                  placeholder="Select a customer"
                  data-testid="customer-select"
                  required
                />
                <Select
                  label="Sender"
                  value={senderId}
                  onChange={(e) => setSenderId(e.target.value)}
                  options={senders.map((s) => ({
                    value: s.id,
                    label: `${s.companyName} — ${s.contactPerson}`,
                  }))}
                  placeholder="Select a sender"
                  data-testid="sender-select"
                  required
                />
                <Input
                  label="Tax Rate (%)"
                  type="number"
                  min="0"
                  max="100"
                  step="0.01"
                  value={taxRate}
                  onChange={(e) => setTaxRate(Number(e.target.value))}
                  data-testid="tax-rate-input"
                />
                <Select
                  label="Status"
                  value={status}
                  onChange={(e) => setStatus(e.target.value as InvoiceStatus)}
                  options={statusOptions}
                  data-testid="status-select"
                />
              </div>
              <div className="mt-4">
                <TextArea
                  label="Notes"
                  value={notes}
                  onChange={(e) => setNotes(e.target.value)}
                  placeholder="Add any additional notes..."
                  data-testid="notes-input"
                />
              </div>
            </div>

            {/* Line Items */}
            <div className={`p-6 rounded-xl ${darkMode ? 'bg-slate-800' : 'bg-white'} shadow-sm`}>
              <div className="flex items-center justify-between mb-4">
                <h2 className={`text-lg font-semibold ${darkMode ? 'text-white' : 'text-gray-900'}`}>
                  Line Items
                </h2>
                <Button
                  type="button"
                  variant="secondary"
                  size="sm"
                  onClick={addLineItem}
                  data-testid="add-line-item-btn"
                >
                  <Plus className="w-4 h-4" />
                  Add Item
                </Button>
              </div>

              <div className="space-y-4">
                {/* Header Row */}
                <div className={`hidden sm:grid grid-cols-12 gap-2 text-xs font-semibold uppercase ${darkMode ? 'text-gray-400' : 'text-gray-500'}`}>
                  <div className="col-span-5">Description</div>
                  <div className="col-span-2">Qty</div>
                  <div className="col-span-2">Unit Price</div>
                  <div className="col-span-2">Total</div>
                  <div className="col-span-1"></div>
                </div>

                {/* Line Items */}
                {lineItems.map((item, index) => (
                  <div
                    key={item.id}
                    className={`grid grid-cols-1 sm:grid-cols-12 gap-2 p-3 rounded-lg ${
                      darkMode ? 'bg-slate-700/50' : 'bg-gray-50'
                    }`}
                    data-testid={`line-item-${index}`}
                  >
                    <div className="sm:col-span-5">
                      <label className={`sm:hidden text-xs font-medium ${darkMode ? 'text-gray-400' : 'text-gray-500'}`}>
                        Description
                      </label>
                      <input
                        type="text"
                        value={item.description}
                        onChange={(e) => updateLineItem(item.id, 'description', e.target.value)}
                        placeholder="Item description"
                        data-testid={`line-item-desc-${index}`}
                        className={`w-full px-3 py-2 rounded-lg border ${
                          darkMode
                            ? 'bg-slate-800 border-slate-600 text-white'
                            : 'bg-white border-gray-300 text-gray-900'
                        }`}
                      />
                    </div>
                    <div className="sm:col-span-2">
                      <label className={`sm:hidden text-xs font-medium ${darkMode ? 'text-gray-400' : 'text-gray-500'}`}>
                        Quantity
                      </label>
                      <input
                        type="number"
                        min="1"
                        value={item.quantity}
                        onChange={(e) => updateLineItem(item.id, 'quantity', Number(e.target.value))}
                        data-testid={`line-item-qty-${index}`}
                        className={`w-full px-3 py-2 rounded-lg border ${
                          darkMode
                            ? 'bg-slate-800 border-slate-600 text-white'
                            : 'bg-white border-gray-300 text-gray-900'
                        }`}
                      />
                    </div>
                    <div className="sm:col-span-2">
                      <label className={`sm:hidden text-xs font-medium ${darkMode ? 'text-gray-400' : 'text-gray-500'}`}>
                        Unit Price
                      </label>
                      <input
                        type="number"
                        min="0"
                        step="0.01"
                        value={item.unitPrice}
                        onChange={(e) => updateLineItem(item.id, 'unitPrice', Number(e.target.value))}
                        data-testid={`line-item-price-${index}`}
                        className={`w-full px-3 py-2 rounded-lg border ${
                          darkMode
                            ? 'bg-slate-800 border-slate-600 text-white'
                            : 'bg-white border-gray-300 text-gray-900'
                        }`}
                      />
                    </div>
                    <div className="sm:col-span-2">
                      <label className={`sm:hidden text-xs font-medium ${darkMode ? 'text-gray-400' : 'text-gray-500'}`}>
                        Total
                      </label>
                      <input
                        type="text"
                        value={formatCurrency(item.total)}
                        readOnly
                        data-testid={`line-item-total-${index}`}
                        className={`w-full px-3 py-2 rounded-lg border ${
                          darkMode
                            ? 'bg-slate-900 border-slate-600 text-gray-400'
                            : 'bg-gray-100 border-gray-300 text-gray-600'
                        }`}
                      />
                    </div>
                    <div className="sm:col-span-1 flex items-end sm:justify-center">
                      <Button
                        type="button"
                        variant="ghost"
                        size="sm"
                        onClick={() => removeLineItem(item.id)}
                        disabled={lineItems.length === 1}
                        data-testid={`remove-line-item-${index}`}
                        className="text-red-500 hover:text-red-600 hover:bg-red-50 dark:hover:bg-red-900/20"
                      >
                        <Trash2 className="w-4 h-4" />
                        <span className="sm:hidden ml-1">Remove</span>
                      </Button>
                    </div>
                  </div>
                ))}
              </div>
            </div>
          </div>

          {/* Summary Sidebar */}
          <div className="lg:col-span-1">
            <div className={`p-6 rounded-xl ${darkMode ? 'bg-slate-800' : 'bg-white'} shadow-sm sticky top-24`}>
              <h2 className={`text-lg font-semibold mb-4 ${darkMode ? 'text-white' : 'text-gray-900'}`}>
                Summary
              </h2>
              <div className="space-y-3">
                <div className="flex justify-between">
                  <span className={darkMode ? 'text-gray-400' : 'text-gray-600'}>Subtotal</span>
                  <span className={darkMode ? 'text-white' : 'text-gray-900'} data-testid="summary-subtotal">
                    {formatCurrency(subtotal)}
                  </span>
                </div>
                <div className="flex justify-between">
                  <span className={darkMode ? 'text-gray-400' : 'text-gray-600'}>
                    Tax Rate
                  </span>
                  <span className={darkMode ? 'text-white' : 'text-gray-900'} data-testid="summary-tax-rate">
                    {taxRate.toFixed(2)}%
                  </span>
                </div>
                <div className="flex justify-between">
                  <span className={darkMode ? 'text-gray-400' : 'text-gray-600'}>Tax Amount</span>
                  <span className={darkMode ? 'text-white' : 'text-gray-900'} data-testid="summary-tax-amount">
                    {formatCurrency(taxAmount)}
                  </span>
                </div>
                <div className={`border-t pt-3 ${darkMode ? 'border-slate-700' : 'border-gray-200'}`}>
                  <div className="flex justify-between">
                    <span className={`font-semibold ${darkMode ? 'text-white' : 'text-gray-900'}`}>
                      Total
                    </span>
                    <span className={`font-bold text-lg ${darkMode ? 'text-white' : 'text-gray-900'}`} data-testid="summary-total">
                      {currency} {formatCurrency(total)}
                    </span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </form>
    </div>
  );
}
