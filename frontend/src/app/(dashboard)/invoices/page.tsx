'use client';

import { useState } from 'react';
import { Button } from '@/components/ui/button';
import { Plus } from 'lucide-react';
import { InvoiceStatus } from '@/lib/types';
import { InvoicesTable } from '@/components/invoices/invoicesTable';
import { AddInvoiceDialog } from '@/components/invoices/addInvoiceForm';

const STATUS_FILTERS: { label: string; value: InvoiceStatus | 'ALL' }[] = [
  { label: 'All', value: 'ALL' },
  { label: 'Pending', value: 'PENDING' },
  { label: 'Overdue', value: 'OVERDUE' },
  { label: 'Paid', value: 'PAID' },
  { label: 'Cancelled', value: 'CANCELLED' },
];

export default function InvoicesPage() {
  const [dialogOpen, setDialogOpen] = useState(false);
  const [statusFilter, setStatusFilter] = useState<InvoiceStatus | 'ALL'>(
    'ALL',
  );

  return (
    <div className='space-y-6'>
      <div className='flex items-center justify-between'>
        <div>
          <h1 className='text-2xl font-black tracking-tight'>Invoices</h1>
          <p className='text-muted-foreground text-sm mt-1'>
            Manage your invoices
          </p>
        </div>
        <Button
          onClick={() => setDialogOpen(true)}
          className='font-bold cursor-pointer'
        >
          <Plus className='w-4 h-4 mr-2' />
          Create Invoice
        </Button>
      </div>

      <div className='flex gap-1 border border-border rounded p-1 bg-card w-fit'>
        {STATUS_FILTERS.map((f) => (
          <button
            key={f.value}
            onClick={() => setStatusFilter(f.value)}
            className={`px-3 py-1.5 rounded text-sm font-semibold transition-colors cursor-pointer ${
              statusFilter === f.value
                ? 'bg-primary text-primary-foreground'
                : 'text-muted-foreground hover:text-foreground'
            }`}
          >
            {f.label}
          </button>
        ))}
      </div>

      <InvoicesTable
        onAddClick={() => setDialogOpen(true)}
        statusFilter={statusFilter}
      />

      <AddInvoiceDialog
        key={dialogOpen ? 'open' : 'closed'}
        open={dialogOpen}
        onOpenChange={setDialogOpen}
      />
    </div>
  );
}
