import { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { ArrowLeft, Download, Pencil } from 'lucide-react';
import { invoiceService, getCustomerById, getSenderById } from '../services/mockApi';
import { downloadInvoicePDF } from '../services/pdfService';
import type { Invoice } from '../types';
import Button from '../components/ui/Button';
import StatusBadge from '../components/ui/StatusBadge';
import LoadingSpinner from '../components/ui/LoadingSpinner';

export default function ViewInvoicePage() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const [loading, setLoading] = useState(true);
  const [invoice, setInvoice] = useState<Invoice | null>(null);
  const [downloading, setDownloading] = useState(false);

  useEffect(() => {
    loadInvoice();
  }, [id]);

  const loadInvoice = async () => {
    if (!id) return;
    setLoading(true);
    try {
      const data = await invoiceService.getById(id);
      setInvoice(data || null);
    } catch (error) {
      console.error('Failed to load invoice:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleDownload = async () => {
    if (!invoice) return;
    setDownloading(true);
    try {
      await downloadInvoicePDF(invoice);
    } catch (error) {
      console.error('Failed to download PDF:', error);
    } finally {
      setDownloading(false);
    }
  };

  const formatDate = (date: Date) => new Date(date).toLocaleDateString('en-US', { year: 'numeric', month: 'long', day: 'numeric' });
  const formatCurrency = (amount: number, currency: string) => new Intl.NumberFormat('en-US', { style: 'currency', currency }).format(amount);

  if (loading) return <LoadingSpinner />;

  if (!invoice) {
    return (
      <div className="text-center py-12">
        <p className="text-gray-600 dark:text-gray-400">Invoice not found</p>
        <Button onClick={() => navigate('/invoices')} className="mt-4">Back to Invoices</Button>
      </div>
    );
  }

  const customer = getCustomerById(invoice.customerId);
  const sender = getSenderById(invoice.senderId);

  return (
    <div className="animate-fade-in">
      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 mb-6">
        <div className="flex items-center gap-4">
          <Button variant="ghost" size="sm" onClick={() => navigate('/invoices')} data-testid="back-btn">
            <ArrowLeft className="w-4 h-4" />
          </Button>
          <div>
            <div className="flex items-center gap-3">
              <h1 className="text-2xl font-bold text-gray-900 dark:text-white">{invoice.invoiceNumber}</h1>
              <StatusBadge status={invoice.status} />
            </div>
            <p className="text-sm mt-1 text-gray-500 dark:text-gray-400">Created on {formatDate(invoice.createdAt)}</p>
          </div>
        </div>
        <div className="flex items-center gap-3">
          <Button variant="secondary" onClick={handleDownload} loading={downloading} data-testid="download-pdf-btn">
            <Download className="w-4 h-4" /> Download PDF
          </Button>
          <Button onClick={() => navigate(`/invoices/${invoice.id}/edit`)} data-testid="edit-invoice-btn">
            <Pencil className="w-4 h-4" /> Edit
          </Button>
        </div>
      </div>

      {/* Invoice Preview */}
      <div className="rounded-xl bg-white dark:bg-slate-800 shadow-sm overflow-hidden">
        {/* Invoice Header */}
        <div className="px-8 py-6 border-b border-gray-200 dark:border-slate-700 bg-gradient-to-r from-violet-50 to-purple-50 dark:from-violet-900/30 dark:to-purple-900/30">
          <div className="flex flex-col sm:flex-row justify-between gap-6">
            <div>
              <h2 className="text-3xl font-bold text-gray-900 dark:text-white">INVOICE</h2>
              <p className="text-lg mt-1 text-violet-600 dark:text-violet-300">{invoice.invoiceNumber}</p>
            </div>
            <div className="text-right">
              <p className="text-sm text-gray-600 dark:text-gray-400">Invoice Date</p>
              <p className="font-medium text-gray-900 dark:text-white">{formatDate(invoice.invoiceDate)}</p>
              <p className="text-sm mt-2 text-gray-600 dark:text-gray-400">Due Date</p>
              <p className="font-medium text-gray-900 dark:text-white">{formatDate(invoice.dueDate)}</p>
            </div>
          </div>
        </div>

        {/* From / To Section */}
        <div className="px-8 py-6 grid grid-cols-1 md:grid-cols-2 gap-8">
          <div>
            <h3 className="text-xs font-semibold uppercase tracking-wider mb-3 text-gray-500 dark:text-gray-400">From</h3>
            {sender && (
              <div className="text-gray-700 dark:text-gray-300">
                <p className="font-semibold text-lg text-gray-900 dark:text-white">{sender.companyName}</p>
                <p>{sender.contactPerson}</p>
                <p>{sender.email}</p>
                <p>{sender.phone}</p>
                <p className="mt-2">{sender.address}</p>
                {sender.bankDetails && <p className="mt-2 text-sm">{sender.bankDetails}</p>}
              </div>
            )}
          </div>
          <div>
            <h3 className="text-xs font-semibold uppercase tracking-wider mb-3 text-gray-500 dark:text-gray-400">Bill To</h3>
            {customer && (
              <div className="text-gray-700 dark:text-gray-300">
                <p className="font-semibold text-lg text-gray-900 dark:text-white">{customer.name}</p>
                <p>{customer.contactPerson}</p>
                <p>{customer.email}</p>
                <p>{customer.phone}</p>
                <p className="mt-2">{customer.address}</p>
              </div>
            )}
          </div>
        </div>

        {/* Line Items Table */}
        <div className="px-8 py-6">
          <table className="w-full" data-testid="view-invoice-table">
            <thead>
              <tr className="border-b border-gray-200 dark:border-slate-700">
                <th className="pb-3 text-left text-xs font-semibold uppercase tracking-wider text-gray-500 dark:text-gray-400">Description</th>
                <th className="pb-3 text-right text-xs font-semibold uppercase tracking-wider text-gray-500 dark:text-gray-400">Qty</th>
                <th className="pb-3 text-right text-xs font-semibold uppercase tracking-wider text-gray-500 dark:text-gray-400">Unit Price</th>
                <th className="pb-3 text-right text-xs font-semibold uppercase tracking-wider text-gray-500 dark:text-gray-400">Total</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-100 dark:divide-slate-700">
              {invoice.lineItems.map((item, index) => (
                <tr key={item.id} data-testid={`view-line-item-${index}`}>
                  <td className="py-4 text-gray-900 dark:text-white">{item.description}</td>
                  <td className="py-4 text-right text-gray-700 dark:text-gray-300">{item.quantity}</td>
                  <td className="py-4 text-right text-gray-700 dark:text-gray-300">{formatCurrency(item.unitPrice, invoice.currency)}</td>
                  <td className="py-4 text-right font-medium text-gray-900 dark:text-white">{formatCurrency(item.total, invoice.currency)}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>

        {/* Summary */}
        <div className="px-8 py-6 bg-gray-50 dark:bg-slate-700/50">
          <div className="flex justify-end">
            <div className="w-full max-w-xs space-y-3">
              <div className="flex justify-between">
                <span className="text-gray-600 dark:text-gray-400">Subtotal</span>
                <span className="text-gray-900 dark:text-white">{formatCurrency(invoice.subtotal, invoice.currency)}</span>
              </div>
              <div className="flex justify-between">
                <span className="text-gray-600 dark:text-gray-400">Tax ({invoice.taxRate}%)</span>
                <span className="text-gray-900 dark:text-white">{formatCurrency(invoice.taxAmount, invoice.currency)}</span>
              </div>
              <div className="flex justify-between pt-3 border-t border-gray-300 dark:border-slate-600">
                <span className="font-semibold text-lg text-gray-900 dark:text-white">Total</span>
                <span className="font-bold text-xl text-gray-900 dark:text-white">{formatCurrency(invoice.total, invoice.currency)}</span>
              </div>
            </div>
          </div>
        </div>

        {/* Notes */}
        {invoice.notes && (
          <div className="px-8 py-6 border-t border-gray-200 dark:border-slate-700">
            <h3 className="text-xs font-semibold uppercase tracking-wider mb-2 text-gray-500 dark:text-gray-400">Notes</h3>
            <p className="text-gray-700 dark:text-gray-300">{invoice.notes}</p>
          </div>
        )}
      </div>
    </div>
  );
}
