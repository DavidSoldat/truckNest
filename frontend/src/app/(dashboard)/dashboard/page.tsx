'use client';

import { useQuery } from '@tanstack/react-query';
import api from '@/lib/api';
import { DashboardResponse } from '@/lib/types';
import {
  Truck,
  Users,
  FileText,
  AlertTriangle,
  Wrench,
  IdCard,
  Loader2,
} from 'lucide-react';
import { daysUntil, formatCurrency, formatDate } from '@/lib/helpers';
import { AlertCard } from '@/components/alertCard';
import { AlertRow } from '@/components/alertRow';
import { StatCard } from '@/components/statCard';

export default function DashboardPage() {
  const { data, isLoading, isError } = useQuery<DashboardResponse>({
    queryKey: ['dashboard'],
    queryFn: async () => {
      const res = await api.get('/dashboard');
      return res.data;
    },
  });

  if (isLoading) {
    return (
      <div className='flex items-center justify-center h-64'>
        <Loader2 className='w-6 h-6 animate-spin text-primary' />
      </div>
    );
  }

  if (isError) {
    return (
      <div className='flex items-center justify-center h-64'>
        <p className='text-destructive text-sm'>Failed to load dashboard</p>
      </div>
    );
  }

  const { stats, servicesDue, documentsExpiring, overdueInvoices } = data!;

  return (
    <div className='space-y-6'>
      <div>
        <h1 className='text-2xl font-black tracking-tight'>Dashboard</h1>
        <p className='text-muted-foreground text-sm mt-1'>
          Fleet overview — updated every 60 seconds
        </p>
      </div>

      <div className='grid grid-cols-2 lg:grid-cols-4 gap-3'>
        <StatCard
          label='Total Trucks'
          value={stats.totalTrucks}
          icon={<Truck className='w-4 h-4' />}
        />
        <StatCard
          label='Total Drivers'
          value={stats.totalDrivers}
          icon={<Users className='w-4 h-4' />}
        />
        <StatCard
          label='Pending Invoices'
          value={stats.pendingInvoicesCount}
          icon={<FileText className='w-4 h-4' />}
        />
        <StatCard
          label='Pending Total'
          value={formatCurrency(stats.pendingInvoicesTotal)}
          icon={<FileText className='w-4 h-4' />}
          highlight
        />
      </div>

      <div className='grid grid-cols-1 lg:grid-cols-3 gap-4'>
        <AlertCard
          title='Service Due'
          icon={<Wrench className='w-4 h-4' />}
          count={servicesDue.length}
          empty='All trucks are up to date'
        >
          {servicesDue.map((truck) => {
            const days = daysUntil(truck.nextServiceDate);
            return (
              <AlertRow
                key={truck.id}
                title={truck.plateNumber}
                subtitle={`Due ${formatDate(truck.nextServiceDate)}`}
                badge={days <= 7 ? 'urgent' : 'warning'}
                badgeText={days <= 0 ? 'Overdue' : `${days}d`}
              />
            );
          })}
        </AlertCard>

        <AlertCard
          title='Documents Expiring'
          icon={<IdCard className='w-4 h-4' />}
          count={documentsExpiring.length}
          empty='All documents are valid'
        >
          {documentsExpiring.map((driver) => {
            const expiry = driver.licenseExpiry ?? driver.visaExpiry;
            const type = driver.licenseExpiry ? 'License' : 'Visa';
            const days = expiry ? daysUntil(expiry) : 0;
            return (
              <AlertRow
                key={driver.id}
                title={driver.fullName}
                subtitle={`${type} expires ${expiry ? formatDate(expiry) : '—'}`}
                badge={days <= 7 ? 'urgent' : 'warning'}
                badgeText={days <= 0 ? 'Expired' : `${days}d`}
              />
            );
          })}
        </AlertCard>

        <AlertCard
          title='Overdue Invoices'
          icon={<AlertTriangle className='w-4 h-4' />}
          count={overdueInvoices.length}
          empty='No overdue invoices'
        >
          {overdueInvoices.map((invoice) => {
            const days = daysUntil(invoice.dueDate);
            return (
              <AlertRow
                key={invoice.id}
                title={invoice.invoiceNumber}
                subtitle={`Due ${formatDate(invoice.dueDate)} · ${formatCurrency(invoice.amount)}`}
                badge='urgent'
                badgeText={`${Math.abs(days)}d late`}
              />
            );
          })}
        </AlertCard>
      </div>
    </div>
  );
}
