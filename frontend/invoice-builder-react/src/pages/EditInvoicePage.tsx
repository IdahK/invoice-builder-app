import { useState, useEffect, useMemo } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { v4 as uuidv4 } from 'uuid';
import { Plus, Trash2 } from 'lucide-react';
import { invoiceService, customerService, senderService, calculateInvoiceTotals } from '../services/mockApi';
import type { Invoice, LineItem, Customer, Sender, InvoiceStatus } from '../types';
import Button from '../components/ui/Button';
import Input from '../components/ui/Input';
import Select from '../components/ui/Select';
import TextArea from '../components/ui/TextArea';
import LoadingSpinner from '../components/ui/LoadingSpinner';

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

export default function EditInvoicePage() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [invoice, setInvoice] = useState<Invoice | null>(null);

  const [invoiceNumber, setInvoiceNumber] = useState('');
  const [currency, setCurrency] = useState('USD');
  const [invoiceDate, setInvoiceDate] = useState('');
  const [dueDate, setDueDate] = useState('');
  const [customerId, setCustomerId] = useState('');
  const [senderId, setSenderId] = useState('');
  const [taxRate, setTaxRate] = useState(0);
  const [notes, setNotes] = useState('');
  const [status, setStatus] = useState<InvoiceStatus>('draft');
  const [lineItems, setLineItems] = useState<LineItem[]>([]);

  const [customers, setCustomers] = useState<Customer[]>([]);
  const [senders, setSenders] = useState<Sender[]>([]);

  useEffect(() => {
    loadData();
  }, [id]);

  const loadData = async () => {
    if (!id) return;
    setLoading(true);
    try {
      const [invoiceData, customersRes, sendersRes] = await Promise.all([
        invoiceService.getById(id),
        customerService.getAll(1, 100),
        senderService.getAll(1, 100),
      ]);

      setCustomers(customersRes.data);
      setSenders(sendersRes.data);

      if (invoiceData) {
        setInvoice(invoiceData);
        setInvoiceNumber(invoiceData.invoiceNumber);
        setCurrency(invoiceData.currency);
        setInvoiceDate(new Date(invoiceData.invoiceDate).toISOString().split('T')[0]);
        setDueDate(new Date(invoiceData.dueDate).toISOString().split('T')[0]);
        setCustomerId(invoiceData.customerId);
        setSenderId(invoiceData.senderId);
        setTaxRate(invoiceData.taxRate);
        setNotes(invoiceData.notes);
        setStatus(invoiceData.status);
        setLineItems(invoiceData.lineItems);
      }
    } catch (error) {
      console.error('Failed to load invoice:', error);
    } finally {
      setLoading(false);
    }
  };

  const { subtotal, taxAmount, total } = useMemo(() => {
    return calculateInvoiceTotals(lineItems, taxRate);
  }, [lineItems, taxRate]);

  const updateLineItem = (itemId: string, field: keyof LineItem, value: string | number) => {
    setLineItems((items) =>
      items.map((item) => {
        if (item.id !== itemId) return item;
        const updated = { ...item, [field]: value };
        if (field === 'quantity' || field === 'unitPrice') {
          updated.total = Number(updated.quantity) * Number(updated.unitPrice);
        }
        return updated;
      })
    );
  };

  const addLineItem = () => {
    setLineItems((items) => [...items, { id: uuidv4(), description: '', quantity: 1, unitPrice: 0, total: 0 }]);
  };

  const removeLineItem = (itemId: string) => {
    if (lineItems.length === 1) return;
    setLineItems((items) => items.filter((item) => item.id !== itemId));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!id) return;
    setSaving(true);
    try {
      await invoiceService.update(id, {
        invoiceNumber, currency,
        invoiceDate: new Date(invoiceDate), dueDate: new Date(dueDate),
        customerId, senderId, taxRate, notes, status, lineItems, subtotal, taxAmount, total,
      });
      navigate('/invoices');
    } catch (error) {
      console.error('Failed to update invoice:', error);
    } finally {
      setSaving(false);
    }
  };

  const formatCurrency = (amount: number) => new Intl.NumberFormat('en-US', { minimumFractionDigits: 2, maximumFractionDigits: 2 }).format(amount);

  if (loading) return <LoadingSpinner />;

  if (!invoice) {
    return (
      <div className="text-center py-12">
        <p className="text-gray-600 dark:text-gray-400">Invoice not found</p>
        <Button onClick={() => navigate('/invoices')} className="mt-4">Back to Invoices</Button>
      </div>
    );
  }

  return (
    <div className="animate-fade-in">
      <form onSubmit={handleSubmit}>
        <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 mb-6">
          <div>
            <h1 className="text-2xl font-bold text-gray-900 dark:text-white">Edit Invoice</h1>
            <p className="text-sm mt-1 text-gray-500 dark:text-gray-400">Update invoice details for {invoiceNumber}</p>
          </div>
          <div className="flex items-center gap-3">
            <Button type="button" variant="secondary" onClick={() => navigate('/invoices')} data-testid="cancel-edit-btn">Cancel</Button>
            <Button type="submit" loading={saving} data-testid="save-invoice-btn">Save Changes</Button>
          </div>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
          <div className="lg:col-span-2 space-y-6">
            <div className="p-6 rounded-xl bg-white dark:bg-slate-800 shadow-sm">
              <h2 className="text-lg font-semibold mb-4 text-gray-900 dark:text-white">Invoice Details</h2>
              <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                <Input label="Invoice Number" value={invoiceNumber} onChange={(e) => setInvoiceNumber(e.target.value)} data-testid="edit-invoice-number-input" required />
                <Select label="Currency" value={currency} onChange={(e) => setCurrency(e.target.value)} options={currencies} data-testid="edit-currency-select" />
                <Input label="Invoice Date" type="date" value={invoiceDate} onChange={(e) => setInvoiceDate(e.target.value)} data-testid="edit-invoice-date-input" required />
                <Input label="Due Date" type="date" value={dueDate} onChange={(e) => setDueDate(e.target.value)} data-testid="edit-due-date-input" required />
                <Select label="Customer" value={customerId} onChange={(e) => setCustomerId(e.target.value)} options={customers.map((c) => ({ value: c.id, label: `${c.name} — ${c.contactPerson}` }))} placeholder="Select a customer" data-testid="edit-customer-select" required />
                <Select label="Sender" value={senderId} onChange={(e) => setSenderId(e.target.value)} options={senders.map((s) => ({ value: s.id, label: `${s.companyName} — ${s.contactPerson}` }))} placeholder="Select a sender" data-testid="edit-sender-select" required />
                <Input label="Tax Rate (%)" type="number" min="0" max="100" step="0.01" value={taxRate} onChange={(e) => setTaxRate(Number(e.target.value))} data-testid="edit-tax-rate-input" />
                <Select label="Status" value={status} onChange={(e) => setStatus(e.target.value as InvoiceStatus)} options={statusOptions} data-testid="edit-status-select" />
              </div>
              <div className="mt-4">
                <TextArea label="Notes" value={notes} onChange={(e) => setNotes(e.target.value)} placeholder="Add any additional notes..." data-testid="edit-notes-input" />
              </div>
            </div>

            <div className="p-6 rounded-xl bg-white dark:bg-slate-800 shadow-sm">
              <div className="flex items-center justify-between mb-4">
                <h2 className="text-lg font-semibold text-gray-900 dark:text-white">Line Items</h2>
                <Button type="button" variant="secondary" size="sm" onClick={addLineItem} data-testid="edit-add-line-item-btn">
                  <Plus className="w-4 h-4" /> Add Item
                </Button>
              </div>
              <div className="space-y-4">
                <div className="hidden sm:grid grid-cols-12 gap-2 text-xs font-semibold uppercase text-gray-500 dark:text-gray-400">
                  <div className="col-span-5">Description</div>
                  <div className="col-span-2">Qty</div>
                  <div className="col-span-2">Unit Price</div>
                  <div className="col-span-2">Total</div>
                  <div className="col-span-1"></div>
                </div>
                {lineItems.map((item, index) => (
                  <div key={item.id} className="grid grid-cols-1 sm:grid-cols-12 gap-2 p-3 rounded-lg bg-gray-50 dark:bg-slate-700/50" data-testid={`edit-line-item-${index}`}>
                    <div className="sm:col-span-5">
                      <input type="text" value={item.description} onChange={(e) => updateLineItem(item.id, 'description', e.target.value)} placeholder="Item description" className="w-full px-3 py-2 rounded-lg border bg-white border-gray-300 text-gray-900 dark:bg-slate-800 dark:border-slate-600 dark:text-white" />
                    </div>
                    <div className="sm:col-span-2">
                      <input type="number" min="1" value={item.quantity} onChange={(e) => updateLineItem(item.id, 'quantity', Number(e.target.value))} className="w-full px-3 py-2 rounded-lg border bg-white border-gray-300 text-gray-900 dark:bg-slate-800 dark:border-slate-600 dark:text-white" />
                    </div>
                    <div className="sm:col-span-2">
                      <input type="number" min="0" step="0.01" value={item.unitPrice} onChange={(e) => updateLineItem(item.id, 'unitPrice', Number(e.target.value))} className="w-full px-3 py-2 rounded-lg border bg-white border-gray-300 text-gray-900 dark:bg-slate-800 dark:border-slate-600 dark:text-white" />
                    </div>
                    <div className="sm:col-span-2">
                      <input type="text" value={formatCurrency(item.total)} readOnly className="w-full px-3 py-2 rounded-lg border bg-gray-100 border-gray-300 text-gray-600 dark:bg-slate-900 dark:border-slate-600 dark:text-gray-400" />
                    </div>
                    <div className="sm:col-span-1 flex items-end sm:justify-center">
                      <Button type="button" variant="ghost" size="sm" onClick={() => removeLineItem(item.id)} disabled={lineItems.length === 1} className="text-red-500 hover:text-red-600 hover:bg-red-50 dark:hover:bg-red-900/20">
                        <Trash2 className="w-4 h-4" />
                      </Button>
                    </div>
                  </div>
                ))}
              </div>
            </div>
          </div>

          <div className="lg:col-span-1">
            <div className="p-6 rounded-xl bg-white dark:bg-slate-800 shadow-sm sticky top-24">
              <h2 className="text-lg font-semibold mb-4 text-gray-900 dark:text-white">Summary</h2>
              <div className="space-y-3">
                <div className="flex justify-between">
                  <span className="text-gray-600 dark:text-gray-400">Subtotal</span>
                  <span className="text-gray-900 dark:text-white">{formatCurrency(subtotal)}</span>
                </div>
                <div className="flex justify-between">
                  <span className="text-gray-600 dark:text-gray-400">Tax Rate</span>
                  <span className="text-gray-900 dark:text-white">{taxRate.toFixed(2)}%</span>
                </div>
                <div className="flex justify-between">
                  <span className="text-gray-600 dark:text-gray-400">Tax Amount</span>
                  <span className="text-gray-900 dark:text-white">{formatCurrency(taxAmount)}</span>
                </div>
                <div className="border-t pt-3 border-gray-200 dark:border-slate-700">
                  <div className="flex justify-between">
                    <span className="font-semibold text-gray-900 dark:text-white">Total</span>
                    <span className="font-bold text-lg text-gray-900 dark:text-white">{currency} {formatCurrency(total)}</span>
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
