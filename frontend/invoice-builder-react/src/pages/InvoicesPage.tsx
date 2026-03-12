import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Plus, Eye, Pencil, Trash2, Download, Filter } from 'lucide-react';
import { invoiceService, getCustomerById } from '../services/mockApi';
import { downloadInvoicePDF } from '../services/pdfService';
import type { Invoice, InvoiceStatus } from '../types';
import Button from '../components/ui/Button';
import SearchInput from '../components/ui/SearchInput';
import StatusBadge from '../components/ui/StatusBadge';
import Pagination from '../components/ui/Pagination';
import LoadingSpinner from '../components/ui/LoadingSpinner';
import EmptyState from '../components/ui/EmptyState';
import ConfirmDialog from '../components/ui/ConfirmDialog';

const PAGE_SIZE = 10;

const statusOptions: { value: InvoiceStatus | ''; label: string }[] = [
  { value: '', label: 'All Statuses' },
  { value: 'draft', label: 'Draft' },
  { value: 'sent', label: 'Sent' },
  { value: 'paid', label: 'Paid' },
  { value: 'overdue', label: 'Overdue' },
  { value: 'cancelled', label: 'Cancelled' },
];

export default function InvoicesPage() {
  const navigate = useNavigate();
  const [invoices, setInvoices] = useState<Invoice[]>([]);
  const [loading, setLoading] = useState(true);
  const [search, setSearch] = useState('');
  const [statusFilter, setStatusFilter] = useState<InvoiceStatus | ''>('');
  const [currentPage, setCurrentPage] = useState(1);
  const [totalPages, setTotalPages] = useState(1);
  const [totalItems, setTotalItems] = useState(0);
  const [deleteId, setDeleteId] = useState<string | null>(null);
  const [deleting, setDeleting] = useState(false);
  const [downloadingId, setDownloadingId] = useState<string | null>(null);

  useEffect(() => {
    loadInvoices();
  }, [currentPage, search, statusFilter]);

  const loadInvoices = async () => {
    setLoading(true);
    try {
      const response = await invoiceService.getAll(
        currentPage,
        PAGE_SIZE,
        search,
        statusFilter || undefined
      );
      setInvoices(response.data);
      setTotalPages(response.totalPages);
      setTotalItems(response.total);
    } catch (error) {
      console.error('Failed to load invoices:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async () => {
    if (!deleteId) return;
    setDeleting(true);
    try {
      await invoiceService.delete(deleteId);
      await loadInvoices();
    } catch (error) {
      console.error('Failed to delete invoice:', error);
    } finally {
      setDeleting(false);
      setDeleteId(null);
    }
  };

  const handleDownload = async (invoice: Invoice) => {
    setDownloadingId(invoice.id);
    try {
      await downloadInvoicePDF(invoice);
    } catch (error) {
      console.error('Failed to download PDF:', error);
    } finally {
      setDownloadingId(null);
    }
  };

  const formatDate = (date: Date) => {
    return new Date(date).toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
    });
  };

  const formatCurrency = (amount: number, currency: string) => {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: currency,
    }).format(amount);
  };

  return (
    <div className="animate-fade-in">
      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 mb-6">
        <div>
          <h1 className="text-2xl font-bold text-gray-900 dark:text-white">
            Invoices
          </h1>
          <p className="text-sm mt-1 text-gray-500 dark:text-gray-400">
            Manage your invoices and track payments
          </p>
        </div>
        <Button onClick={() => navigate('/invoices/new')} data-testid="new-invoice-btn">
          <Plus className="w-4 h-4" />
          New Invoice
        </Button>
      </div>

      {/* Filters */}
      <div className="p-4 rounded-xl mb-6 bg-white dark:bg-slate-800 shadow-sm">
        <div className="flex flex-col sm:flex-row gap-4">
          <div className="flex-1">
            <SearchInput
              value={search}
              onChange={(value) => {
                setSearch(value);
                setCurrentPage(1);
              }}
              placeholder="Search invoices..."
            />
          </div>
          <div className="flex items-center gap-2">
            <Filter className="w-4 h-4 text-gray-500 dark:text-gray-400" />
            <select
              value={statusFilter}
              onChange={(e) => {
                setStatusFilter(e.target.value as InvoiceStatus | '');
                setCurrentPage(1);
              }}
              data-testid="status-filter"
              className="px-3 py-2 rounded-lg border transition-colors bg-white border-gray-300 text-gray-900 dark:bg-slate-700 dark:border-slate-600 dark:text-white"
            >
              {statusOptions.map((option) => (
                <option key={option.value} value={option.value}>
                  {option.label}
                </option>
              ))}
            </select>
          </div>
        </div>
      </div>

      {/* Table */}
      <div className="rounded-xl overflow-hidden bg-white dark:bg-slate-800 shadow-sm">
        {loading ? (
          <LoadingSpinner />
        ) : invoices.length === 0 ? (
          <EmptyState
            title="No invoices found"
            description={search || statusFilter ? "Try adjusting your filters" : "Create your first invoice to get started"}
            actionLabel={!search && !statusFilter ? "Create Invoice" : undefined}
            onAction={!search && !statusFilter ? () => navigate('/invoices/new') : undefined}
          />
        ) : (
          <div className="overflow-x-auto">
            <table className="w-full" data-testid="invoices-table">
              <thead>
                <tr className="bg-gray-50 dark:bg-slate-700">
                  <th className="px-6 py-4 text-left text-xs font-semibold uppercase tracking-wider text-gray-600 dark:text-gray-300">
                    Invoice #
                  </th>
                  <th className="px-6 py-4 text-left text-xs font-semibold uppercase tracking-wider text-gray-600 dark:text-gray-300">
                    Customer
                  </th>
                  <th className="px-6 py-4 text-left text-xs font-semibold uppercase tracking-wider text-gray-600 dark:text-gray-300">
                    Date
                  </th>
                  <th className="px-6 py-4 text-left text-xs font-semibold uppercase tracking-wider text-gray-600 dark:text-gray-300">
                    Due Date
                  </th>
                  <th className="px-6 py-4 text-left text-xs font-semibold uppercase tracking-wider text-gray-600 dark:text-gray-300">
                    Amount
                  </th>
                  <th className="px-6 py-4 text-left text-xs font-semibold uppercase tracking-wider text-gray-600 dark:text-gray-300">
                    Status
                  </th>
                  <th className="px-6 py-4 text-right text-xs font-semibold uppercase tracking-wider text-gray-600 dark:text-gray-300">
                    Actions
                  </th>
                </tr>
              </thead>
              <tbody className="divide-y divide-gray-100 dark:divide-slate-700">
                {invoices.map((invoice) => {
                  const customer = getCustomerById(invoice.customerId);
                  return (
                    <tr
                      key={invoice.id}
                      className="table-row-hover transition-colors"
                      data-testid={`invoice-row-${invoice.id}`}
                    >
                      <td className="px-6 py-4 text-gray-900 dark:text-white font-medium">
                        {invoice.invoiceNumber}
                      </td>
                      <td className="px-6 py-4 text-gray-700 dark:text-gray-300">
                        <div>{customer?.name || 'Unknown'}</div>
                        <div className="text-sm text-gray-500 dark:text-gray-400">
                          {customer?.contactPerson}
                        </div>
                      </td>
                      <td className="px-6 py-4 text-gray-700 dark:text-gray-300">
                        {formatDate(invoice.invoiceDate)}
                      </td>
                      <td className="px-6 py-4 text-gray-700 dark:text-gray-300">
                        {formatDate(invoice.dueDate)}
                      </td>
                      <td className="px-6 py-4 text-gray-900 dark:text-white font-medium">
                        {formatCurrency(invoice.total, invoice.currency)}
                      </td>
                      <td className="px-6 py-4">
                        <StatusBadge status={invoice.status} />
                      </td>
                      <td className="px-6 py-4">
                        <div className="flex items-center justify-end gap-1">
                          <Button
                            variant="ghost"
                            size="sm"
                            onClick={() => navigate(`/invoices/${invoice.id}`)}
                            data-testid={`view-invoice-${invoice.id}`}
                            title="View"
                          >
                            <Eye className="w-4 h-4" />
                          </Button>
                          <Button
                            variant="ghost"
                            size="sm"
                            onClick={() => navigate(`/invoices/${invoice.id}/edit`)}
                            data-testid={`edit-invoice-${invoice.id}`}
                            title="Edit"
                          >
                            <Pencil className="w-4 h-4" />
                          </Button>
                          <Button
                            variant="ghost"
                            size="sm"
                            onClick={() => handleDownload(invoice)}
                            loading={downloadingId === invoice.id}
                            data-testid={`download-invoice-${invoice.id}`}
                            title="Download PDF"
                          >
                            <Download className="w-4 h-4" />
                          </Button>
                          <Button
                            variant="ghost"
                            size="sm"
                            onClick={() => setDeleteId(invoice.id)}
                            data-testid={`delete-invoice-${invoice.id}`}
                            title="Delete"
                            className="text-red-500 hover:text-red-600 hover:bg-red-50 dark:hover:bg-red-900/20"
                          >
                            <Trash2 className="w-4 h-4" />
                          </Button>
                        </div>
                      </td>
                    </tr>
                  );
                })}
              </tbody>
            </table>
          </div>
        )}
      </div>

      {/* Pagination */}
      {!loading && invoices.length > 0 && (
        <Pagination
          currentPage={currentPage}
          totalPages={totalPages}
          totalItems={totalItems}
          pageSize={PAGE_SIZE}
          onPageChange={setCurrentPage}
        />
      )}

      {/* Delete Confirmation */}
      <ConfirmDialog
        isOpen={!!deleteId}
        onClose={() => setDeleteId(null)}
        onConfirm={handleDelete}
        title="Delete Invoice"
        message="Are you sure you want to delete this invoice? This action cannot be undone."
        confirmText="Delete"
        loading={deleting}
      />
    </div>
  );
}
