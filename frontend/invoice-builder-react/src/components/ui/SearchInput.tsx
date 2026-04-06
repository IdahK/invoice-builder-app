import { Search, X } from 'lucide-react';

interface SearchInputProps {
  value: string;
  onChange: (value: string) => void;
  placeholder?: string;
}

export default function SearchInput({ value, onChange, placeholder = 'Search...' }: SearchInputProps) {
  return (
    <div className="relative group w-full">
      <Search className="absolute left-3.5 top-1/2 -translate-y-1/2 w-4 h-4 text-gray-400 dark:text-gray-500 transition-colors group-focus-within:text-violet-500 dark:group-focus-within:text-violet-400 pointer-events-none" />
      <input
        type="text"
        value={value}
        onChange={(e) => onChange(e.target.value)}
        placeholder={placeholder}
        data-testid="search-input"
        className="
          w-full pl-10 pr-10 py-2.5 text-sm rounded-md
          border border-gray-300 dark:border-slate-600
          bg-white dark:bg-slate-800
          text-gray-900 dark:text-white
          placeholder-gray-500 dark:placeholder-gray-400
          shadow-sm
          transition-all duration-150
          focus:outline-none
          focus:border-violet-500 dark:focus:border-violet-500
          focus:ring-2 focus:ring-violet-500/20
          hover:border-gray-400 dark:hover:border-slate-500
        "
      />
      {value && (
        <button
          onClick={() => onChange('')}
          className="absolute right-2.5 top-1/2 -translate-y-1/2 p-1 rounded-md transition-colors text-gray-400 hover:text-gray-600 hover:bg-gray-100 dark:text-gray-500 dark:hover:text-gray-300 dark:hover:bg-slate-700"
          data-testid="search-clear-btn"
          aria-label="Clear search"
        >
          <X className="w-3.5 h-3.5" />
        </button>
      )}
    </div>
  );
}
