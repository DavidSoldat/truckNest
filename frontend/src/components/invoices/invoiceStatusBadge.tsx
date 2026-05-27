'use client';

import { InvoiceStatus } from '@/lib/types';

interface InvoiceStatusBadgeProps {
  status: InvoiceStatus;
}

const config: Record<InvoiceStatus, { label: string; className: string }> = {
  PENDING: {
    label: 'Pending',
    className: 'bg-yellow-500/10 text-yellow-500 border-yellow-500/20',
  },
  PAID: {
    label: 'Paid',
    className: 'bg-green-500/10 text-green-500 border-green-500/20',
  },
  OVERDUE: {
    label: 'Overdue',
    className: 'bg-destructive/10 text-destructive border-destructive/20',
  },
  CANCELLED: {
    label: 'Cancelled',
    className: 'bg-muted text-muted-foreground border-border',
  },
};

export function InvoiceStatusBadge({ status }: InvoiceStatusBadgeProps) {
  const { label, className } = config[status];
  return (
    <span
      className={`inline-flex items-center px-2 py-0.5 rounded text-xs font-semibold border ${className}`}
    >
      {label}
    </span>
  );
}
