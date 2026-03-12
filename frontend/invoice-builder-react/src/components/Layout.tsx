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
    <div className={`min-h-screen ${darkMode ? 'dark bg-slate-900' : 'bg-gray-50'}`}>
      {/* Header */}
      <header className={`sticky top-0 z-50 ${darkMode ? 'bg-slate-800 border-slate-700' : 'bg-white border-gray-200'} border-b shadow-sm`}>
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex items-center justify-between h-16">
            {/* Logo */}
            <Link to="/" className="flex items-center gap-3" data-testid="logo-link">
              <div className="w-10 h-10 bg-gradient-to-br from-violet-500 to-purple-600 rounded-xl flex items-center justify-center shadow-lg">
                <span className="text-white font-bold text-lg">IB</span>
              </div>
              <span className={`text-xl font-semibold ${darkMode ? 'text-white' : 'text-gray-900'}`}>
                Invoice Builder
              </span>
            </Link>

            {/* Navigation */}
            <nav className="hidden md:flex items-center gap-1">
              {navItems.map(({ path, label, icon: Icon }) => (
                <Link
                  key={path}
                  to={path}
                  data-testid={`nav-${label.toLowerCase().replace(' ', '-')}`}
                  className={`flex items-center gap-2 px-4 py-2 rounded-lg text-sm font-medium transition-all duration-200 ${
                    isActive(path)
                      ? 'bg-violet-100 text-violet-700 dark:bg-violet-900/50 dark:text-violet-300'
                      : `${darkMode ? 'text-gray-300 hover:text-white hover:bg-slate-700' : 'text-gray-600 hover:text-gray-900 hover:bg-gray-100'}`
                  }`}
                >
                  <Icon className="w-4 h-4" />
                  {label}
                </Link>
              ))}
            </nav>

            {/* Actions */}
            <div className="flex items-center gap-2">
              {/* PDF Mode Toggle */}
              <div className="relative group">
                <button
                  data-testid="settings-btn"
                  className={`p-2 rounded-lg transition-colors ${
                    darkMode 
                      ? 'text-gray-400 hover:text-white hover:bg-slate-700' 
                      : 'text-gray-500 hover:text-gray-700 hover:bg-gray-100'
                  }`}
                >
                  <Settings className="w-5 h-5" />
                </button>
                <div className={`absolute right-0 mt-2 w-48 py-2 rounded-lg shadow-lg border opacity-0 invisible group-hover:opacity-100 group-hover:visible transition-all duration-200 ${
                  darkMode ? 'bg-slate-800 border-slate-700' : 'bg-white border-gray-200'
                }`}>
                  <div className={`px-4 py-2 text-xs font-semibold uppercase ${darkMode ? 'text-gray-400' : 'text-gray-500'}`}>
                    PDF Generation
                  </div>
                  <button
                    data-testid="pdf-mode-client"
                    onClick={() => setPDFGenerationMode('client')}
                    className={`w-full px-4 py-2 text-left text-sm flex items-center justify-between ${
                      darkMode ? 'hover:bg-slate-700' : 'hover:bg-gray-100'
                    } ${pdfGenerationMode === 'client' ? 'text-violet-600' : darkMode ? 'text-gray-300' : 'text-gray-700'}`}
                  >
                    Client-side
                    {pdfGenerationMode === 'client' && <span className="text-violet-600">✓</span>}
                  </button>
                  <button
                    data-testid="pdf-mode-server"
                    onClick={() => setPDFGenerationMode('server')}
                    className={`w-full px-4 py-2 text-left text-sm flex items-center justify-between ${
                      darkMode ? 'hover:bg-slate-700' : 'hover:bg-gray-100'
                    } ${pdfGenerationMode === 'server' ? 'text-violet-600' : darkMode ? 'text-gray-300' : 'text-gray-700'}`}
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
                className={`p-2 rounded-lg transition-colors ${
                  darkMode 
                    ? 'text-yellow-400 hover:text-yellow-300 hover:bg-slate-700' 
                    : 'text-gray-500 hover:text-gray-700 hover:bg-gray-100'
                }`}
              >
                {darkMode ? <Sun className="w-5 h-5" /> : <Moon className="w-5 h-5" />}
              </button>
            </div>
          </div>
        </div>

        {/* Mobile Navigation */}
        <div className="md:hidden border-t border-gray-200 dark:border-slate-700">
          <div className="flex overflow-x-auto py-2 px-4 gap-2">
            {navItems.map(({ path, label, icon: Icon }) => (
              <Link
                key={path}
                to={path}
                className={`flex items-center gap-2 px-3 py-2 rounded-lg text-sm font-medium whitespace-nowrap transition-all ${
                  isActive(path)
                    ? 'bg-violet-100 text-violet-700 dark:bg-violet-900/50 dark:text-violet-300'
                    : `${darkMode ? 'text-gray-300' : 'text-gray-600'}`
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
      <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {children}
      </main>

      {/* Footer */}
      <footer className={`border-t ${darkMode ? 'border-slate-700 bg-slate-800' : 'border-gray-200 bg-white'} mt-auto`}>
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-6">
          <p className={`text-center text-sm ${darkMode ? 'text-gray-400' : 'text-gray-500'}`}>
            Invoice Builder &copy; {new Date().getFullYear()} - Built with React, TypeScript & TailwindCSS
          </p>
        </div>
      </footer>
    </div>
  );
}
