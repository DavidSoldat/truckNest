'use client';

import { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { Loader2, Users, MoreHorizontal, Pencil, Trash2 } from 'lucide-react';
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
import { Client } from '@/lib/types';
import { toast } from 'sonner';

interface ClientsTableProps {
  onAddClick: () => void;
  onEditClick: (client: Client) => void;
}

export function ClientsTable({ onAddClick, onEditClick }: ClientsTableProps) {
  const queryClient = useQueryClient();
  const [deleteTarget, setDeleteTarget] = useState<Client | null>(null);

  const { data: clients, isLoading } = useQuery<Client[]>({
    queryKey: ['clients'],
    queryFn: async () => {
      const res = await api.get('/clients');
      return res.data;
    },
  });

  const deleteMutation = useMutation({
    mutationFn: async (id: string) => {
      await api.delete(`/clients/${id}`);
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['clients'] });
      queryClient.invalidateQueries({ queryKey: ['dashboard'] });
      toast.success('Client deleted');
      setDeleteTarget(null);
    },
    onError: () => toast.error('Failed to delete client'),
  });

  if (isLoading) {
    return (
      <div className='flex items-center justify-center h-48 bg-card border border-border rounded'>
        <Loader2 className='w-5 h-5 animate-spin text-primary' />
      </div>
    );
  }

  if (clients?.length === 0) {
    return (
      <div className='flex flex-col items-center justify-center h-48 gap-3 bg-card border border-border rounded'>
        <Users className='w-8 h-8 text-muted-foreground' />
        <p className='text-muted-foreground text-sm'>No clients yet</p>
        <Button variant='outline' size='sm' onClick={onAddClick} className='cursor-pointer'>
          Add your first client
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
                Name
              </TableHead>
              <TableHead className='text-xs uppercase tracking-widest text-muted-foreground'>
                Contact Person
              </TableHead>
              <TableHead className='text-xs uppercase tracking-widest text-muted-foreground'>
                Email
              </TableHead>
              <TableHead className='text-xs uppercase tracking-widest text-muted-foreground'>
                Phone
              </TableHead>
              <TableHead className='text-xs uppercase tracking-widest text-muted-foreground'>
                Payment Terms
              </TableHead>
              <TableHead />
            </TableRow>
          </TableHeader>
          <TableBody>
            {clients?.map((client) => (
              <TableRow
                key={client.id}
                className='border-border hover:bg-secondary/50'
              >
                <TableCell className='font-semibold'>{client.name}</TableCell>
                <TableCell className='text-muted-foreground'>
                  {client.contactPerson ?? '—'}
                </TableCell>
                <TableCell className='text-muted-foreground'>
                  {client.contactEmail ?? '—'}
                </TableCell>
                <TableCell className='text-muted-foreground'>
                  {client.phone ?? '—'}
                </TableCell>
                <TableCell className='text-muted-foreground'>
                  {client.paymentTermsDays} days
                </TableCell>
                <TableCell className='w-10'>
                  <DropdownMenu>
                    <DropdownMenuTrigger asChild>
                      <Button
                        variant='ghost'
                        size='icon'
                        className='h-8 w-8 text-muted-foreground hover:text-foreground cursor-pointer'
                        onClick={(e) => e.stopPropagation()}
                      >
                        <MoreHorizontal className='w-4 h-4' />
                      </Button>
                    </DropdownMenuTrigger>
                    <DropdownMenuContent
                      align='end'
                      className='bg-card border-border'
                    >
                      <DropdownMenuItem
                        className='cursor-pointer'
                        onClick={() => onEditClick(client)}
                      >
                        <Pencil className='w-4 h-4 mr-2' />
                        Edit
                      </DropdownMenuItem>
                      <DropdownMenuItem
                        className='cursor-pointer text-destructive focus:text-destructive'
                        onClick={() => setDeleteTarget(client)}
                      >
                        <Trash2 className='w-4 h-4 mr-2' />
                        Delete
                      </DropdownMenuItem>
                    </DropdownMenuContent>
                  </DropdownMenu>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </div>

      <AlertDialog
        open={!!deleteTarget}
        onOpenChange={(open) => !open && setDeleteTarget(null)}
      >
        <AlertDialogContent className='bg-card border-border'>
          <AlertDialogHeader>
            <AlertDialogTitle className='font-black'>
              Delete Client
            </AlertDialogTitle>
            <AlertDialogDescription>
              Are you sure you want to delete{' '}
              <span className='font-bold text-foreground'>
                {deleteTarget?.name}
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
                deleteTarget && deleteMutation.mutate(deleteTarget.id)
              }
            >
              {deleteMutation.isPending ? (
                <Loader2 className='w-4 h-4 animate-spin' />
              ) : (
                'Delete'
              )}
            </AlertDialogAction>
          </AlertDialogFooter>
        </AlertDialogContent>
      </AlertDialog>
    </>
  );
}
