'use client';

import { useState } from 'react';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { toast } from 'sonner';
import { Loader2 } from 'lucide-react';
import api from '@/lib/api';
import { DriverRequest, DriverResponse, DriverStatus } from '@/lib/types';
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

const emptyForm: DriverRequest = {
  firstName: '',
  lastName: '',
  dateOfBirth: '',
  phone: '',
  email: '',
  licenseNumber: '',
  licenseExpiry: '',
  visaExpiry: '',
  status: 'ACTIVE',
  monthlySalary: undefined,
  notes: '',
};

function formFromDriver(driver: DriverResponse): DriverRequest {
  return {
    firstName: driver.firstName,
    lastName: driver.lastName,
    dateOfBirth: driver.dateOfBirth ?? '',
    phone: driver.phone ?? '',
    email: driver.email ?? '',
    licenseNumber: driver.licenseNumber ?? '',
    licenseExpiry: driver.licenseExpiry ?? '',
    visaExpiry: driver.visaExpiry ?? '',
    status: driver.status,
    monthlySalary: driver.monthlySalary ?? undefined,
    notes: driver.notes ?? '',
  };
}

interface AddDriverDialogProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  editTarget?: DriverResponse | null;
}

export function AddDriverDialog({
  open,
  onOpenChange,
  editTarget,
}: AddDriverDialogProps) {
  const queryClient = useQueryClient();
  const isEditing = !!editTarget;
  const [form, setForm] = useState<DriverRequest>(
    editTarget ? formFromDriver(editTarget) : emptyForm,
  );

  const createMutation = useMutation({
    mutationFn: async (data: DriverRequest) => {
      const res = await api.post('/drivers', data);
      return res.data;
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['drivers'] });
      queryClient.invalidateQueries({ queryKey: ['dashboard'] });
      toast.success('Driver added successfully');
      onOpenChange(false);
    },
    onError: () => toast.error('Failed to add driver'),
  });

  const editMutation = useMutation({
    mutationFn: async (data: DriverRequest) => {
      const res = await api.put(`/drivers/${editTarget!.id}`, data);
      return res.data;
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['drivers'] });
      queryClient.invalidateQueries({ queryKey: ['dashboard'] });
      toast.success('Driver updated successfully');
      onOpenChange(false);
    },
    onError: () => toast.error('Failed to update driver'),
  });

  const isPending = createMutation.isPending || editMutation.isPending;

  const handleSubmit = () => {
    const payload: DriverRequest = {
      ...form,
      dateOfBirth: form.dateOfBirth || undefined,
      phone: form.phone || undefined,
      email: form.email || undefined,
      licenseNumber: form.licenseNumber || undefined,
      licenseExpiry: form.licenseExpiry || undefined,
      visaExpiry: form.visaExpiry || undefined,
      notes: form.notes || undefined,
      monthlySalary: form.monthlySalary || undefined,
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
            {isEditing ? 'Edit Driver' : 'Add New Driver'}
          </DialogTitle>
        </DialogHeader>

        <div className='space-y-4 py-2'>
          <div className='grid grid-cols-2 gap-3'>
            <div className='space-y-1.5'>
              <Label className='text-xs uppercase tracking-widest text-muted-foreground'>
                First Name *
              </Label>
              <Input
                value={form.firstName}
                onChange={(e) =>
                  setForm({ ...form, firstName: e.target.value })
                }
                placeholder='Marko'
                className='bg-secondary border-border'
              />
            </div>
            <div className='space-y-1.5'>
              <Label className='text-xs uppercase tracking-widest text-muted-foreground'>
                Last Name *
              </Label>
              <Input
                value={form.lastName}
                onChange={(e) => setForm({ ...form, lastName: e.target.value })}
                placeholder='Petrović'
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
                placeholder='+387 61 123 456'
                className='bg-secondary border-border'
              />
            </div>
            <div className='space-y-1.5'>
              <Label className='text-xs uppercase tracking-widest text-muted-foreground'>
                Email
              </Label>
              <Input
                value={form.email}
                onChange={(e) => setForm({ ...form, email: e.target.value })}
                placeholder='marko@example.com'
                className='bg-secondary border-border'
              />
            </div>
          </div>

          <div className='grid grid-cols-2 gap-3'>
            <div className='space-y-1.5'>
              <Label className='text-xs uppercase tracking-widest text-muted-foreground'>
                Date of Birth
              </Label>
              <Input
                type='date'
                value={form.dateOfBirth}
                onChange={(e) =>
                  setForm({ ...form, dateOfBirth: e.target.value })
                }
                className='bg-secondary border-border'
              />
            </div>
            <div className='space-y-1.5'>
              <Label className='text-xs uppercase tracking-widest text-muted-foreground'>
                Monthly Salary (€)
              </Label>
              <Input
                type='number'
                value={form.monthlySalary ?? ''}
                onChange={(e) =>
                  setForm({
                    ...form,
                    monthlySalary: parseFloat(e.target.value),
                  })
                }
                placeholder='1200'
                className='bg-secondary border-border'
              />
            </div>
          </div>

          <div className='grid grid-cols-2 gap-3'>
            <div className='space-y-1.5'>
              <Label className='text-xs uppercase tracking-widest text-muted-foreground'>
                License Number
              </Label>
              <Input
                value={form.licenseNumber}
                onChange={(e) =>
                  setForm({ ...form, licenseNumber: e.target.value })
                }
                placeholder='BA123456'
                className='bg-secondary border-border'
              />
            </div>
            <div className='space-y-1.5'>
              <Label className='text-xs uppercase tracking-widest text-muted-foreground'>
                License Expiry
              </Label>
              <Input
                type='date'
                value={form.licenseExpiry}
                onChange={(e) =>
                  setForm({ ...form, licenseExpiry: e.target.value })
                }
                className='bg-secondary border-border'
              />
            </div>
          </div>

          <div className='grid grid-cols-2 gap-3'>
            <div className='space-y-1.5'>
              <Label className='text-xs uppercase tracking-widest text-muted-foreground'>
                Visa Expiry
              </Label>
              <Input
                type='date'
                value={form.visaExpiry}
                onChange={(e) =>
                  setForm({ ...form, visaExpiry: e.target.value })
                }
                className='bg-secondary border-border'
              />
            </div>
            <div className='space-y-1.5'>
              <Label className='text-xs uppercase tracking-widest text-muted-foreground cursor-pointer'>
                Status
              </Label>
              <Select
                value={form.status}
                onValueChange={(v) =>
                  setForm({ ...form, status: v as DriverStatus })
                }
              >
                <SelectTrigger className='bg-secondary border-border'>
                  <SelectValue />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value='ACTIVE'>Active</SelectItem>
                  <SelectItem value='INACTIVE'>Inactive</SelectItem>
                  <SelectItem value='ON_LEAVE'>On Leave</SelectItem>
                  <SelectItem value='TERMINATED'>Terminated</SelectItem>
                </SelectContent>
              </Select>
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
            className='border-border cursor-pointer'
          >
            Cancel
          </Button>
          <Button
            onClick={handleSubmit}
            disabled={isPending}
            className='font-bold cursor-pointer'
          >
            {isPending ? (
              <Loader2 className='w-4 h-4 animate-spin' />
            ) : isEditing ? (
              'Save Changes'
            ) : (
              'Add Driver'
            )}
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
}
