import { InputHTMLAttributes, forwardRef } from 'react';
import { useAppStore } from '../../store/appStore';

interface InputProps extends InputHTMLAttributes<HTMLInputElement> {
  label?: string;
  error?: string;
  helperText?: string;
}

const Input = forwardRef<HTMLInputElement, InputProps>(
  ({ className = '', label, error, helperText, id, ...props }, ref) => {
    const darkMode = useAppStore((state) => state.darkMode);
    const inputId = id || label?.toLowerCase().replace(/\s+/g, '-');

    return (
      <div className="space-y-1">
        {label && (
          <label
            htmlFor={inputId}
            className={`block text-sm font-medium ${darkMode ? 'text-gray-300' : 'text-gray-700'}`}
          >
            {label}
          </label>
        )}
        <input
          ref={ref}
          id={inputId}
          className={`
            w-full px-3 py-2 rounded-lg border transition-colors duration-200
            ${darkMode 
              ? 'bg-slate-800 border-slate-600 text-white placeholder-gray-400 focus:border-violet-500 focus:ring-1 focus:ring-violet-500' 
              : 'bg-white border-gray-300 text-gray-900 placeholder-gray-400 focus:border-violet-500 focus:ring-1 focus:ring-violet-500'
            }
            ${error ? 'border-red-500 focus:border-red-500 focus:ring-red-500' : ''}
            ${className}
          `}
          {...props}
        />
        {error && (
          <p className="text-sm text-red-500">{error}</p>
        )}
        {helperText && !error && (
          <p className={`text-sm ${darkMode ? 'text-gray-400' : 'text-gray-500'}`}>{helperText}</p>
        )}
      </div>
    );
  }
);

Input.displayName = 'Input';
export default Input;
