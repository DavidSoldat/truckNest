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
import { DriverResponse } from '@/lib/types';
import { toast } from 'sonner';
import { DriverStatusBadge } from './driverStatusBadge';

interface DriversTableProps {
  onAddClick: () => void;
  onEditClick: (driver: DriverResponse) => void;
}

const formatDate = (date: string | null) => {
  if (!date) return '—';
  return new Date(date).toLocaleDateString('en-GB', {
    day: '2-digit',
    month: 'short',
    year: 'numeric',
  });
};

const isExpiringSoon = (date: string | null) => {
  if (!date) return false;
  const diff = Math.ceil(
    (new Date(date).getTime() - Date.now()) / (1000 * 60 * 60 * 24),
  );
  return diff <= 30;
};

export function DriversTable({ onAddClick, onEditClick }: DriversTableProps) {
  const queryClient = useQueryClient();
  const [deleteTarget, setDeleteTarget] = useState<DriverResponse | null>(null);

  const { data: drivers, isLoading } = useQuery<DriverResponse[]>({
    queryKey: ['drivers'],
    queryFn: async () => {
      const res = await api.get('/drivers');
      return res.data;
    },
  });

  const deleteMutation = useMutation({
    mutationFn: async (id: string) => {
      await api.delete(`/drivers/${id}`);
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['drivers'] });
      queryClient.invalidateQueries({ queryKey: ['dashboard'] });
      toast.success('Driver deleted');
      setDeleteTarget(null);
    },
    onError: () => toast.error('Failed to delete driver'),
  });

  if (isLoading) {
    return (
      <div className='flex items-center justify-center h-48 bg-card border border-border rounded'>
        <Loader2 className='w-5 h-5 animate-spin text-primary' />
      </div>
    );
  }

  if (drivers?.length === 0) {
    return (
      <div className='flex flex-col items-center justify-center h-48 gap-3 bg-card border border-border rounded'>
        <Users className='w-8 h-8 text-muted-foreground' />
        <p className='text-muted-foreground text-sm'>No drivers yet</p>
        <Button variant='outline' size='sm' onClick={onAddClick}>
          Add your first driver
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
                Phone
              </TableHead>
              <TableHead className='text-xs uppercase tracking-widest text-muted-foreground'>
                License Expiry
              </TableHead>
              <TableHead className='text-xs uppercase tracking-widest text-muted-foreground'>
                Visa Expiry
              </TableHead>
              <TableHead className='text-xs uppercase tracking-widest text-muted-foreground'>
                Salary
              </TableHead>
              <TableHead className='text-xs uppercase tracking-widest text-muted-foreground'>
                Status
              </TableHead>
              <TableHead />
            </TableRow>
          </TableHeader>
          <TableBody>
            {drivers?.map((driver) => (
              <TableRow
                key={driver.id}
                className='border-border hover:bg-secondary/50'
              >
                <TableCell className='font-semibold'>
                  {driver.firstName} {driver.lastName}
                </TableCell>
                <TableCell className='text-muted-foreground'>
                  {driver.phone ?? '—'}
                </TableCell>
                <TableCell>
                  {driver.licenseExpiry ? (
                    <span
                      className={
                        isExpiringSoon(driver.licenseExpiry)
                          ? 'text-primary font-semibold'
                          : 'text-muted-foreground'
                      }
                    >
                      {formatDate(driver.licenseExpiry)}
                      {isExpiringSoon(driver.licenseExpiry) && ' ⚠'}
                    </span>
                  ) : (
                    '—'
                  )}
                </TableCell>
                <TableCell>
                  {driver.visaExpiry ? (
                    <span
                      className={
                        isExpiringSoon(driver.visaExpiry)
                          ? 'text-primary font-semibold'
                          : 'text-muted-foreground'
                      }
                    >
                      {formatDate(driver.visaExpiry)}
                      {isExpiringSoon(driver.visaExpiry) && ' ⚠'}
                    </span>
                  ) : (
                    '—'
                  )}
                </TableCell>
                <TableCell className='text-muted-foreground'>
                  {driver.monthlySalary
                    ? new Intl.NumberFormat('de-DE', {
                        style: 'currency',
                        currency: 'EUR',
                      }).format(driver.monthlySalary)
                    : '—'}
                </TableCell>
                <TableCell>
                  <DriverStatusBadge status={driver.status} />
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
                        onClick={() => onEditClick(driver)}
                      >
                        <Pencil className='w-4 h-4 mr-2' />
                        Edit
                      </DropdownMenuItem>
                      <DropdownMenuItem
                        className='cursor-pointer text-destructive focus:text-destructive'
                        onClick={() => setDeleteTarget(driver)}
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
              Delete Driver
            </AlertDialogTitle>
            <AlertDialogDescription>
              Are you sure you want to delete{' '}
              <span className='font-bold text-foreground'>
                {deleteTarget?.firstName} {deleteTarget?.lastName}
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
