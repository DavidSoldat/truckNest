'use client';

import Link from 'next/link';
import { usePathname, useRouter } from 'next/navigation';
import {
  LayoutDashboard,
  Truck,
  Users,
  Building2,
  FileText,
  LogOut,
} from 'lucide-react';
import {
  Sidebar,
  SidebarContent,
  SidebarFooter,
  SidebarGroup,
  SidebarGroupContent,
  SidebarHeader,
  SidebarMenu,
  SidebarMenuButton,
  SidebarMenuItem,
} from '@/components/ui/sidebar';
import { useAuthStore } from '@/lib/auth-store';
import axios from 'axios';

const navItems = [
  { label: 'Dashboard', href: '/dashboard', icon: LayoutDashboard },
  { label: 'Trucks', href: '/trucks', icon: Truck },
  { label: 'Drivers', href: '/drivers', icon: Users },
  { label: 'Clients', href: '/clients', icon: Building2 },
  { label: 'Invoices', href: '/invoices', icon: FileText },
];

export function AppSidebar() {
  const pathname = usePathname();
  const router = useRouter();

  const clearAuth = useAuthStore((s) => s.clearAuth);

  const handleLogout = async () => {
    await axios.post('/api/auth/logout');
    clearAuth();
    router.push('/login');
  };

  return (
    <Sidebar>
      <SidebarHeader className='p-4 border-b border-sidebar-border'>
        <div className='flex items-center gap-3'>
          <div className='w-8 h-8 bg-primary rounded flex items-center justify-center shrink-0'>
            <Truck className='w-4 h-4 text-primary-foreground' />
          </div>
          <span className='font-black tracking-tight text-lg'>TruckNest</span>
        </div>
      </SidebarHeader>

      <SidebarContent className='p-2'>
        <SidebarGroup>
          <SidebarGroupContent>
            <SidebarMenu>
              {navItems.map((item) => {
                const isActive = pathname === item.href;
                return (
                  <SidebarMenuItem key={item.href}>
                    <SidebarMenuButton
                      asChild
                      isActive={isActive}
                      className='h-10 font-medium'
                    >
                      <Link href={item.href}>
                        <item.icon className='w-4 h-4' />
                        <span>{item.label}</span>
                      </Link>
                    </SidebarMenuButton>
                  </SidebarMenuItem>
                );
              })}
            </SidebarMenu>
          </SidebarGroupContent>
        </SidebarGroup>
      </SidebarContent>

      <SidebarFooter className='p-2 border-t border-sidebar-border'>
        <SidebarMenu>
          <SidebarMenuItem>
            <SidebarMenuButton
              onClick={handleLogout}
              className='h-10 cursor-pointer font-medium text-muted-foreground hover:text-destructive hover:bg-destructive/10'
            >
              <LogOut className='w-4 h-4' />
              <span>Sign out</span>
            </SidebarMenuButton>
          </SidebarMenuItem>
        </SidebarMenu>
      </SidebarFooter>
    </Sidebar>
  );
}
