import { TextareaHTMLAttributes, forwardRef } from 'react';
import { useAppStore } from '../../store/appStore';

interface TextAreaProps extends TextareaHTMLAttributes<HTMLTextAreaElement> {
  label?: string;
  error?: string;
  helperText?: string;
}

const TextArea = forwardRef<HTMLTextAreaElement, TextAreaProps>(
  ({ className = '', label, error, helperText, id, ...props }, ref) => {
    const darkMode = useAppStore((state) => state.darkMode);
    const textareaId = id || label?.toLowerCase().replace(/\s+/g, '-');

    return (
      <div className="space-y-1">
        {label && (
          <label
            htmlFor={textareaId}
            className={`block text-sm font-medium ${darkMode ? 'text-gray-300' : 'text-gray-700'}`}
          >
            {label}
          </label>
        )}
        <textarea
          ref={ref}
          id={textareaId}
          className={`
            w-full px-3 py-2 rounded-lg border transition-colors duration-200 resize-vertical min-h-[100px]
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

TextArea.displayName = 'TextArea';
export default TextArea;
