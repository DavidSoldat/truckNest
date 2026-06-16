'use client';

import { useState } from 'react';
import { Button } from '@/components/ui/button';
import { Plus } from 'lucide-react';
import { TruckResponse } from '@/lib/types';
import { TrucksTable } from '@/components/trucks/trucksTable';
import { AddTruckDialog } from '@/components/trucks/addTruckForm';

export default function TrucksPage() {
  const [dialogOpen, setDialogOpen] = useState(false);
  const [editTarget, setEditTarget] = useState<TruckResponse | null>(null);

  const handleEditClick = (truck: TruckResponse) => {
    setEditTarget(truck);
    setDialogOpen(true);
  };

  const handleOpenChange = (open: boolean) => {
    setDialogOpen(open);
    if (!open) setEditTarget(null);
  };

  return (
    <div className='space-y-6'>
      <div className='flex items-center justify-between'>
        <div>
          <h1 className='text-2xl font-black tracking-tight'>Trucks</h1>
          <p className='text-muted-foreground text-sm mt-1'>
            Manage your fleet
          </p>
        </div>
        <Button
          onClick={() => setDialogOpen(true)}
          className='font-bold cursor-pointer'
        >
          <Plus className='w-4 h-4 mr-2' />
          Add Truck
        </Button>
      </div>

      <TrucksTable
        onAddClick={() => setDialogOpen(true)}
        onEditClick={handleEditClick}
      />
      <AddTruckDialog
        key={editTarget?.id ?? 'new'}
        open={dialogOpen}
        onOpenChange={handleOpenChange}
        editTarget={editTarget}
      />
    </div>
  );
}
