import { useState, useEffect } from 'react';
import { Plus, Eye, Pencil, Trash2, Building2 } from 'lucide-react';
import { senderService } from '../services/mockApi';
import type { Sender } from '../types';
import { useAppStore } from '../store/appStore';
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

interface SenderFormData {
  companyName: string;
  contactPerson: string;
  email: string;
  phone: string;
  address: string;
  bankDetails: string;
}

const initialFormData: SenderFormData = {
  companyName: '',
  contactPerson: '',
  email: '',
  phone: '',
  address: '',
  bankDetails: '',
};

export default function SendersPage() {
  const darkMode = useAppStore((state) => state.darkMode);
  const [senders, setSenders] = useState<Sender[]>([]);
  const [loading, setLoading] = useState(true);
  const [search, setSearch] = useState('');
  const [currentPage, setCurrentPage] = useState(1);
  const [totalPages, setTotalPages] = useState(1);
  const [totalItems, setTotalItems] = useState(0);
  
  // Modal states
  const [showModal, setShowModal] = useState(false);
  const [modalMode, setModalMode] = useState<'create' | 'edit' | 'view'>('create');
  const [selectedSender, setSelectedSender] = useState<Sender | null>(null);
  const [formData, setFormData] = useState<SenderFormData>(initialFormData);
  const [saving, setSaving] = useState(false);
  
  // Delete states
  const [deleteId, setDeleteId] = useState<string | null>(null);
  const [deleting, setDeleting] = useState(false);

  useEffect(() => {
    loadSenders();
  }, [currentPage, search]);

  const loadSenders = async () => {
    setLoading(true);
    try {
      const response = await senderService.getAll(currentPage, PAGE_SIZE, search);
      setSenders(response.data);
      setTotalPages(response.totalPages);
      setTotalItems(response.total);
    } catch (error) {
      console.error('Failed to load senders:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleOpenModal = (mode: 'create' | 'edit' | 'view', sender?: Sender) => {
    setModalMode(mode);
    if (sender) {
      setSelectedSender(sender);
      setFormData({
        companyName: sender.companyName,
        contactPerson: sender.contactPerson,
        email: sender.email,
        phone: sender.phone,
        address: sender.address,
        bankDetails: sender.bankDetails || '',
      });
    } else {
      setSelectedSender(null);
      setFormData(initialFormData);
    }
    setShowModal(true);
  };

  const handleCloseModal = () => {
    setShowModal(false);
    setSelectedSender(null);
    setFormData(initialFormData);
  };

  const handleSave = async () => {
    setSaving(true);
    try {
      if (modalMode === 'edit' && selectedSender) {
        await senderService.update(selectedSender.id, formData);
      } else {
        await senderService.create(formData);
      }
      await loadSenders();
      handleCloseModal();
    } catch (error) {
      console.error('Failed to save sender:', error);
    } finally {
      setSaving(false);
    }
  };

  const handleDelete = async () => {
    if (!deleteId) return;
    setDeleting(true);
    try {
      await senderService.delete(deleteId);
      await loadSenders();
    } catch (error) {
      console.error('Failed to delete sender:', error);
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
          <h1 className={`text-2xl font-bold ${darkMode ? 'text-white' : 'text-gray-900'}`}>
            Senders
          </h1>
          <p className={`text-sm mt-1 ${darkMode ? 'text-gray-400' : 'text-gray-500'}`}>
            Manage your company profiles for invoices
          </p>
        </div>
        <Button onClick={() => handleOpenModal('create')} data-testid="new-sender-btn">
          <Plus className="w-4 h-4" />
          Add Sender
        </Button>
      </div>

      {/* Search */}
      <div className={`p-4 rounded-xl mb-6 ${darkMode ? 'bg-slate-800' : 'bg-white'} shadow-sm`}>
        <SearchInput
          value={search}
          onChange={(value) => {
            setSearch(value);
            setCurrentPage(1);
          }}
          placeholder="Search senders..."
        />
      </div>

      {/* Table */}
      <div className={`rounded-xl overflow-hidden ${darkMode ? 'bg-slate-800' : 'bg-white'} shadow-sm`}>
        {loading ? (
          <LoadingSpinner />
        ) : senders.length === 0 ? (
          <EmptyState
            title="No senders found"
            description={search ? "Try adjusting your search" : "Add your first sender profile to get started"}
            actionLabel={!search ? "Add Sender" : undefined}
            onAction={!search ? () => handleOpenModal('create') : undefined}
            icon={<Building2 className={`w-8 h-8 ${darkMode ? 'text-gray-400' : 'text-gray-500'}`} />}
          />
        ) : (
          <div className="overflow-x-auto">
            <table className="w-full" data-testid="senders-table">
              <thead>
                <tr className={darkMode ? 'bg-slate-700' : 'bg-gray-50'}>
                  <th className={`px-6 py-4 text-left text-xs font-semibold uppercase tracking-wider ${darkMode ? 'text-gray-300' : 'text-gray-600'}`}>
                    Company
                  </th>
                  <th className={`px-6 py-4 text-left text-xs font-semibold uppercase tracking-wider ${darkMode ? 'text-gray-300' : 'text-gray-600'}`}>
                    Contact
                  </th>
                  <th className={`px-6 py-4 text-left text-xs font-semibold uppercase tracking-wider ${darkMode ? 'text-gray-300' : 'text-gray-600'}`}>
                    Email
                  </th>
                  <th className={`px-6 py-4 text-left text-xs font-semibold uppercase tracking-wider ${darkMode ? 'text-gray-300' : 'text-gray-600'}`}>
                    Phone
                  </th>
                  <th className={`px-6 py-4 text-left text-xs font-semibold uppercase tracking-wider ${darkMode ? 'text-gray-300' : 'text-gray-600'}`}>
                    Created
                  </th>
                  <th className={`px-6 py-4 text-right text-xs font-semibold uppercase tracking-wider ${darkMode ? 'text-gray-300' : 'text-gray-600'}`}>
                    Actions
                  </th>
                </tr>
              </thead>
              <tbody className={`divide-y ${darkMode ? 'divide-slate-700' : 'divide-gray-100'}`}>
                {senders.map((sender) => (
                  <tr
                    key={sender.id}
                    className="table-row-hover transition-colors"
                    data-testid={`sender-row-${sender.id}`}
                  >
                    <td className={`px-6 py-4 ${darkMode ? 'text-white' : 'text-gray-900'} font-medium`}>
                      {sender.companyName}
                    </td>
                    <td className={`px-6 py-4 ${darkMode ? 'text-gray-300' : 'text-gray-700'}`}>
                      {sender.contactPerson}
                    </td>
                    <td className={`px-6 py-4 ${darkMode ? 'text-gray-300' : 'text-gray-700'}`}>
                      {sender.email}
                    </td>
                    <td className={`px-6 py-4 ${darkMode ? 'text-gray-300' : 'text-gray-700'}`}>
                      {sender.phone}
                    </td>
                    <td className={`px-6 py-4 ${darkMode ? 'text-gray-300' : 'text-gray-700'}`}>
                      {formatDate(sender.createdAt)}
                    </td>
                    <td className="px-6 py-4">
                      <div className="flex items-center justify-end gap-1">
                        <Button
                          variant="ghost"
                          size="sm"
                          onClick={() => handleOpenModal('view', sender)}
                          data-testid={`view-sender-${sender.id}`}
                          title="View"
                        >
                          <Eye className="w-4 h-4" />
                        </Button>
                        <Button
                          variant="ghost"
                          size="sm"
                          onClick={() => handleOpenModal('edit', sender)}
                          data-testid={`edit-sender-${sender.id}`}
                          title="Edit"
                        >
                          <Pencil className="w-4 h-4" />
                        </Button>
                        <Button
                          variant="ghost"
                          size="sm"
                          onClick={() => setDeleteId(sender.id)}
                          data-testid={`delete-sender-${sender.id}`}
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
      {!loading && senders.length > 0 && (
        <Pagination
          currentPage={currentPage}
          totalPages={totalPages}
          totalItems={totalItems}
          pageSize={PAGE_SIZE}
          onPageChange={setCurrentPage}
        />
      )}

      {/* Sender Modal */}
      <Modal
        isOpen={showModal}
        onClose={handleCloseModal}
        title={
          modalMode === 'create'
            ? 'Add Sender'
            : modalMode === 'edit'
            ? 'Edit Sender'
            : 'Sender Details'
        }
        size="lg"
        footer={
          modalMode !== 'view' ? (
            <>
              <Button variant="secondary" onClick={handleCloseModal}>
                Cancel
              </Button>
              <Button onClick={handleSave} loading={saving} data-testid="save-sender-btn">
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
            value={formData.companyName}
            onChange={(e) => setFormData({ ...formData, companyName: e.target.value })}
            disabled={modalMode === 'view'}
            data-testid="sender-company-input"
            placeholder="Enter company name"
          />
          <Input
            label="Contact Person"
            value={formData.contactPerson}
            onChange={(e) => setFormData({ ...formData, contactPerson: e.target.value })}
            disabled={modalMode === 'view'}
            data-testid="sender-contact-input"
            placeholder="Enter contact person name"
          />
          <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
            <Input
              label="Email"
              type="email"
              value={formData.email}
              onChange={(e) => setFormData({ ...formData, email: e.target.value })}
              disabled={modalMode === 'view'}
              data-testid="sender-email-input"
              placeholder="email@example.com"
            />
            <Input
              label="Phone"
              value={formData.phone}
              onChange={(e) => setFormData({ ...formData, phone: e.target.value })}
              disabled={modalMode === 'view'}
              data-testid="sender-phone-input"
              placeholder="+1 555-0000"
            />
          </div>
          <TextArea
            label="Address"
            value={formData.address}
            onChange={(e) => setFormData({ ...formData, address: e.target.value })}
            disabled={modalMode === 'view'}
            data-testid="sender-address-input"
            placeholder="Enter full address"
          />
          <TextArea
            label="Bank Details"
            value={formData.bankDetails}
            onChange={(e) => setFormData({ ...formData, bankDetails: e.target.value })}
            disabled={modalMode === 'view'}
            data-testid="sender-bank-input"
            placeholder="Enter bank account details"
            helperText="Optional - Include bank name, account number, etc."
          />
        </div>
      </Modal>

      {/* Delete Confirmation */}
      <ConfirmDialog
        isOpen={!!deleteId}
        onClose={() => setDeleteId(null)}
        onConfirm={handleDelete}
        title="Delete Sender"
        message="Are you sure you want to delete this sender? This action cannot be undone."
        confirmText="Delete"
        loading={deleting}
      />
    </div>
  );
}
