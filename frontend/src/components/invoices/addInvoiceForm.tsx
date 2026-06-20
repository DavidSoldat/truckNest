'use client';

import { useState } from 'react';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { toast } from 'sonner';
import { Loader2 } from 'lucide-react';
import api from '@/lib/api';
import { Client, InvoiceRequest } from '@/lib/types';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Textarea } from '@/components/ui/textarea';
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogFooter,
} from '@/components/ui/dialog';
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select';

const emptyForm: InvoiceRequest = {
  clientId: '',
  invoiceNumber: '',
  issueDate: new Date().toISOString().split('T')[0],
  amount: 0,
  notes: '',
};

interface AddInvoiceDialogProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
}

export function AddInvoiceDialog({
  open,
  onOpenChange,
}: AddInvoiceDialogProps) {
  const queryClient = useQueryClient();
  const [form, setForm] = useState<InvoiceRequest>(emptyForm);

  const { data: clients } = useQuery<Client[]>({
    queryKey: ['clients'],
    queryFn: async () => {
      const res = await api.get('/clients');
      return res.data;
    },
  });

  const createMutation = useMutation({
    mutationFn: async (data: InvoiceRequest) => {
      const res = await api.post('/invoices', data);
      return res.data;
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['invoices'] });
      queryClient.invalidateQueries({ queryKey: ['dashboard'] });
      toast.success('Invoice created successfully');
      setForm(emptyForm);
      onOpenChange(false);
    },
    onError: () => toast.error('Failed to create invoice'),
  });

  const handleSubmit = () => {
    if (!form.clientId) {
      toast.error('Client is required');
      return;
    }
    if (!form.invoiceNumber.trim()) {
      toast.error('Invoice number is required');
      return;
    }
    if (!form.amount || form.amount <= 0) {
      toast.error('Amount must be greater than 0');
      return;
    }

    const payload: InvoiceRequest = {
      ...form,
      notes: form.notes || undefined,
    };

    createMutation.mutate(payload);
  };

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className='bg-card border-border max-w-lg'>
        <DialogHeader>
          <DialogTitle className='font-black tracking-tight'>
            Create Invoice
          </DialogTitle>
        </DialogHeader>

        <div className='space-y-4 py-2'>
          <div className='space-y-1.5'>
            <Label className='text-xs uppercase tracking-widest text-muted-foreground cursor-pointer'>
              Client *
            </Label>
            <Select
              value={form.clientId}
              onValueChange={(v) => setForm({ ...form, clientId: v })}
            >
              <SelectTrigger className='bg-secondary border-border'>
                <SelectValue placeholder='Select a client...' />
              </SelectTrigger>
              <SelectContent>
                {clients?.map((client) => (
                  <SelectItem key={client.id} value={client.id}>
                    {client.name}
                  </SelectItem>
                ))}
              </SelectContent>
            </Select>
          </div>

          <div className='grid grid-cols-2 gap-3 '>
            <div className='space-y-1.5'>
              <Label className='text-xs uppercase tracking-widest text-muted-foreground'>
                Invoice Number *
              </Label>
              <Input
                value={form.invoiceNumber}
                onChange={(e) =>
                  setForm({ ...form, invoiceNumber: e.target.value })
                }
                placeholder='INV-2026-001'
                className='bg-secondary border-border'
              />
            </div>
            <div className='space-y-1.5'>
              <Label className='text-xs uppercase tracking-widest text-muted-foreground'>
                Issue Date *
              </Label>
              <Input
                type='date'
                value={form.issueDate}
                onChange={(e) =>
                  setForm({ ...form, issueDate: e.target.value })
                }
                className='bg-secondary border-border'
              />
            </div>
          </div>

          <div className='space-y-1.5'>
            <Label className='text-xs uppercase tracking-widest text-muted-foreground'>
              Amount (€) *
            </Label>
            <Input
              type='number'
              min={0}
              step='0.01'
              value={form.amount || ''}
              onChange={(e) =>
                setForm({ ...form, amount: parseFloat(e.target.value) || 0 })
              }
              placeholder='1500.00'
              className='bg-secondary border-border'
            />
          </div>

          <div className='space-y-1.5'>
            <Label className='text-xs uppercase tracking-widest text-muted-foreground'>
              Notes
            </Label>
            <Textarea
              value={form.notes}
              onChange={(e) => setForm({ ...form, notes: e.target.value })}
              placeholder='Optional notes...'
              className='bg-secondary border-border resize-none'
              rows={3}
            />
          </div>
        </div>

        <DialogFooter>
          <Button
            variant='outline'
            onClick={() => onOpenChange(false)}
            className='border-border cursor-pointer'
          >
            Cancel
          </Button>
          <Button
            onClick={handleSubmit}
            disabled={createMutation.isPending}
            className='font-bold cursor-pointer'
          >
            {createMutation.isPending ? (
              <Loader2 className='w-4 h-4 animate-spin' />
            ) : (
              'Create Invoice'
            )}
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
}
