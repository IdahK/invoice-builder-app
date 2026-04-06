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
    <div style={{ display: 'flex', minHeight: '100vh' }} className="bg-gray-50 dark:bg-slate-950">
      {/* Mobile Overlay when sidebar is open */}
      {sidebarOpen && (
        <div 
          style={{ 
            position: 'fixed',
            inset: 0,
            backgroundColor: 'rgba(0, 0, 0, 0.5)',
            zIndex: 30
          }}
          className="hidden md:hidden"
          onClick={() => setSidebarOpen(false)} 
        />
      )}

      {/* Desktop Sidebar */}
      <div style={{ width: '256px', flexShrink: 0 }} className="hidden md:block md:relative md:z-0">
        <Sidebar />
      </div>

      {/* Mobile Sidebar - Overlay */}
      <div 
        style={{
          position: 'fixed',
          top: 0,
          left: 0,
          bottom: 0,
          width: '256px',
          zIndex: 40,
          transform: sidebarOpen ? 'translateX(0)' : 'translateX(-100%)',
          transition: 'transform 0.3s ease-in-out'
        }}
        className="hidden md:hidden"
      >
        <Sidebar />
      </div>

      {/* Main Content Area */}
      <div style={{ flex: 1, display: 'flex', flexDirection: 'column', minHeight: '100vh' }}>
        {/* Header */}
        <Header onSearchChange={onSearchChange} />

        {/* Page Content */}
        <main style={{ flex: 1, overflowY: 'auto' }}>
          <div style={{ padding: '16px' }} className="sm:p-6 lg:p-8">
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
