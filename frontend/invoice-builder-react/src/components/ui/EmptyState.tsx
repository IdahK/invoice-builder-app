import { AlertCircle } from 'lucide-react';
import { useAppStore } from '../../store/appStore';
import Button from './Button';

interface EmptyStateProps {
  title: string;
  description: string;
  actionLabel?: string;
  onAction?: () => void;
  icon?: React.ReactNode;
}

export default function EmptyState({
  title,
  description,
  actionLabel,
  onAction,
  icon,
}: EmptyStateProps) {
  const darkMode = useAppStore((state) => state.darkMode);

  return (
    <div className="flex flex-col items-center justify-center py-12 text-center">
      <div className={`w-16 h-16 rounded-full flex items-center justify-center mb-4 ${
        darkMode ? 'bg-slate-700' : 'bg-gray-100'
      }`}>
        {icon || <AlertCircle className={`w-8 h-8 ${darkMode ? 'text-gray-400' : 'text-gray-500'}`} />}
      </div>
      <h3 className={`text-lg font-semibold mb-2 ${darkMode ? 'text-white' : 'text-gray-900'}`}>
        {title}
      </h3>
      <p className={`text-sm mb-6 max-w-sm ${darkMode ? 'text-gray-400' : 'text-gray-500'}`}>
        {description}
      </p>
      {actionLabel && onAction && (
        <Button onClick={onAction} data-testid="empty-state-action">
          {actionLabel}
        </Button>
      )}
    </div>
  );
}
