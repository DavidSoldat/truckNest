'use client';

import { useState } from 'react';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { toast } from 'sonner';
import { Loader2 } from 'lucide-react';
import api from '@/lib/api';
import { Invoice, MarkPaidRequest } from '@/lib/types';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogFooter,
} from '@/components/ui/dialog';

interface MarkPaidDialogProps {
  invoice: Invoice | null;
  open: boolean;
  onOpenChange: (open: boolean) => void;
}

export function MarkPaidDialog({
  invoice,
  open,
  onOpenChange,
}: MarkPaidDialogProps) {
  const queryClient = useQueryClient();
  const [form, setForm] = useState<MarkPaidRequest>({
    paymentDate: new Date().toISOString().split('T')[0],
    amountPaid: invoice?.amount ?? 0,
  });

  const markPaidMutation = useMutation({
    mutationFn: async (data: MarkPaidRequest) => {
      const res = await api.patch(`/invoices/${invoice!.id}/mark-paid`, data);
      return res.data;
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['invoices'] });
      queryClient.invalidateQueries({ queryKey: ['dashboard'] });
      toast.success('Invoice marked as paid');
      onOpenChange(false);
    },
    onError: () => toast.error('Failed to mark invoice as paid'),
  });

  const handleSubmit = () => {
    if (!form.paymentDate) {
      toast.error('Payment date is required');
      return;
    }
    if (!form.amountPaid || form.amountPaid <= 0) {
      toast.error('Amount paid must be greater than 0');
      return;
    }
    markPaidMutation.mutate(form);
  };

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className='bg-card border-border max-w-sm'>
        <DialogHeader>
          <DialogTitle className='font-black tracking-tight'>
            Mark as Paid
          </DialogTitle>
        </DialogHeader>

        <div className='space-y-4 py-2'>
          <p className='text-sm text-muted-foreground'>
            Invoice{' '}
            <span className='font-semibold text-foreground'>
              {invoice?.invoiceNumber}
            </span>
          </p>

          <div className='space-y-1.5'>
            <Label className='text-xs uppercase tracking-widest text-muted-foreground'>
              Payment Date *
            </Label>
            <Input
              type='date'
              value={form.paymentDate}
              onChange={(e) =>
                setForm({ ...form, paymentDate: e.target.value })
              }
              className='bg-secondary border-border'
            />
          </div>

          <div className='space-y-1.5'>
            <Label className='text-xs uppercase tracking-widest text-muted-foreground'>
              Amount Paid (€) *
            </Label>
            <Input
              type='number'
              min={0}
              step='0.01'
              value={form.amountPaid}
              onChange={(e) =>
                setForm({
                  ...form,
                  amountPaid: parseFloat(e.target.value) || 0,
                })
              }
              className='bg-secondary border-border'
            />
          </div>
        </div>

        <DialogFooter>
          <Button
            variant='outline'
            onClick={() => onOpenChange(false)}
            className='border-border'
          >
            Cancel
          </Button>
          <Button
            onClick={handleSubmit}
            disabled={markPaidMutation.isPending}
            className='font-bold'
          >
            {markPaidMutation.isPending ? (
              <Loader2 className='w-4 h-4 animate-spin' />
            ) : (
              'Confirm Payment'
            )}
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
}
