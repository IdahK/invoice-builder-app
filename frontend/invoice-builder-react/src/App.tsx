import { Routes, Route, Navigate } from 'react-router-dom';
import { useEffect } from 'react';
import { useAppStore } from './store/appStore';
import Layout from './components/Layout';
import InvoicesPage from './pages/InvoicesPage';
import CustomersPage from './pages/CustomersPage';
import SendersPage from './pages/SendersPage';
import NewInvoicePage from './pages/NewInvoicePage';
import EditInvoicePage from './pages/EditInvoicePage';
import ViewInvoicePage from './pages/ViewInvoicePage';

function App() {
  const darkMode = useAppStore((state) => state.darkMode);

  useEffect(() => {
    if (darkMode) {
      document.documentElement.classList.add('dark');
    } else {
      document.documentElement.classList.remove('dark');
    }
  }, [darkMode]);

  return (
    <Layout>
      <Routes>
        <Route path="/" element={<Navigate to="/invoices" replace />} />
        <Route path="/invoices" element={<InvoicesPage />} />
        <Route path="/invoices/new" element={<NewInvoicePage />} />
        <Route path="/invoices/:id/edit" element={<EditInvoicePage />} />
        <Route path="/invoices/:id" element={<ViewInvoicePage />} />
        <Route path="/customers" element={<CustomersPage />} />
        <Route path="/senders" element={<SendersPage />} />
      </Routes>
    </Layout>
  );
}

export default App;
