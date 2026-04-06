import { Link, useLocation } from 'react-router-dom';
import { FileText, Users, Building2, Settings, LogOut, HelpCircle, Plus, LogIn, UserPlus } from 'lucide-react';

interface SidebarProps {
  onLogout?: () => void;
  isAuthenticated?: boolean;
}

export default function Sidebar({ onLogout, isAuthenticated = true }: SidebarProps) {
  const location = useLocation();

  const mainNavItems = [
    { path: '/invoices', label: 'Invoices', icon: FileText },
    { path: '/customers', label: 'Customers', icon: Users },
    { path: '/senders', label: 'Senders', icon: Building2 },
  ];

  const isActive = (path: string) => {
    if (path === '/invoices') {
      return location.pathname === path || location.pathname.startsWith('/invoices/');
    }
    return location.pathname.startsWith(path);
  };

  const mainNavClasses = (path: string) => {
    const base = 'flex items-center gap-3 px-4 py-3 rounded-lg transition-all duration-200 relative';
    const active = isActive(path)
      ? 'bg-violet-600 text-white font-medium'
      : 'text-gray-600 dark:text-gray-400 hover:text-gray-900 dark:hover:text-white hover:bg-gray-100 dark:hover:bg-slate-800';
    return `${base} ${active}`;
  };

  return (
    <div className="flex flex-col h-screen w-64 bg-white dark:bg-slate-900 border-r border-gray-200 dark:border-slate-800 transition-colors duration-200">
      {/* Header with Logo */}
      <div className="p-4 border-b border-gray-200 dark:border-slate-800">
        <Link to="/" className="flex items-center gap-2">
          <div className="w-10 h-10 bg-gradient-to-br from-violet-600 to-violet-700 rounded-lg flex items-center justify-center flex-shrink-0">
            <span className="text-white font-bold text-lg">IB</span>
          </div>
          <div>
            <div className="text-sm font-bold text-gray-900 dark:text-white">Invoice Builder</div>
            <div className="text-xs text-gray-500 dark:text-gray-400 uppercase tracking-wider">Management</div>
          </div>
        </Link>
      </div>

      {/* New Invoice Button */}
      <div className="px-4 py-4 border-b border-gray-200 dark:border-slate-800">
        <Link
          to="/invoices/new"
          className="flex items-center justify-center gap-2 w-full bg-violet-600 hover:bg-violet-700 active:bg-violet-800 text-white font-medium py-3 px-4 rounded-full transition-all duration-200 hover:shadow-lg active:scale-95 shadow-md"
        >
          <Plus className="w-5 h-5" />
          + New Invoice
        </Link>
      </div>

      {/* Main Navigation */}
      <nav className="flex-1 overflow-y-auto p-4 space-y-2">
        {mainNavItems.map((item) => (
          <Link
            key={item.path}
            to={item.path}
            className={mainNavClasses(item.path)}
          >
            <item.icon className="w-5 h-5 flex-shrink-0" />
            <span className="text-sm font-medium">{item.label}</span>
          </Link>
        ))}
      </nav>

      {/* Bottom Navigation */}
      <div className="border-t border-gray-200 dark:border-slate-800 p-4 space-y-2">
        <Link
          to="/settings"
          className={mainNavClasses('/settings')}
        >
          <Settings className="w-5 h-5 flex-shrink-0" />
          <span className="text-sm font-medium">Settings</span>
        </Link>

        <a
          href="#help"
          className="flex items-center gap-3 px-4 py-3 rounded-lg text-gray-600 dark:text-gray-400 hover:text-gray-900 dark:hover:text-white hover:bg-gray-100 dark:hover:bg-slate-800 transition-all duration-200 cursor-pointer"
        >
          <HelpCircle className="w-5 h-5 flex-shrink-0" />
          <span className="text-sm font-medium">Help</span>
        </a>

        {isAuthenticated ? (
          <button
            onClick={onLogout}
            className="w-full flex items-center gap-3 px-4 py-3 rounded-lg text-red-600 dark:text-red-400 hover:bg-red-50 dark:hover:bg-red-900/20 transition-all duration-200 border-0 bg-transparent font-medium"
          >
            <LogOut className="w-5 h-5 flex-shrink-0" />
            <span className="text-sm font-medium">Logout</span>
          </button>
        ) : (
          <>
            <Link
              to="/login"
              className="flex items-center justify-center gap-2 px-4 py-3 rounded-lg text-gray-600 dark:text-gray-400 hover:bg-gray-100 dark:hover:bg-slate-800 transition-all duration-200"
            >
              <LogIn className="w-5 h-5" />
              <span className="text-sm font-medium">Login</span>
            </Link>
            <Link
              to="/register"
              className="flex items-center justify-center gap-2 px-4 py-3 rounded-lg bg-violet-100 dark:bg-violet-900/30 text-violet-600 dark:text-violet-400 hover:bg-violet-200 dark:hover:bg-violet-900/50 transition-all duration-200"
            >
              <UserPlus className="w-5 h-5" />
              <span className="text-sm font-medium">Register</span>
            </Link>
          </>
        )}
      </div>
    </div>
  );
}
