'use client';

import { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import {
  Loader2,
  FileText,
  MoreHorizontal,
  CheckCircle,
  XCircle,
  Bell,
} from 'lucide-react';
import { Button } from '@/components/ui/button';
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from '@/components/ui/table';
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu';
import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle,
} from '@/components/ui/alert-dialog';
import api from '@/lib/api';
import { Client, Invoice, InvoiceStatus } from '@/lib/types';
import { toast } from 'sonner';
import { InvoiceStatusBadge } from './invoiceStatusBadge';
import { MarkPaidDialog } from './markPaidDialog';

interface InvoicesTableProps {
  onAddClick: () => void;
  statusFilter: InvoiceStatus | 'ALL';
}

const formatDate = (date: string | null) => {
  if (!date) return '—';
  return new Date(date).toLocaleDateString('en-GB', {
    day: '2-digit',
    month: 'short',
    year: 'numeric',
  });
};

const formatCurrency = (amount: number | null) => {
  if (amount == null) return '—';
  return new Intl.NumberFormat('de-DE', {
    style: 'currency',
    currency: 'EUR',
  }).format(amount);
};

export function InvoicesTable({
  onAddClick,
  statusFilter,
}: InvoicesTableProps) {
  const queryClient = useQueryClient();
  const [cancelTarget, setCancelTarget] = useState<Invoice | null>(null);
  const [markPaidTarget, setMarkPaidTarget] = useState<Invoice | null>(null);

  const { data: clients } = useQuery<Client[]>({
    queryKey: ['clients'],
    queryFn: async () => {
      const res = await api.get('/clients');
      return res.data;
    },
  });

  const clientName = (clientId: string) =>
    clients?.find((c) => c.id === clientId)?.name ?? '—';

  const { data: invoices, isLoading } = useQuery<Invoice[]>({
    queryKey: ['invoices', statusFilter],
    queryFn: async () => {
      const params = statusFilter !== 'ALL' ? `?status=${statusFilter}` : '';
      const res = await api.get(`/invoices${params}`);
      return res.data;
    },
  });

  const cancelMutation = useMutation({
    mutationFn: async (id: string) => {
      const res = await api.patch(`/invoices/${id}/cancel`);
      return res.data;
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['invoices'] });
      queryClient.invalidateQueries({ queryKey: ['dashboard'] });
      toast.success('Invoice cancelled');
      setCancelTarget(null);
    },
    onError: () => toast.error('Failed to cancel invoice'),
  });

  const reminderMutation = useMutation({
    mutationFn: async (id: string) => {
      await api.post(`/invoices/${id}/send-reminder`);
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['invoices'] });
      toast.success('Reminder sent');
    },
    onError: () => toast.error('Failed to send reminder'),
  });

  if (isLoading) {
    return (
      <div className='flex items-center justify-center h-48 bg-card border border-border rounded'>
        <Loader2 className='w-5 h-5 animate-spin text-primary' />
      </div>
    );
  }

  if (invoices?.length === 0) {
    return (
      <div className='flex flex-col items-center justify-center h-48 gap-3 bg-card border border-border rounded'>
        <FileText className='w-8 h-8 text-muted-foreground' />
        <p className='text-muted-foreground text-sm'>No invoices yet</p>
        <Button variant='outline' size='sm' onClick={onAddClick}>
          Create your first invoice
        </Button>
      </div>
    );
  }

  return (
    <>
      <div className='border border-border rounded bg-card'>
        <Table>
          <TableHeader>
            <TableRow className='border-border hover:bg-transparent'>
              <TableHead className='text-xs uppercase tracking-widest text-muted-foreground'>
                Invoice #
              </TableHead>
              <TableHead className='text-xs uppercase tracking-widest text-muted-foreground'>
                Client
              </TableHead>
              <TableHead className='text-xs uppercase tracking-widest text-muted-foreground'>
                Issue Date
              </TableHead>
              <TableHead className='text-xs uppercase tracking-widest text-muted-foreground'>
                Due Date
              </TableHead>
              <TableHead className='text-xs uppercase tracking-widest text-muted-foreground'>
                Amount
              </TableHead>
              <TableHead className='text-xs uppercase tracking-widest text-muted-foreground'>
                Status
              </TableHead>
              <TableHead className='text-xs uppercase tracking-widest text-muted-foreground'>
                Paid
              </TableHead>
              <TableHead />
            </TableRow>
          </TableHeader>
          <TableBody>
            {invoices?.map((invoice) => (
              <TableRow
                key={invoice.id}
                className='border-border hover:bg-secondary/50'
              >
                <TableCell className='font-semibold'>
                  {invoice.invoiceNumber}
                </TableCell>
                <TableCell className='text-muted-foreground'>
                  {clientName(invoice.clientId)}
                </TableCell>
                <TableCell className='text-muted-foreground'>
                  {formatDate(invoice.issueDate)}
                </TableCell>
                <TableCell className='text-muted-foreground'>
                  {formatDate(invoice.dueDate)}
                </TableCell>
                <TableCell className='font-semibold'>
                  {formatCurrency(invoice.amount)}
                </TableCell>
                <TableCell>
                  <InvoiceStatusBadge status={invoice.status} />
                </TableCell>
                <TableCell className='text-muted-foreground'>
                  {invoice.amountPaid != null
                    ? formatCurrency(invoice.amountPaid)
                    : '—'}
                </TableCell>
                <TableCell className='w-10'>
                  <DropdownMenu>
                    <DropdownMenuTrigger asChild>
                      <Button
                        variant='ghost'
                        size='icon'
                        className='h-8 w-8 text-muted-foreground hover:text-foreground'
                        onClick={(e) => e.stopPropagation()}
                      >
                        <MoreHorizontal className='w-4 h-4' />
                      </Button>
                    </DropdownMenuTrigger>
                    <DropdownMenuContent
                      align='end'
                      className='bg-card border-border'
                    >
                      {(invoice.status === 'PENDING' ||
                        invoice.status === 'OVERDUE') && (
                        <DropdownMenuItem
                          className='cursor-pointer'
                          onClick={() => setMarkPaidTarget(invoice)}
                        >
                          <CheckCircle className='w-4 h-4 mr-2' />
                          Mark as Paid
                        </DropdownMenuItem>
                      )}

                      {(invoice.status === 'PENDING' ||
                        invoice.status === 'OVERDUE') && (
                        <DropdownMenuItem
                          className='cursor-pointer'
                          onClick={() => reminderMutation.mutate(invoice.id)}
                          disabled={reminderMutation.isPending}
                        >
                          <Bell className='w-4 h-4 mr-2' />
                          Send Reminder
                        </DropdownMenuItem>
                      )}

                      {(invoice.status === 'PENDING' ||
                        invoice.status === 'OVERDUE') && (
                        <DropdownMenuSeparator />
                      )}

                      {(invoice.status === 'PENDING' ||
                        invoice.status === 'OVERDUE') && (
                        <DropdownMenuItem
                          className='cursor-pointer text-destructive focus:text-destructive'
                          onClick={() => setCancelTarget(invoice)}
                        >
                          <XCircle className='w-4 h-4 mr-2' />
                          Cancel Invoice
                        </DropdownMenuItem>
                      )}

                      {(invoice.status === 'PAID' ||
                        invoice.status === 'CANCELLED') && (
                        <DropdownMenuItem
                          disabled
                          className='text-muted-foreground'
                        >
                          No actions available
                        </DropdownMenuItem>
                      )}
                    </DropdownMenuContent>
                  </DropdownMenu>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </div>

      <MarkPaidDialog
        key={markPaidTarget?.id ?? 'none'}
        invoice={markPaidTarget}
        open={!!markPaidTarget}
        onOpenChange={(open) => {
          if (!open) setMarkPaidTarget(null);
        }}
      />

      <AlertDialog
        open={!!cancelTarget}
        onOpenChange={(open) => !open && setCancelTarget(null)}
      >
        <AlertDialogContent className='bg-card border-border'>
          <AlertDialogHeader>
            <AlertDialogTitle className='font-black'>
              Cancel Invoice
            </AlertDialogTitle>
            <AlertDialogDescription>
              Are you sure you want to cancel invoice{' '}
              <span className='font-bold text-foreground'>
                {cancelTarget?.invoiceNumber}
              </span>
              ? This action cannot be undone.
            </AlertDialogDescription>
          </AlertDialogHeader>
          <AlertDialogFooter>
            <AlertDialogCancel className='border-border'>
              Cancel
            </AlertDialogCancel>
            <AlertDialogAction
              className='bg-destructive text-destructive-foreground hover:bg-destructive/90'
              onClick={() =>
                cancelTarget && cancelMutation.mutate(cancelTarget.id)
              }
            >
              {cancelMutation.isPending ? (
                <Loader2 className='w-4 h-4 animate-spin' />
              ) : (
                'Cancel Invoice'
              )}
            </AlertDialogAction>
          </AlertDialogFooter>
        </AlertDialogContent>
      </AlertDialog>
    </>
  );
}
