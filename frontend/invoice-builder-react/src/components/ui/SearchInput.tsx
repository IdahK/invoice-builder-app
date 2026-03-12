import { Search, X } from 'lucide-react';

interface SearchInputProps {
  value: string;
  onChange: (value: string) => void;
  placeholder?: string;
}

export default function SearchInput({ value, onChange, placeholder = 'Search...' }: SearchInputProps) {
  return (
    <div className="relative">
      <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-gray-500 dark:text-gray-400" />
      <input
        type="text"
        value={value}
        onChange={(e) => onChange(e.target.value)}
        placeholder={placeholder}
        data-testid="search-input"
        className="w-full pl-10 pr-10 py-2 rounded-lg border transition-colors duration-200 bg-white border-gray-300 text-gray-900 placeholder-gray-400 focus:border-violet-500 focus:ring-1 focus:ring-violet-500 dark:bg-slate-800 dark:border-slate-600 dark:text-white dark:placeholder-gray-400 dark:focus:border-violet-500 dark:focus:ring-1 dark:focus:ring-violet-500"
      />
      {value && (
        <button
          onClick={() => onChange('')}
          className="absolute right-3 top-1/2 -translate-y-1/2 p-1 rounded-full transition-colors hover:bg-gray-100 text-gray-500 dark:hover:bg-slate-700 dark:text-gray-400"
          data-testid="search-clear-btn"
        >
          <X className="w-4 h-4" />
        </button>
      )}
    </div>
  );
}
