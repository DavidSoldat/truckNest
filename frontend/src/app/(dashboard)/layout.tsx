import { AppSidebar } from '@/components/app-sidebar';
import { SidebarProvider, SidebarTrigger } from '@/components/ui/sidebar';

export default function DashboardLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <SidebarProvider>
      <AppSidebar />
      <main className='flex-1 flex flex-col min-h-screen'>
        <header className='h-12 border-b border-border flex items-center px-4 gap-3'>
          <SidebarTrigger className='text-muted-foreground hover:text-foreground' />
          <div className='w-px h-4 bg-border' />
          <span className='text-xs text-muted-foreground uppercase tracking-widest'>
            Fleet Management
          </span>
        </header>
        <div className='flex-1 p-6'>{children}</div>
      </main>
    </SidebarProvider>
  );
}
