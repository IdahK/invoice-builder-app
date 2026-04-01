import { useState, useEffect } from 'react';
import { Plus, Eye, Pencil, Trash2, Users } from 'lucide-react';
import { customerService } from '../services/mockApi';
import type { Customer } from '../types';
import Button from '../components/ui/Button';
import Input from '../components/ui/Input';
import TextArea from '../components/ui/TextArea';
import SearchInput from '../components/ui/SearchInput';
import Pagination from '../components/ui/Pagination';
import LoadingSpinner from '../components/ui/LoadingSpinner';
import EmptyState from '../components/ui/EmptyState';
import Modal from '../components/ui/Modal';
import ConfirmDialog from '../components/ui/ConfirmDialog';

const PAGE_SIZE = 10;

interface CustomerFormData {
  name: string;
  contactPerson: string;
  email: string;
  phone: string;
  address: string;
}

const initialFormData: CustomerFormData = {
  name: '',
  contactPerson: '',
  email: '',
  phone: '',
  address: '',
};

export default function CustomersPage() {
  const [customers, setCustomers] = useState<Customer[]>([]);
  const [loading, setLoading] = useState(true);
  const [search, setSearch] = useState('');
  const [currentPage, setCurrentPage] = useState(1);
  const [totalPages, setTotalPages] = useState(1);
  const [totalItems, setTotalItems] = useState(0);
  
  // Modal states
  const [showModal, setShowModal] = useState(false);
  const [modalMode, setModalMode] = useState<'create' | 'edit' | 'view'>('create');
  const [selectedCustomer, setSelectedCustomer] = useState<Customer | null>(null);
  const [formData, setFormData] = useState<CustomerFormData>(initialFormData);
  const [saving, setSaving] = useState(false);
  
  // Delete states
  const [deleteId, setDeleteId] = useState<string | null>(null);
  const [deleting, setDeleting] = useState(false);

  useEffect(() => {
    loadCustomers();
  }, [currentPage, search]);

  const loadCustomers = async () => {
    setLoading(true);
    try {
      const response = await customerService.getAll(currentPage, PAGE_SIZE, search);
      setCustomers(response.data);
      setTotalPages(response.totalPages);
      setTotalItems(response.total);
    } catch (error) {
      console.error('Failed to load customers:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleOpenModal = (mode: 'create' | 'edit' | 'view', customer?: Customer) => {
    setModalMode(mode);
    if (customer) {
      setSelectedCustomer(customer);
      setFormData({
        name: customer.name,
        contactPerson: customer.contactPerson,
        email: customer.email,
        phone: customer.phone,
        address: customer.address,
      });
    } else {
      setSelectedCustomer(null);
      setFormData(initialFormData);
    }
    setShowModal(true);
  };

  const handleCloseModal = () => {
    setShowModal(false);
    setSelectedCustomer(null);
    setFormData(initialFormData);
  };

  const handleSave = async () => {
    setSaving(true);
    try {
      if (modalMode === 'edit' && selectedCustomer) {
        await customerService.update(selectedCustomer.id, formData);
      } else {
        await customerService.create(formData);
      }
      await loadCustomers();
      handleCloseModal();
    } catch (error) {
      console.error('Failed to save customer:', error);
    } finally {
      setSaving(false);
    }
  };

  const handleDelete = async () => {
    if (!deleteId) return;
    setDeleting(true);
    try {
      await customerService.delete(deleteId);
      await loadCustomers();
    } catch (error) {
      console.error('Failed to delete customer:', error);
    } finally {
      setDeleting(false);
      setDeleteId(null);
    }
  };

  const formatDate = (date: Date) => {
    return new Date(date).toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
    });
  };

  return (
    <div className="animate-fade-in">
      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 mb-6">
        <div>
          <h1 className="text-2xl font-bold text-gray-900 dark:text-white">
            Customers
          </h1>
          <p className="text-sm mt-1 text-gray-500 dark:text-gray-400">
            Manage your customer database
          </p>
        </div>
        <Button onClick={() => handleOpenModal('create')} data-testid="new-customer-btn">
          <Plus className="w-4 h-4" />
          Add Customer
        </Button>
      </div>

      {/* Search */}
      <div className="p-4 rounded-xl mb-6 bg-white dark:bg-slate-800 shadow-sm">
        <SearchInput
          value={search}
          onChange={(value) => {
            setSearch(value);
            setCurrentPage(1);
          }}
          placeholder="Search customers..."
        />
      </div>

      {/* Table */}
      <div className="rounded-xl overflow-hidden bg-white dark:bg-slate-800 shadow-sm">
        {loading ? (
          <LoadingSpinner />
        ) : customers.length === 0 ? (
          <EmptyState
            title="No customers found"
            description={search ? "Try adjusting your search" : "Add your first customer to get started"}
            actionLabel={!search ? "Add Customer" : undefined}
            onAction={!search ? () => handleOpenModal('create') : undefined}
            icon={<Users className="w-8 h-8 text-gray-500 dark:text-gray-400" />}
          />
        ) : (
          <div className="overflow-x-auto">
            <table className="w-full" data-testid="customers-table">
              <thead>
                <tr className="bg-gray-50 dark:bg-slate-700">
                  <th className="px-6 py-4 text-left text-xs font-semibold uppercase tracking-wider text-gray-600 dark:text-gray-300">
                    Company
                  </th>
                  <th className="px-6 py-4 text-left text-xs font-semibold uppercase tracking-wider text-gray-600 dark:text-gray-300">
                    Contact
                  </th>
                  <th className="px-6 py-4 text-left text-xs font-semibold uppercase tracking-wider text-gray-600 dark:text-gray-300">
                    Email
                  </th>
                  <th className="px-6 py-4 text-left text-xs font-semibold uppercase tracking-wider text-gray-600 dark:text-gray-300">
                    Phone
                  </th>
                  <th className="px-6 py-4 text-left text-xs font-semibold uppercase tracking-wider text-gray-600 dark:text-gray-300">
                    Created
                  </th>
                  <th className="px-6 py-4 text-right text-xs font-semibold uppercase tracking-wider text-gray-600 dark:text-gray-300">
                    Actions
                  </th>
                </tr>
              </thead>
              <tbody className="divide-y divide-gray-100 dark:divide-slate-700">
                {customers.map((customer) => (
                  <tr
                    key={customer.id}
                    className="table-row-hover transition-colors"
                    data-testid={`customer-row-${customer.id}`}
                  >
                    <td className="px-6 py-4 text-gray-900 dark:text-white font-medium">
                      {customer.name}
                    </td>
                    <td className="px-6 py-4 text-gray-700 dark:text-gray-300">
                      {customer.contactPerson}
                    </td>
                    <td className="px-6 py-4 text-gray-700 dark:text-gray-300">
                      {customer.email}
                    </td>
                    <td className="px-6 py-4 text-gray-700 dark:text-gray-300">
                      {customer.phone}
                    </td>
                    <td className="px-6 py-4 text-gray-700 dark:text-gray-300">
                      {formatDate(customer.createdAt)}
                    </td>
                    <td className="px-6 py-4">
                      <div className="flex items-center justify-end gap-1">
                        <Button
                          variant="ghost"
                          size="sm"
                          onClick={() => handleOpenModal('view', customer)}
                          data-testid={`view-customer-${customer.id}`}
                          title="View"
                        >
                          <Eye className="w-4 h-4" />
                        </Button>
                        <Button
                          variant="ghost"
                          size="sm"
                          onClick={() => handleOpenModal('edit', customer)}
                          data-testid={`edit-customer-${customer.id}`}
                          title="Edit"
                        >
                          <Pencil className="w-4 h-4" />
                        </Button>
                        <Button
                          variant="ghost"
                          size="sm"
                          onClick={() => setDeleteId(customer.id)}
                          data-testid={`delete-customer-${customer.id}`}
                          title="Delete"
                          className="text-red-500 hover:text-red-600 hover:bg-red-50 dark:hover:bg-red-900/20"
                        >
                          <Trash2 className="w-4 h-4" />
                        </Button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>

      {/* Pagination */}
      {!loading && customers.length > 0 && (
        <Pagination
          currentPage={currentPage}
          totalPages={totalPages}
          totalItems={totalItems}
          pageSize={PAGE_SIZE}
          onPageChange={setCurrentPage}
        />
      )}

      {/* Customer Modal */}
      <Modal
        isOpen={showModal}
        onClose={handleCloseModal}
        title={
          modalMode === 'create'
            ? 'Add Customer'
            : modalMode === 'edit'
            ? 'Edit Customer'
            : 'Customer Details'
        }
        size="lg"
        footer={
          modalMode !== 'view' ? (
            <>
              <Button variant="secondary" onClick={handleCloseModal}>
                Cancel
              </Button>
              <Button onClick={handleSave} loading={saving} data-testid="save-customer-btn">
                {modalMode === 'create' ? 'Create' : 'Save Changes'}
              </Button>
            </>
          ) : (
            <Button variant="secondary" onClick={handleCloseModal}>
              Close
            </Button>
          )
        }
      >
        <div className="space-y-4">
          <Input
            label="Company Name"
            value={formData.name}
            onChange={(e) => setFormData({ ...formData, name: e.target.value })}
            disabled={modalMode === 'view'}
            data-testid="customer-name-input"
            placeholder="Enter company name"
          />
          <Input
            label="Contact Person"
            value={formData.contactPerson}
            onChange={(e) => setFormData({ ...formData, contactPerson: e.target.value })}
            disabled={modalMode === 'view'}
            data-testid="customer-contact-input"
            placeholder="Enter contact person name"
          />
          <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
            <Input
              label="Email"
              type="email"
              value={formData.email}
              onChange={(e) => setFormData({ ...formData, email: e.target.value })}
              disabled={modalMode === 'view'}
              data-testid="customer-email-input"
              placeholder="email@example.com"
            />
            <Input
              label="Phone"
              value={formData.phone}
              onChange={(e) => setFormData({ ...formData, phone: e.target.value })}
              disabled={modalMode === 'view'}
              data-testid="customer-phone-input"
              placeholder="+1 555-0000"
            />
          </div>
          <TextArea
            label="Address"
            value={formData.address}
            onChange={(e) => setFormData({ ...formData, address: e.target.value })}
            disabled={modalMode === 'view'}
            data-testid="customer-address-input"
            placeholder="Enter full address"
          />
        </div>
      </Modal>

      {/* Delete Confirmation */}
      <ConfirmDialog
        isOpen={!!deleteId}
        onClose={() => setDeleteId(null)}
        onConfirm={handleDelete}
        title="Delete Customer"
        message="Are you sure you want to delete this customer? This action cannot be undone."
        confirmText="Delete"
        loading={deleting}
      />
    </div>
  );
}
