import { TextareaHTMLAttributes, forwardRef } from 'react';

interface TextAreaProps extends TextareaHTMLAttributes<HTMLTextAreaElement> {
  label?: string;
  error?: string;
  helperText?: string;
}

const TextArea = forwardRef<HTMLTextAreaElement, TextAreaProps>(
  ({ className = '', label, error, helperText, id, ...props }, ref) => {
    const textareaId = id || label?.toLowerCase().replace(/\s+/g, '-');

    return (
      <div className="space-y-2">
        {label && (
          <label
            htmlFor={textareaId}
            className="block text-sm font-medium text-gray-700 dark:text-gray-300"
          >
            {label}
          </label>
        )}
        <textarea
          ref={ref}
          id={textareaId}
          className={`
            w-full px-4 py-2.5 rounded-md border transition-all duration-200 resize-vertical min-h-[120px] text-base
            bg-white border-gray-300 text-gray-900 placeholder-gray-500 focus:border-violet-500 focus:ring-2 focus:ring-violet-500/20 focus:outline-none
            dark:bg-slate-800 dark:border-slate-600 dark:text-white dark:placeholder-gray-400 dark:focus:border-violet-500 dark:focus:ring-2 dark:focus:ring-violet-500/30
            ${error ? 'border-red-500 focus:border-red-500 focus:ring-red-500/20' : ''}
            ${className}
          `}
          {...props}
        />
        {error && (
          <p className="text-sm text-red-600 dark:text-red-400">{error}</p>
        )}
        {helperText && !error && (
          <p className="text-sm text-gray-600 dark:text-gray-400">{helperText}</p>
        )}
      </div>
    );
  }
);

TextArea.displayName = 'TextArea';
export default TextArea;
