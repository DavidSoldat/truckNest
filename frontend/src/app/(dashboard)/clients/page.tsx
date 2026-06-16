'use client';

import { useState } from 'react';
import { Button } from '@/components/ui/button';
import { Plus } from 'lucide-react';
import { Client } from '@/lib/types';
import { ClientsTable } from '@/components/clients/clientsTable';
import { AddClientDialog } from '@/components/clients/addClientForm';


export default function ClientsPage() {
  const [dialogOpen, setDialogOpen] = useState(false);
  const [editTarget, setEditTarget] = useState<Client | null>(null);

  const handleEditClick = (client: Client) => {
    setEditTarget(client);
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
          <h1 className='text-2xl font-black tracking-tight'>Clients</h1>
          <p className='text-muted-foreground text-sm mt-1'>
            Manage your clients
          </p>
        </div>
        <Button onClick={() => setDialogOpen(true)} className='font-bold cursor-pointer'>
          <Plus className='w-4 h-4 mr-2' />
          Add Client
        </Button>
      </div>

      <ClientsTable
        onAddClick={() => setDialogOpen(true)}
        onEditClick={handleEditClick}
      />
      <AddClientDialog
        key={editTarget?.id ?? 'new'}
        open={dialogOpen}
        onOpenChange={handleOpenChange}
        editTarget={editTarget}
      />
    </div>
  );
}