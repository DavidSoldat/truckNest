'use client';

import { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { Loader2, Truck, MoreHorizontal, Pencil, Trash2 } from 'lucide-react';
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
import { TruckResponse } from '@/lib/types';
import { toast } from 'sonner';
import { TruckStatusBadge } from './truckStatusBadge';

interface TrucksTableProps {
  onAddClick: () => void;
  onEditClick: (truck: TruckResponse) => void;
}

const formatDate = (date: string | null) => {
  if (!date) return '—';
  return new Date(date).toLocaleDateString('en-GB', {
    day: '2-digit',
    month: 'short',
    year: 'numeric',
  });
};

const isServiceDueSoon = (date: string | null) => {
  if (!date) return false;
  const diff = Math.ceil(
    (new Date(date).getTime() - Date.now()) / (1000 * 60 * 60 * 24),
  );
  return diff <= 14;
};

export function TrucksTable({ onAddClick, onEditClick }: TrucksTableProps) {
  const queryClient = useQueryClient();
  const [deleteTarget, setDeleteTarget] = useState<TruckResponse | null>(null);

  const { data: trucks, isLoading } = useQuery<TruckResponse[]>({
    queryKey: ['trucks'],
    queryFn: async () => {
      const res = await api.get('/trucks');
      return res.data;
    },
  });

  const deleteMutation = useMutation({
    mutationFn: async (id: string) => {
      await api.delete(`/trucks/${id}`);
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['trucks'] });
      queryClient.invalidateQueries({ queryKey: ['dashboard'] });
      toast.success('Truck deleted');
      setDeleteTarget(null);
    },
    onError: () => {
      toast.error('Failed to delete truck');
    },
  });

  if (isLoading) {
    return (
      <div className='flex items-center justify-center h-48 bg-card border border-border rounded'>
        <Loader2 className='w-5 h-5 animate-spin text-primary' />
      </div>
    );
  }

  if (trucks?.length === 0) {
    return (
      <div className='flex flex-col items-center justify-center h-48 gap-3 bg-card border border-border rounded'>
        <Truck className='w-8 h-8 text-muted-foreground' />
        <p className='text-muted-foreground text-sm'>No trucks yet</p>
        <Button
          variant='outline'
          size='sm'
          onClick={onAddClick}
          className='cursor-pointer'
        >
          Add your first truck
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
                Plate
              </TableHead>
              <TableHead className='text-xs uppercase tracking-widest text-muted-foreground'>
                Make / Model
              </TableHead>
              <TableHead className='text-xs uppercase tracking-widest text-muted-foreground'>
                Year
              </TableHead>
              <TableHead className='text-xs uppercase tracking-widest text-muted-foreground'>
                Euro
              </TableHead>
              <TableHead className='text-xs uppercase tracking-widest text-muted-foreground'>
                Next Service
              </TableHead>
              <TableHead className='text-xs uppercase tracking-widest text-muted-foreground'>
                Status
              </TableHead>
              <TableHead />
            </TableRow>
          </TableHeader>
          <TableBody>
            {trucks?.map((truck) => (
              <TableRow
                key={truck.id}
                className='border-border hover:bg-secondary/50'
              >
                <TableCell className='font-bold font-mono'>
                  {truck.plateNumber}
                </TableCell>
                <TableCell>
                  {truck.make} {truck.model}
                </TableCell>
                <TableCell className='text-muted-foreground'>
                  {truck.year}
                </TableCell>
                <TableCell className='text-muted-foreground'>
                  {truck.euroStandard?.replace('_', ' ') ?? '—'}
                </TableCell>
                <TableCell>
                  {truck.nextServiceDate ? (
                    <span
                      className={
                        isServiceDueSoon(truck.nextServiceDate)
                          ? 'text-primary font-semibold'
                          : 'text-muted-foreground'
                      }
                    >
                      {formatDate(truck.nextServiceDate)}
                      {isServiceDueSoon(truck.nextServiceDate) && ' ⚠'}
                    </span>
                  ) : (
                    '—'
                  )}
                </TableCell>
                <TableCell>
                  <TruckStatusBadge status={truck.status} />
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
                        onClick={() => onEditClick(truck)}
                      >
                        <Pencil className='w-4 h-4 mr-2' />
                        Edit
                      </DropdownMenuItem>
                      <DropdownMenuItem
                        className='cursor-pointer text-destructive focus:text-destructive'
                        onClick={() => setDeleteTarget(truck)}
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
              Delete Truck
            </AlertDialogTitle>
            <AlertDialogDescription>
              Are you sure you want to delete{' '}
              <span className='font-bold text-foreground'>
                {deleteTarget?.plateNumber}
              </span>
              ? This action cannot be undone.
            </AlertDialogDescription>
          </AlertDialogHeader>
          <AlertDialogFooter>
            <AlertDialogCancel className='border-border cursor-pointer'>
              Cancel
            </AlertDialogCancel>
            <AlertDialogAction
              className='bg-destructive text-destructive-foreground hover:bg-destructive/90 cursor-pointer'
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
