import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Plus, Eye, Pencil, Trash2, Download, Filter } from 'lucide-react';
import { invoiceService, getCustomerById } from '../services/mockApi';
import { downloadInvoicePDF } from '../services/pdfService';
import type { Invoice, InvoiceStatus } from '../types';
import Button from '../components/ui/Button';
import SearchInput from '../components/ui/SearchInput';
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

const statusConfig: Record<string, { dot: string; badge: string; label: string }> = {
  paid: {
    dot: 'bg-green-500',
    badge: 'bg-green-50 text-green-800 dark:bg-green-900/30 dark:text-green-300',
    label: 'Paid',
  },
  sent: {
    dot: 'bg-blue-500',
    badge: 'bg-blue-50 text-blue-800 dark:bg-blue-900/30 dark:text-blue-300',
    label: 'Sent',
  },
  overdue: {
    dot: 'bg-red-500',
    badge: 'bg-red-50 text-red-800 dark:bg-red-900/30 dark:text-red-300',
    label: 'Overdue',
  },
  draft: {
    dot: 'bg-gray-400',
    badge: 'bg-gray-100 text-gray-600 dark:bg-slate-700 dark:text-gray-400 border border-gray-200 dark:border-slate-600',
    label: 'Draft',
  },
  cancelled: {
    dot: 'bg-amber-500',
    badge: 'bg-amber-50 text-amber-800 dark:bg-amber-900/30 dark:text-amber-300',
    label: 'Cancelled',
  },
};

function StatusBadge({ status }: { status: string }) {
  const config = statusConfig[status] ?? statusConfig.draft;
  return (
    <span className={`inline-flex items-center gap-1.5 px-2.5 py-1 rounded-full text-xs font-medium ${config.badge}`}>
      <span className={`w-1.5 h-1.5 rounded-full shrink-0 ${config.dot}`} />
      {config.label}
    </span>
  );
}

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

  const formatDate = (date: Date) =>
    new Date(date).toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
    });

  const formatCurrency = (amount: number, currency: string) =>
    new Intl.NumberFormat('en-US', { style: 'currency', currency }).format(amount);

  const iconBtnClass =
    'p-2 rounded-md text-gray-400 hover:text-gray-700 hover:bg-gray-100 dark:hover:text-gray-200 dark:hover:bg-slate-600 transition-colors';

  return (
    <div className="animate-fade-in">

      {/* ── Page header ── */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-3 mb-6">
        <div>
          <h1 className="text-2xl font-semibold text-gray-900 dark:text-white">Invoices</h1>
          <p className="text-sm mt-1 text-gray-500 dark:text-gray-400">
            Manage your invoices and track payments
          </p>
        </div>
        <Button
          onClick={() => navigate('/invoices/new')}
          data-testid="new-invoice-btn"
          className="w-full sm:w-auto justify-center"
        >
          <Plus className="w-4 h-4" />
          New Invoice
        </Button>
      </div>

      {/* ── Filters ── */}
      <div className="p-3 rounded-xl mb-5 bg-white dark:bg-slate-800 border border-gray-100 dark:border-slate-700 shadow-sm">
        <div className="flex flex-col sm:flex-row gap-2">
          <div className="flex-1 min-w-0">
            <SearchInput
              value={search}
              onChange={(value) => { setSearch(value); setCurrentPage(1); }}
              placeholder="Search invoices..."
            />
          </div>
          <div className="flex items-center gap-2 w-full sm:w-auto">
            <Filter className="w-4 h-4 text-gray-400 dark:text-gray-500 shrink-0" />
            <select
              value={statusFilter}
              onChange={(e) => { setStatusFilter(e.target.value as InvoiceStatus | ''); setCurrentPage(1); }}
              data-testid="status-filter"
              className="flex-1 sm:flex-none px-3 py-2 rounded-lg border text-sm bg-white border-gray-200 text-gray-700 dark:bg-slate-700 dark:border-slate-600 dark:text-white focus:outline-none focus:ring-2 focus:ring-violet-500/20 hover:border-gray-300 dark:hover:border-slate-500 transition-colors"
            >
              {statusOptions.map((o) => (
                <option key={o.value} value={o.value}>{o.label}</option>
              ))}
            </select>
          </div>
        </div>
      </div>

      {/* ── Table card ── */}
      <div className="rounded-xl overflow-hidden bg-white dark:bg-slate-800 border border-gray-100 dark:border-slate-700 shadow-sm">
        {loading ? (
          <LoadingSpinner />
        ) : invoices.length === 0 ? (
          <EmptyState
            title="No invoices found"
            description={search || statusFilter ? 'Try adjusting your filters' : 'Create your first invoice to get started'}
            actionLabel={!search && !statusFilter ? 'Create Invoice' : undefined}
            onAction={!search && !statusFilter ? () => navigate('/invoices/new') : undefined}
          />
        ) : (
          <>
            {/* ── Desktop table (md+) ── */}
            <div className="hidden md:block overflow-x-auto">
              <table
                className="w-full border-collapse"
                style={{ minWidth: '720px' }}
                data-testid="invoices-table"
              >
                {/*
                  Column widths add up to 100%.
                  Invoice#: 16%  Customer: 22%  Date: 13%  Due: 13%  Amount: 13%  Status: 11%  Actions: 12%
                */}
                <colgroup>
                  <col style={{ width: '16%' }} />
                  <col style={{ width: '22%' }} />
                  <col style={{ width: '13%' }} />
                  <col style={{ width: '13%' }} />
                  <col style={{ width: '13%' }} />
                  <col style={{ width: '11%' }} />
                  <col style={{ width: '12%' }} />
                </colgroup>

                <thead>
                  <tr className="bg-gray-50 dark:bg-slate-700/60 border-b border-gray-100 dark:border-slate-700">
                    {['Invoice #', 'Customer', 'Date', 'Due Date', 'Amount', 'Status'].map((h) => (
                      <th
                        key={h}
                        className="px-6 py-4 text-left text-[11px] font-semibold uppercase tracking-wider text-gray-400 dark:text-gray-500"
                      >
                        {h}
                      </th>
                    ))}
                    <th className="px-6 py-4 text-right text-[11px] font-semibold uppercase tracking-wider text-gray-400 dark:text-gray-500">
                      Actions
                    </th>
                  </tr>
                </thead>

                <tbody>
                  {invoices.map((invoice, i) => {
                    const customer = getCustomerById(invoice.customerId);
                    return (
                      <tr
                        key={invoice.id}
                        className={`hover:bg-gray-50/80 dark:hover:bg-slate-700/30 transition-colors duration-100 ${
                          i !== invoices.length - 1 ? 'border-b border-gray-100 dark:border-slate-700/60' : ''
                        }`}
                        data-testid={`invoice-row-${invoice.id}`}
                      >
                        {/* Invoice # */}
                        <td className="px-6 py-4">
                          <span className="font-mono text-sm font-medium text-gray-900 dark:text-white">
                            {invoice.invoiceNumber}
                          </span>
                        </td>

                        {/* Customer */}
                        <td className="px-6 py-4">
                          <p className="text-sm font-medium text-gray-900 dark:text-white leading-snug">
                            {customer?.name || 'Unknown'}
                          </p>
                          {customer?.contactPerson && (
                            <p className="text-xs text-gray-400 dark:text-gray-500 mt-0.5">
                              {customer.contactPerson}
                            </p>
                          )}
                        </td>

                        {/* Date */}
                        <td className="px-6 py-4 text-sm text-gray-500 dark:text-gray-400 whitespace-nowrap">
                          {formatDate(invoice.invoiceDate)}
                        </td>

                        {/* Due Date */}
                        <td className="px-6 py-4 text-sm text-gray-500 dark:text-gray-400 whitespace-nowrap">
                          {formatDate(invoice.dueDate)}
                        </td>

                        {/* Amount */}
                        <td className="px-6 py-4">
                          <span className="text-sm font-semibold text-gray-900 dark:text-white tabular-nums whitespace-nowrap">
                            {formatCurrency(invoice.total, invoice.currency)}
                          </span>
                        </td>

                        {/* Status */}
                        <td className="px-6 py-4">
                          <StatusBadge status={invoice.status} />
                        </td>

                        {/* Actions */}
                        <td className="px-6 py-4">
                          <div className="flex items-center justify-end gap-0.5">
                            <button
                              onClick={() => navigate(`/invoices/${invoice.id}`)}
                              title="View"
                              data-testid={`view-invoice-${invoice.id}`}
                              className={iconBtnClass}
                            >
                              <Eye className="w-4 h-4" />
                            </button>
                            <button
                              onClick={() => navigate(`/invoices/${invoice.id}/edit`)}
                              title="Edit"
                              data-testid={`edit-invoice-${invoice.id}`}
                              className={iconBtnClass}
                            >
                              <Pencil className="w-4 h-4" />
                            </button>
                            <button
                              onClick={() => handleDownload(invoice)}
                              disabled={downloadingId === invoice.id}
                              title="Download PDF"
                              data-testid={`download-invoice-${invoice.id}`}
                              className={`${iconBtnClass} disabled:opacity-40`}
                            >
                              <Download className="w-4 h-4" />
                            </button>
                            <button
                              onClick={() => setDeleteId(invoice.id)}
                              title="Delete"
                              data-testid={`delete-invoice-${invoice.id}`}
                              className="p-2 rounded-md text-gray-400 hover:text-red-600 hover:bg-red-50 dark:hover:text-red-400 dark:hover:bg-red-900/20 transition-colors"
                            >
                              <Trash2 className="w-4 h-4" />
                            </button>
                          </div>
                        </td>
                      </tr>
                    );
                  })}
                </tbody>
              </table>
            </div>

            {/* ── Mobile card list (< md) ── */}
            <ul className="md:hidden divide-y divide-gray-100 dark:divide-slate-700">
              {invoices.map((invoice) => {
                const customer = getCustomerById(invoice.customerId);
                return (
                  <li
                    key={invoice.id}
                    className="p-4 hover:bg-gray-50/80 dark:hover:bg-slate-700/30 transition-colors"
                    data-testid={`invoice-row-${invoice.id}`}
                  >
                    {/* Invoice number + status */}
                    <div className="flex items-center justify-between gap-2 mb-2">
                      <span className="font-mono text-sm font-medium text-gray-900 dark:text-white">
                        {invoice.invoiceNumber}
                      </span>
                      <StatusBadge status={invoice.status} />
                    </div>

                    {/* Customer */}
                    <div className="mb-3">
                      <p className="text-sm font-medium text-gray-900 dark:text-white">
                        {customer?.name || 'Unknown'}
                      </p>
                      {customer?.contactPerson && (
                        <p className="text-xs text-gray-400 dark:text-gray-500 mt-0.5">
                          {customer.contactPerson}
                        </p>
                      )}
                    </div>

                    {/* Dates + amount */}
                    <div className="flex flex-wrap items-center gap-x-4 gap-y-1 mb-3 text-xs text-gray-500 dark:text-gray-400">
                      <span>
                        <span className="text-gray-400 dark:text-gray-500">Issued </span>
                        {formatDate(invoice.invoiceDate)}
                      </span>
                      <span>
                        <span className="text-gray-400 dark:text-gray-500">Due </span>
                        {formatDate(invoice.dueDate)}
                      </span>
                      <span className="ml-auto text-sm font-semibold tabular-nums text-gray-900 dark:text-white">
                        {formatCurrency(invoice.total, invoice.currency)}
                      </span>
                    </div>

                    {/* Actions */}
                    <div className="flex items-center gap-1 pt-3 border-t border-gray-100 dark:border-slate-700">
                      <button
                        onClick={() => navigate(`/invoices/${invoice.id}`)}
                        data-testid={`view-invoice-${invoice.id}`}
                        className={`${iconBtnClass} flex items-center gap-1.5 text-xs px-2.5`}
                      >
                        <Eye className="w-3.5 h-3.5" /> View
                      </button>
                      <button
                        onClick={() => navigate(`/invoices/${invoice.id}/edit`)}
                        data-testid={`edit-invoice-${invoice.id}`}
                        className={`${iconBtnClass} flex items-center gap-1.5 text-xs px-2.5`}
                      >
                        <Pencil className="w-3.5 h-3.5" /> Edit
                      </button>
                      <button
                        onClick={() => handleDownload(invoice)}
                        disabled={downloadingId === invoice.id}
                        data-testid={`download-invoice-${invoice.id}`}
                        className={`${iconBtnClass} flex items-center gap-1.5 text-xs px-2.5 disabled:opacity-40`}
                      >
                        <Download className="w-3.5 h-3.5" /> PDF
                      </button>
                      <button
                        onClick={() => setDeleteId(invoice.id)}
                        data-testid={`delete-invoice-${invoice.id}`}
                        className="ml-auto p-2 rounded-md text-gray-400 hover:text-red-600 hover:bg-red-50 dark:hover:text-red-400 dark:hover:bg-red-900/20 transition-colors"
                      >
                        <Trash2 className="w-4 h-4" />
                      </button>
                    </div>
                  </li>
                );
              })}
            </ul>
          </>
        )}

        {/* Pagination */}
        {!loading && invoices.length > 0 && (
          <div className="border-t border-gray-100 dark:border-slate-700 bg-gray-50/60 dark:bg-slate-700/30 px-6 py-3">
            <Pagination
              currentPage={currentPage}
              totalPages={totalPages}
              totalItems={totalItems}
              pageSize={PAGE_SIZE}
              onPageChange={setCurrentPage}
            />
          </div>
        )}
      </div>

      {/* Delete confirmation */}
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