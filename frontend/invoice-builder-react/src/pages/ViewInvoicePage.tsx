import { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { ArrowLeft, Download, Pencil, Printer } from 'lucide-react';
import { invoiceService, getCustomerById, getSenderById } from '../services/mockApi';
import { downloadInvoicePDF } from '../services/pdfService';
import type { Invoice } from '../types';
import { useAppStore } from '../store/appStore';
import Button from '../components/ui/Button';
import StatusBadge from '../components/ui/StatusBadge';
import LoadingSpinner from '../components/ui/LoadingSpinner';

export default function ViewInvoicePage() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const darkMode = useAppStore((state) => state.darkMode);
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

  const formatDate = (date: Date) => {
    return new Date(date).toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'long',
      day: 'numeric',
    });
  };

  const formatCurrency = (amount: number, currency: string) => {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: currency,
    }).format(amount);
  };

  if (loading) {
    return <LoadingSpinner />;
  }

  if (!invoice) {
    return (
      <div className="text-center py-12">
        <p className={darkMode ? 'text-gray-400' : 'text-gray-600'}>Invoice not found</p>
        <Button onClick={() => navigate('/invoices')} className="mt-4">
          Back to Invoices
        </Button>
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
          <Button
            variant="ghost"
            size="sm"
            onClick={() => navigate('/invoices')}
            data-testid="back-btn"
          >
            <ArrowLeft className="w-4 h-4" />
          </Button>
          <div>
            <div className="flex items-center gap-3">
              <h1 className={`text-2xl font-bold ${darkMode ? 'text-white' : 'text-gray-900'}`}>
                {invoice.invoiceNumber}
              </h1>
              <StatusBadge status={invoice.status} />
            </div>
            <p className={`text-sm mt-1 ${darkMode ? 'text-gray-400' : 'text-gray-500'}`}>
              Created on {formatDate(invoice.createdAt)}
            </p>
          </div>
        </div>
        <div className="flex items-center gap-3">
          <Button
            variant="secondary"
            onClick={handleDownload}
            loading={downloading}
            data-testid="download-pdf-btn"
          >
            <Download className="w-4 h-4" />
            Download PDF
          </Button>
          <Button
            onClick={() => navigate(`/invoices/${invoice.id}/edit`)}
            data-testid="edit-invoice-btn"
          >
            <Pencil className="w-4 h-4" />
            Edit
          </Button>
        </div>
      </div>

      {/* Invoice Preview */}
      <div className={`rounded-xl ${darkMode ? 'bg-slate-800' : 'bg-white'} shadow-sm overflow-hidden`}>
        {/* Invoice Header */}
        <div className={`px-8 py-6 border-b ${darkMode ? 'border-slate-700 bg-gradient-to-r from-violet-900/30 to-purple-900/30' : 'border-gray-200 bg-gradient-to-r from-violet-50 to-purple-50'}`}>
          <div className="flex flex-col sm:flex-row justify-between gap-6">
            <div>
              <h2 className={`text-3xl font-bold ${darkMode ? 'text-white' : 'text-gray-900'}`}>
                INVOICE
              </h2>
              <p className={`text-lg mt-1 ${darkMode ? 'text-violet-300' : 'text-violet-600'}`}>
                {invoice.invoiceNumber}
              </p>
            </div>
            <div className="text-right">
              <p className={`text-sm ${darkMode ? 'text-gray-400' : 'text-gray-600'}`}>Invoice Date</p>
              <p className={`font-medium ${darkMode ? 'text-white' : 'text-gray-900'}`}>
                {formatDate(invoice.invoiceDate)}
              </p>
              <p className={`text-sm mt-2 ${darkMode ? 'text-gray-400' : 'text-gray-600'}`}>Due Date</p>
              <p className={`font-medium ${darkMode ? 'text-white' : 'text-gray-900'}`}>
                {formatDate(invoice.dueDate)}
              </p>
            </div>
          </div>
        </div>

        {/* From / To Section */}
        <div className="px-8 py-6 grid grid-cols-1 md:grid-cols-2 gap-8">
          {/* From */}
          <div>
            <h3 className={`text-xs font-semibold uppercase tracking-wider mb-3 ${darkMode ? 'text-gray-400' : 'text-gray-500'}`}>
              From
            </h3>
            {sender && (
              <div className={darkMode ? 'text-gray-300' : 'text-gray-700'}>
                <p className={`font-semibold text-lg ${darkMode ? 'text-white' : 'text-gray-900'}`}>
                  {sender.companyName}
                </p>
                <p>{sender.contactPerson}</p>
                <p>{sender.email}</p>
                <p>{sender.phone}</p>
                <p className="mt-2">{sender.address}</p>
                {sender.bankDetails && (
                  <p className="mt-2 text-sm">{sender.bankDetails}</p>
                )}
              </div>
            )}
          </div>

          {/* To */}
          <div>
            <h3 className={`text-xs font-semibold uppercase tracking-wider mb-3 ${darkMode ? 'text-gray-400' : 'text-gray-500'}`}>
              Bill To
            </h3>
            {customer && (
              <div className={darkMode ? 'text-gray-300' : 'text-gray-700'}>
                <p className={`font-semibold text-lg ${darkMode ? 'text-white' : 'text-gray-900'}`}>
                  {customer.name}
                </p>
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
              <tr className={darkMode ? 'border-b border-slate-700' : 'border-b border-gray-200'}>
                <th className={`pb-3 text-left text-xs font-semibold uppercase tracking-wider ${darkMode ? 'text-gray-400' : 'text-gray-500'}`}>
                  Description
                </th>
                <th className={`pb-3 text-right text-xs font-semibold uppercase tracking-wider ${darkMode ? 'text-gray-400' : 'text-gray-500'}`}>
                  Qty
                </th>
                <th className={`pb-3 text-right text-xs font-semibold uppercase tracking-wider ${darkMode ? 'text-gray-400' : 'text-gray-500'}`}>
                  Unit Price
                </th>
                <th className={`pb-3 text-right text-xs font-semibold uppercase tracking-wider ${darkMode ? 'text-gray-400' : 'text-gray-500'}`}>
                  Total
                </th>
              </tr>
            </thead>
            <tbody className={`divide-y ${darkMode ? 'divide-slate-700' : 'divide-gray-100'}`}>
              {invoice.lineItems.map((item, index) => (
                <tr key={item.id} data-testid={`view-line-item-${index}`}>
                  <td className={`py-4 ${darkMode ? 'text-white' : 'text-gray-900'}`}>
                    {item.description}
                  </td>
                  <td className={`py-4 text-right ${darkMode ? 'text-gray-300' : 'text-gray-700'}`}>
                    {item.quantity}
                  </td>
                  <td className={`py-4 text-right ${darkMode ? 'text-gray-300' : 'text-gray-700'}`}>
                    {formatCurrency(item.unitPrice, invoice.currency)}
                  </td>
                  <td className={`py-4 text-right font-medium ${darkMode ? 'text-white' : 'text-gray-900'}`}>
                    {formatCurrency(item.total, invoice.currency)}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>

        {/* Summary */}
        <div className={`px-8 py-6 ${darkMode ? 'bg-slate-700/50' : 'bg-gray-50'}`}>
          <div className="flex justify-end">
            <div className="w-full max-w-xs space-y-3">
              <div className="flex justify-between">
                <span className={darkMode ? 'text-gray-400' : 'text-gray-600'}>Subtotal</span>
                <span className={darkMode ? 'text-white' : 'text-gray-900'}>
                  {formatCurrency(invoice.subtotal, invoice.currency)}
                </span>
              </div>
              <div className="flex justify-between">
                <span className={darkMode ? 'text-gray-400' : 'text-gray-600'}>
                  Tax ({invoice.taxRate}%)
                </span>
                <span className={darkMode ? 'text-white' : 'text-gray-900'}>
                  {formatCurrency(invoice.taxAmount, invoice.currency)}
                </span>
              </div>
              <div className={`flex justify-between pt-3 border-t ${darkMode ? 'border-slate-600' : 'border-gray-300'}`}>
                <span className={`font-semibold text-lg ${darkMode ? 'text-white' : 'text-gray-900'}`}>
                  Total
                </span>
                <span className={`font-bold text-xl ${darkMode ? 'text-white' : 'text-gray-900'}`}>
                  {formatCurrency(invoice.total, invoice.currency)}
                </span>
              </div>
            </div>
          </div>
        </div>

        {/* Notes */}
        {invoice.notes && (
          <div className={`px-8 py-6 border-t ${darkMode ? 'border-slate-700' : 'border-gray-200'}`}>
            <h3 className={`text-xs font-semibold uppercase tracking-wider mb-2 ${darkMode ? 'text-gray-400' : 'text-gray-500'}`}>
              Notes
            </h3>
            <p className={darkMode ? 'text-gray-300' : 'text-gray-700'}>
              {invoice.notes}
            </p>
          </div>
        )}
      </div>
    </div>
  );
}
