import { ReactNode } from 'react';
import { Link, useLocation } from 'react-router-dom';
import { FileText, Users, Building2, Plus, Moon, Sun, Settings } from 'lucide-react';
import { useAppStore } from '../store/appStore';

interface LayoutProps {
  children: ReactNode;
}

export default function Layout({ children }: LayoutProps) {
  const location = useLocation();
  const { darkMode, toggleDarkMode, pdfGenerationMode, setPDFGenerationMode } = useAppStore();

  const navItems = [
    { path: '/invoices', label: 'Invoices', icon: FileText },
    { path: '/customers', label: 'Customers', icon: Users },
    { path: '/senders', label: 'Senders', icon: Building2 },
    { path: '/invoices/new', label: 'New Invoice', icon: Plus },
  ];

  const isActive = (path: string) => {
    if (path === '/invoices/new') {
      return location.pathname === path;
    }
    if (path === '/invoices') {
      return location.pathname === path || location.pathname.startsWith('/invoices/');
    }
    return location.pathname.startsWith(path);
  };

  return (
    <div className="min-h-screen bg-white dark:bg-slate-950 transition-colors duration-200 flex flex-col">
      {/* Header */}
      <header className="sticky top-0 z-50 bg-white dark:bg-slate-900 border-b border-gray-200 dark:border-slate-800 shadow-sm transition-colors duration-200">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex items-center justify-between h-16 gap-4">
            {/* Logo */}
            <Link to="/" className="flex items-center gap-3 shrink-0" data-testid="logo-link">
              <div className="w-10 h-10 bg-gradient-to-br from-violet-500 to-primary-600 rounded-lg flex items-center justify-center shadow-sm hover:shadow-md transition-shadow">
                <span className="text-white font-bold text-lg">IB</span>
              </div>
              <span className="hidden sm:block text-xl font-semibold text-gray-900 dark:text-white">
                Invoice Builder
              </span>
            </Link>

            {/* Navigation */}
            <nav className="hidden md:flex items-center gap-2 flex-initial md:flex-1 justify-center ml-8">
              {navItems.map(({ path, label, icon: Icon }) => (
                <Link
                  key={path}
                  to={path}
                  data-testid={`nav-${label.toLowerCase().replace(' ', '-')}`}
                  className={`flex items-center gap-2 px-4 py-2 rounded-md text-sm font-medium transition-all duration-150 ${
                    isActive(path)
                      ? 'bg-violet-50 text-violet-700 dark:bg-violet-900/30 dark:text-violet-300'
                      : 'text-gray-600 hover:text-gray-900 hover:bg-gray-50 dark:text-gray-400 dark:hover:text-white dark:hover:bg-slate-800'
                  }`}
                >
                  <Icon className="w-4 h-4" />
                  {label}
                </Link>
              ))}
            </nav>

            {/* Actions */}
            <div className="flex items-center gap-2 justify-self-end-safe shrink-0">
              {/* PDF Mode Toggle */}
              <div className="relative group">
                <button
                  data-testid="settings-btn"
                  className="p-2 rounded-md transition-colors duration-150 text-gray-500 hover:text-gray-700 hover:bg-gray-100 dark:text-gray-400 dark:hover:text-white dark:hover:bg-slate-800"
                  title="Settings"
                >
                  <Settings className="w-5 h-5" />
                </button>
                <div className="absolute right-0 mt-2 w-48 py-2 rounded-lg shadow-lg border opacity-0 invisible group-hover:opacity-100 group-hover:visible transition-all duration-200 bg-white border-gray-200 dark:bg-slate-800 dark:border-slate-700">
                  <div className="px-4 py-2 text-xs font-semibold uppercase text-gray-500 dark:text-gray-400">
                    PDF Generation
                  </div>
                  <button
                    data-testid="pdf-mode-client"
                    onClick={() => setPDFGenerationMode('client')}
                    className={`w-full px-4 py-2 text-left text-sm flex items-center justify-between hover:bg-gray-50 dark:hover:bg-slate-700 transition-colors ${
                      pdfGenerationMode === 'client' ? 'text-violet-600' : 'text-gray-700 dark:text-gray-300'
                    }`}
                  >
                    Client-side
                    {pdfGenerationMode === 'client' && <span className="text-violet-600">✓</span>}
                  </button>
                  <button
                    data-testid="pdf-mode-server"
                    onClick={() => setPDFGenerationMode('server')}
                    className={`w-full px-4 py-2 text-left text-sm flex items-center justify-between hover:bg-gray-50 dark:hover:bg-slate-700 transition-colors ${
                      pdfGenerationMode === 'server' ? 'text-violet-600' : 'text-gray-700 dark:text-gray-300'
                    }`}
                  >
                    Server-side
                    {pdfGenerationMode === 'server' && <span className="text-violet-600">✓</span>}
                  </button>
                </div>
              </div>

              {/* Dark Mode Toggle */}
              <button
                onClick={toggleDarkMode}
                data-testid="dark-mode-toggle"
                className="p-2 rounded-md transition-colors duration-150 text-gray-500 hover:text-gray-700 hover:bg-gray-100 dark:text-gray-400 dark:hover:text-white dark:hover:bg-slate-800"
                title={darkMode ? 'Light mode' : 'Dark mode'}
              >
                {darkMode ? <Sun className="w-5 h-5" /> : <Moon className="w-5 h-5" />}
              </button>
            </div>
          </div>
        </div>

        {/* Mobile Navigation */}
        <div className="md:hidden border-t border-gray-200 dark:border-slate-800">
          <div className="flex overflow-x-auto py-2 px-4 gap-2">
            {navItems.map(({ path, label, icon: Icon }) => (
              <Link
                key={path}
                to={path}
                className={`flex items-center gap-2 px-3 py-2 rounded-md text-sm font-medium whitespace-nowrap transition-all duration-150 ${
                  isActive(path)
                    ? 'bg-violet-50 text-violet-700 dark:bg-violet-900/30 dark:text-violet-300'
                    : 'text-gray-600 dark:text-gray-400'
                }`}
              >
                <Icon className="w-4 h-4" />
                {label}
              </Link>
            ))}
          </div>
        </div>
      </header>

      {/* Main Content */}
      <main className="flex-1 max-w-7xl mx-auto w-full px-4 sm:px-6 lg:px-8 py-8">
        {children}
      </main>

      {/* Footer */}
      <footer className="border-t border-gray-200 bg-white dark:border-slate-800 dark:bg-slate-900 mt-auto transition-colors duration-200">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-6">
          <p className="text-center text-sm text-gray-600 dark:text-gray-400">
            Invoice Builder &copy; {new Date().getFullYear()} - Built with React, TypeScript & TailwindCSS
          </p>
        </div>
      </footer>
    </div>
  );
}
