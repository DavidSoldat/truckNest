'use client';

import { useState } from 'react';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { toast } from 'sonner';
import { Loader2 } from 'lucide-react';
import api from '@/lib/api';
import { Client, ClientRequest } from '@/lib/types';
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

const emptyForm: ClientRequest = {
  name: '',
  contactPerson: '',
  contactEmail: '',
  phone: '',
  paymentTermsDays: 30,
  notes: '',
};

function formFromClient(client: Client): ClientRequest {
  return {
    name: client.name,
    contactPerson: client.contactPerson ?? '',
    contactEmail: client.contactEmail ?? '',
    phone: client.phone ?? '',
    paymentTermsDays: client.paymentTermsDays,
    notes: client.notes ?? '',
  };
}

interface AddClientDialogProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  editTarget?: Client | null;
}

export function AddClientDialog({
  open,
  onOpenChange,
  editTarget,
}: AddClientDialogProps) {
  const queryClient = useQueryClient();
  const isEditing = !!editTarget;
  const [form, setForm] = useState<ClientRequest>(
    editTarget ? formFromClient(editTarget) : emptyForm,
  );

  const createMutation = useMutation({
    mutationFn: async (data: ClientRequest) => {
      const res = await api.post('/clients', data);
      return res.data;
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['clients'] });
      queryClient.invalidateQueries({ queryKey: ['dashboard'] });
      toast.success('Client added successfully');
      onOpenChange(false);
    },
    onError: () => toast.error('Failed to add client'),
  });

  const editMutation = useMutation({
    mutationFn: async (data: ClientRequest) => {
      const res = await api.put(`/clients/${editTarget!.id}`, data);
      return res.data;
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['clients'] });
      queryClient.invalidateQueries({ queryKey: ['dashboard'] });
      toast.success('Client updated successfully');
      onOpenChange(false);
    },
    onError: () => toast.error('Failed to update client'),
  });

  const isPending = createMutation.isPending || editMutation.isPending;

  const handleSubmit = () => {
    if (!form.name.trim()) {
      toast.error('Client name is required');
      return;
    }

    const payload: ClientRequest = {
      ...form,
      contactPerson: form.contactPerson || undefined,
      contactEmail: form.contactEmail || undefined,
      phone: form.phone || undefined,
      notes: form.notes || undefined,
    };

    if (isEditing) {
      editMutation.mutate(payload);
    } else {
      createMutation.mutate(payload);
    }
  };

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className='bg-card border-border max-w-lg'>
        <DialogHeader>
          <DialogTitle className='font-black tracking-tight'>
            {isEditing ? 'Edit Client' : 'Add New Client'}
          </DialogTitle>
        </DialogHeader>

        <div className='space-y-4 py-2'>
          <div className='space-y-1.5'>
            <Label className='text-xs uppercase tracking-widest text-muted-foreground'>
              Company / Client Name *
            </Label>
            <Input
              value={form.name}
              onChange={(e) => setForm({ ...form, name: e.target.value })}
              placeholder='Acme d.o.o.'
              className='bg-secondary border-border'
            />
          </div>

          <div className='grid grid-cols-2 gap-3'>
            <div className='space-y-1.5'>
              <Label className='text-xs uppercase tracking-widest text-muted-foreground'>
                Contact Person
              </Label>
              <Input
                value={form.contactPerson}
                onChange={(e) =>
                  setForm({ ...form, contactPerson: e.target.value })
                }
                placeholder='Ime Prezime'
                className='bg-secondary border-border'
              />
            </div>
            <div className='space-y-1.5'>
              <Label className='text-xs uppercase tracking-widest text-muted-foreground'>
                Email
              </Label>
              <Input
                type='email'
                value={form.contactEmail}
                onChange={(e) =>
                  setForm({ ...form, contactEmail: e.target.value })
                }
                placeholder='contact@example.com'
                className='bg-secondary border-border'
              />
            </div>
          </div>

          <div className='grid grid-cols-2 gap-3'>
            <div className='space-y-1.5'>
              <Label className='text-xs uppercase tracking-widest text-muted-foreground'>
                Phone
              </Label>
              <Input
                value={form.phone}
                onChange={(e) => setForm({ ...form, phone: e.target.value })}
                placeholder='+387 61 000 000'
                className='bg-secondary border-border'
              />
            </div>
            <div className='space-y-1.5'>
              <Label className='text-xs uppercase tracking-widest text-muted-foreground'>
                Payment Terms (days) *
              </Label>
              <Input
                type='number'
                min={0}
                value={form.paymentTermsDays}
                onChange={(e) =>
                  setForm({
                    ...form,
                    paymentTermsDays: parseInt(e.target.value) || 0,
                  })
                }
                placeholder='30'
                className='bg-secondary border-border'
              />
            </div>
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
            className='border-border'
          >
            Cancel
          </Button>
          <Button
            onClick={handleSubmit}
            disabled={isPending}
            className='font-bold'
          >
            {isPending ? (
              <Loader2 className='w-4 h-4 animate-spin' />
            ) : isEditing ? (
              'Save Changes'
            ) : (
              'Add Client'
            )}
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
}
