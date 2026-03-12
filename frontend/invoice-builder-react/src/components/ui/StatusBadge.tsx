import type { InvoiceStatus } from '../../types';

interface StatusBadgeProps {
  status: InvoiceStatus;
  size?: 'sm' | 'md';
}

export default function StatusBadge({ status, size = 'md' }: StatusBadgeProps) {
  const sizes = {
    sm: 'px-2 py-0.5 text-xs',
    md: 'px-3 py-1 text-sm',
  };

  return (
    <span
      className={`inline-flex items-center font-medium rounded-full capitalize status-${status} ${sizes[size]}`}
      data-testid={`status-badge-${status}`}
    >
      {status}
    </span>
  );
}
