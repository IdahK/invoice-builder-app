import { ReactNode, useState } from 'react';
import Sidebar from './Sidebar';
import Header from './Header';

interface LayoutProps {
  children: ReactNode;
  onSearchChange?: (query: string) => void;
}

export default function Layout({ children, onSearchChange }: LayoutProps) {
  const [sidebarOpen, setSidebarOpen] = useState(true);

  return (
    <div className="min-h-screen bg-gray-50 dark:bg-slate-950 transition-colors duration-200 flex">
      {/* Mobile Overlay when sidebar is open */}
      {sidebarOpen && (
        <div className="fixed inset-0 bg-black/50 z-30 md:hidden" onClick={() => setSidebarOpen(false)} />
      )}

      {/* Sidebar */}
      <div className={`
        hidden md:block md:w-64 md:flex-shrink-0 md:relative md:z-0
      `}>
        <Sidebar />
      </div>

      {/* Mobile Sidebar - Overlay */}
      <div className={`
        fixed inset-y-0 left-0 w-64 z-40 md:hidden transition-transform duration-300 ease-in-out
        ${sidebarOpen ? 'translate-x-0' : '-translate-x-full'}
      `}>
        <Sidebar />
      </div>

      {/* Main Content Area */}
      <div className="flex-1 flex flex-col min-h-screen">
        {/* Header */}
        <Header onSearchChange={onSearchChange} />

        {/* Page Content */}
        <main className="flex-1 overflow-auto">
          <div className="p-4 sm:p-6 lg:p-8">
            {children}
          </div>
        </main>

        {/* Footer */}
        <footer className="border-t border-gray-200 bg-white dark:border-slate-800 dark:bg-slate-900 transition-colors duration-200">
          <div className="px-4 sm:px-6 lg:px-8 py-6">
            <p className="text-center text-sm text-gray-600 dark:text-gray-400">
              Invoice Builder &copy; {new Date().getFullYear()} - Built with React, TypeScript & TailwindCSS
            </p>
          </div>
        </footer>
      </div>
    </div>
  );
}
