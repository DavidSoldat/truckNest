'use client';

import { useState } from 'react';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { toast } from 'sonner';
import { Loader2 } from 'lucide-react';
import api from '@/lib/api';
import {
  TruckRequest,
  TruckResponse,
  TruckStatus,
  EuroStandard,
} from '@/lib/types';
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

const euroStandards: EuroStandard[] = [
  'EURO_1',
  'EURO_2',
  'EURO_3',
  'EURO_4',
  'EURO_5',
  'EURO_6',
];

const emptyForm: TruckRequest = {
  plateNumber: '',
  make: '',
  model: '',
  year: new Date().getFullYear(),
  vin: '',
  nextServiceDate: '',
  status: 'ACTIVE',
  euroStandard: undefined,
  notes: '',
};

function formFromTruck(truck: TruckResponse): TruckRequest {
  return {
    plateNumber: truck.plateNumber,
    make: truck.make,
    model: truck.model,
    year: truck.year,
    vin: truck.vin ?? '',
    nextServiceDate: truck.nextServiceDate ?? '',
    status: truck.status,
    euroStandard: truck.euroStandard ?? undefined,
    notes: truck.notes ?? '',
  };
}

interface AddTruckDialogProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  editTarget?: TruckResponse | null;
}

export function AddTruckDialog({
  open,
  onOpenChange,
  editTarget,
}: AddTruckDialogProps) {
  const queryClient = useQueryClient();
  const isEditing = !!editTarget;

  const [form, setForm] = useState<TruckRequest>(
    editTarget ? formFromTruck(editTarget) : emptyForm,
  );

  const createMutation = useMutation({
    mutationFn: async (data: TruckRequest) => {
      const res = await api.post('/trucks', data);
      return res.data;
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['trucks'] });
      queryClient.invalidateQueries({ queryKey: ['dashboard'] });
      toast.success('Truck added successfully');
      onOpenChange(false);
      setForm(emptyForm);
    },
    onError: () => toast.error('Failed to add truck'),
  });

  const editMutation = useMutation({
    mutationFn: async (data: TruckRequest) => {
      const res = await api.put(`/trucks/${editTarget!.id}`, data);
      return res.data;
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['trucks'] });
      queryClient.invalidateQueries({ queryKey: ['dashboard'] });
      toast.success('Truck updated successfully');
      onOpenChange(false);
    },
    onError: () => toast.error('Failed to update truck'),
  });

  const isPending = createMutation.isPending || editMutation.isPending;

  const handleSubmit = () => {
    const payload: TruckRequest = {
      ...form,
      year: Number(form.year),
      vin: form.vin || undefined,
      nextServiceDate: form.nextServiceDate || undefined,
      notes: form.notes || undefined,
      euroStandard: form.euroStandard || undefined,
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
            {isEditing ? 'Edit Truck' : 'Add New Truck'}
          </DialogTitle>
        </DialogHeader>

        <div className='space-y-4 py-2'>
          <div className='grid grid-cols-2 gap-3'>
            <div className='space-y-1.5'>
              <Label className='text-xs uppercase tracking-widest text-muted-foreground'>
                Plate Number *
              </Label>
              <Input
                value={form.plateNumber}
                onChange={(e) =>
                  setForm({ ...form, plateNumber: e.target.value })
                }
                placeholder='A123-B-456'
                className='bg-secondary border-border'
              />
            </div>
            <div className='space-y-1.5'>
              <Label className='text-xs uppercase tracking-widest text-muted-foreground'>
                VIN
              </Label>
              <Input
                value={form.vin}
                onChange={(e) => setForm({ ...form, vin: e.target.value })}
                placeholder='Optional'
                className='bg-secondary border-border'
              />
            </div>
          </div>

          <div className='grid grid-cols-2 gap-3'>
            <div className='space-y-1.5'>
              <Label className='text-xs uppercase tracking-widest text-muted-foreground'>
                Make *
              </Label>
              <Input
                value={form.make}
                onChange={(e) => setForm({ ...form, make: e.target.value })}
                placeholder='Volvo'
                className='bg-secondary border-border'
              />
            </div>
            <div className='space-y-1.5'>
              <Label className='text-xs uppercase tracking-widest text-muted-foreground'>
                Model *
              </Label>
              <Input
                value={form.model}
                onChange={(e) => setForm({ ...form, model: e.target.value })}
                placeholder='FH16'
                className='bg-secondary border-border'
              />
            </div>
          </div>

          <div className='grid grid-cols-3 gap-3'>
            <div className='space-y-1.5'>
              <Label className='text-xs uppercase tracking-widest text-muted-foreground'>
                Year *
              </Label>
              <Input
                type='number'
                value={form.year}
                onChange={(e) =>
                  setForm({ ...form, year: parseInt(e.target.value) })
                }
                className='bg-secondary border-border'
              />
            </div>
            <div className='space-y-1.5'>
              <Label className='text-xs uppercase tracking-widest text-muted-foreground'>
                Euro Standard
              </Label>
              <Select
                value={form.euroStandard}
                onValueChange={(v) =>
                  setForm({ ...form, euroStandard: v as EuroStandard })
                }
              >
                <SelectTrigger className='bg-secondary border-border'>
                  <SelectValue placeholder='Select' />
                </SelectTrigger>
                <SelectContent>
                  {euroStandards.map((e) => (
                    <SelectItem key={e} value={e}>
                      {e.replace('_', ' ')}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
            </div>
            <div className='space-y-1.5'>
              <Label className='text-xs uppercase tracking-widest text-muted-foreground'>
                Status
              </Label>
              <Select
                value={form.status}
                onValueChange={(v) =>
                  setForm({ ...form, status: v as TruckStatus })
                }
              >
                <SelectTrigger className='bg-secondary border-border'>
                  <SelectValue />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value='ACTIVE'>Active</SelectItem>
                  <SelectItem value='INACTIVE'>Inactive</SelectItem>
                  <SelectItem value='IN_SERVICE'>In Service</SelectItem>
                  <SelectItem value='RETIRED'>Retired</SelectItem>
                </SelectContent>
              </Select>
            </div>
          </div>

          <div className='space-y-1.5'>
            <Label className='text-xs uppercase tracking-widest text-muted-foreground'>
              Next Service Date
            </Label>
            <Input
              type='date'
              value={form.nextServiceDate}
              onChange={(e) =>
                setForm({ ...form, nextServiceDate: e.target.value })
              }
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
            disabled={isPending}
            className='font-bold cursor-pointer'
          >
            {isPending ? (
              <Loader2 className='w-4 h-4 animate-spin' />
            ) : isEditing ? (
              'Save Changes'
            ) : (
              'Add Truck'
            )}
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
}
