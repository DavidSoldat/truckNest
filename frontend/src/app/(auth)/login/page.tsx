'use client';

import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { useAuthStore } from '@/lib/auth-store';
import axios from 'axios';
import { Loader2, Truck } from 'lucide-react';
import Link from 'next/link';
import { useRouter } from 'next/navigation';
import { useState } from 'react';
import { toast } from 'sonner';

export default function LoginPage() {
  const router = useRouter();
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [loading, setLoading] = useState(false);

  const setAuth = useAuthStore((s) => s.setAuth);

  const handleLogin = async (e: React.SubmitEvent<HTMLFormElement>) => {
    e.preventDefault();
    setLoading(true);
    try {
      const { data } = await axios.post('/api/auth/login', { email, password });
      setAuth(data.companyId, data.email);
      router.push('/dashboard');
    } catch {
      toast.error('Invalid email or password');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className='min-h-screen bg-background flex'>
      <div className='hidden lg:flex lg:w-1/2 flex-col justify-between p-12 border-r border-border relative overflow-hidden'>
        <div
          className='absolute inset-0 opacity-[0.03]'
          style={{
            backgroundImage: `linear-gradient(var(--color-foreground) 1px, transparent 1px),
              linear-gradient(90deg, var(--color-foreground) 1px, transparent 1px)`,
            backgroundSize: '40px 40px',
          }}
        />
        <div className='flex items-center gap-3 relative z-10'>
          <div className='w-9 h-9 bg-primary rounded flex items-center justify-center'>
            <Truck className='w-5 h-5 text-primary-foreground' />
          </div>
          <span className='text-xl font-bold tracking-tight'>TruckNest</span>
        </div>

        <div className='relative z-10'>
          <div className='text-6xl font-black tracking-tighter leading-none text-foreground mb-6'>
            FLEET
            <br />
            <span className='text-primary'>UNDER</span>
            <br />
            CONTROL
          </div>
          <p className='text-muted-foreground text-sm max-w-xs leading-relaxed'>
            Manage your trucks, drivers, and invoices from one place. Built for
            small trucking companies in the region.
          </p>
        </div>

        <div className='grid grid-cols-3 gap-4 relative z-10'>
          {[
            { label: 'Trucks tracked', value: '—' },
            { label: 'Drivers managed', value: '—' },
            { label: 'Invoices sent', value: '—' },
          ].map((stat) => (
            <div key={stat.label} className='border border-border rounded p-3'>
              <div className='text-2xl font-black text-primary'>
                {stat.value}
              </div>
              <div className='text-xs text-muted-foreground mt-1'>
                {stat.label}
              </div>
            </div>
          ))}
        </div>
      </div>

      <div className='flex-1 flex items-center justify-center p-8'>
        <div className='w-full max-w-sm'>
          <div className='flex items-center gap-3 mb-10 lg:hidden'>
            <div className='w-9 h-9 bg-primary rounded flex items-center justify-center'>
              <Truck className='w-5 h-5 text-primary-foreground' />
            </div>
            <span className='text-xl font-bold tracking-tight'>TruckNest</span>
          </div>

          <div className='mb-8'>
            <h1 className='text-2xl font-black tracking-tight mb-1'>Sign in</h1>
            <p className='text-muted-foreground text-sm'>
              Enter your credentials to access your fleet
            </p>
          </div>

          <form onSubmit={handleLogin} className='space-y-4'>
            <div className='space-y-1.5'>
              <Label
                htmlFor='email'
                className='text-xs uppercase tracking-widest text-muted-foreground'
              >
                Email
              </Label>
              <Input
                id='email'
                type='email'
                placeholder='you@company.com'
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                required
                className='bg-secondary border-border h-11'
              />
            </div>

            <div className='space-y-1.5'>
              <Label
                htmlFor='password'
                className='text-xs uppercase tracking-widest text-muted-foreground'
              >
                Password
              </Label>
              <Input
                id='password'
                type='password'
                placeholder='••••••••'
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                required
                className='bg-secondary border-border h-11'
              />
            </div>

            <Button
              type='submit'
              className='w-full h-11 font-bold tracking-wide'
              disabled={loading}
            >
              {loading ? (
                <Loader2 className='w-4 h-4 animate-spin' />
              ) : (
                'SIGN IN'
              )}
            </Button>
          </form>

          <p className='text-center text-sm text-muted-foreground mt-6'>
            No account yet?{' '}
            <Link
              href='/register'
              className='text-primary hover:underline font-medium'
            >
              Register your company
            </Link>
          </p>
        </div>
      </div>
    </div>
  );
}
