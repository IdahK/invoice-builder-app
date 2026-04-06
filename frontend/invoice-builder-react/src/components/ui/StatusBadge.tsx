import type { InvoiceStatus } from '../../types';

interface StatusBadgeProps {
  status: InvoiceStatus;
  size?: 'sm' | 'md';
}

export default function StatusBadge({ status, size = 'md' }: StatusBadgeProps) {
  const sizes = {
    sm: 'px-2.5 py-1 text-xs',
    md: 'px-3.5 py-1.5 text-sm',
  };

  return (
    <span
      className={`inline-flex items-center font-semibold rounded-md capitalize status-${status} transition-colors duration-150 ${sizes[size]}`}
      data-testid={`status-badge-${status}`}
    >
      {status}
    </span>
  );
}
