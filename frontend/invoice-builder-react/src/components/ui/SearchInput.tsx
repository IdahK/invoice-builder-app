import { Search, X } from 'lucide-react';
import { useAppStore } from '../../store/appStore';

interface SearchInputProps {
  value: string;
  onChange: (value: string) => void;
  placeholder?: string;
}

export default function SearchInput({ value, onChange, placeholder = 'Search...' }: SearchInputProps) {
  const darkMode = useAppStore((state) => state.darkMode);

  return (
    <div className="relative">
      <Search className={`absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 ${
        darkMode ? 'text-gray-400' : 'text-gray-500'
      }`} />
      <input
        type="text"
        value={value}
        onChange={(e) => onChange(e.target.value)}
        placeholder={placeholder}
        data-testid="search-input"
        className={`
          w-full pl-10 pr-10 py-2 rounded-lg border transition-colors duration-200
          ${darkMode 
            ? 'bg-slate-800 border-slate-600 text-white placeholder-gray-400 focus:border-violet-500 focus:ring-1 focus:ring-violet-500' 
            : 'bg-white border-gray-300 text-gray-900 placeholder-gray-400 focus:border-violet-500 focus:ring-1 focus:ring-violet-500'
          }
        `}
      />
      {value && (
        <button
          onClick={() => onChange('')}
          className={`absolute right-3 top-1/2 -translate-y-1/2 p-1 rounded-full transition-colors ${
            darkMode ? 'hover:bg-slate-700 text-gray-400' : 'hover:bg-gray-100 text-gray-500'
          }`}
          data-testid="search-clear-btn"
        >
          <X className="w-4 h-4" />
        </button>
      )}
    </div>
  );
}
