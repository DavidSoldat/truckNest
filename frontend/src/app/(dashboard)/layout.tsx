'use client';

import { useQuery } from '@tanstack/react-query';
import { SidebarProvider } from '@/components/ui/sidebar';
import { AppSidebar } from '@/components/app-sidebar';
import { SidebarTrigger } from '@/components/ui/sidebar';
import api from '@/lib/api';

export default function DashboardLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  const { data: company } = useQuery({
    queryKey: ['company'],
    queryFn: async () => {
      const res = await api.get('/companies/me');
      return res.data as { id: string; name: string; contactEmail: string };
    },
  });

  return (
    <SidebarProvider>
      <AppSidebar />
      <main className='flex-1 flex flex-col min-h-screen'>
        <header className='h-12 border-b border-border flex items-center px-4 gap-3'>
          <SidebarTrigger className='text-muted-foreground hover:text-foreground cursor-pointer' />
          <div className='w-px h-4 bg-border' />
          <span className='text-xs text-muted-foreground uppercase tracking-widest cursor-default'>
            Fleet Management
          </span>
          {company?.name && (
            <span className='ml-auto text-xs font-semibold text-muted-foreground uppercase tracking-widest cursor-default'>
              {company.name}
            </span>
          )}
        </header>
        <div className='flex-1 p-6'>{children}</div>
      </main>
    </SidebarProvider>
  );
}
