/* eslint-disable @typescript-eslint/no-explicit-any */
'use client';

import { useState } from 'react';
import { useRouter } from 'next/navigation';
import Link from 'next/link';
import { toast } from 'sonner';
import { Truck, Loader2 } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import axios from 'axios';

export default function RegisterPage() {
  const router = useRouter();
  const [loading, setLoading] = useState(false);
  const [form, setForm] = useState({
    companyName: '',
    ownerFirstName: '',
    ownerLastName: '',
    email: '',
    password: '',
  });

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setForm((prev) => ({ ...prev, [e.target.name]: e.target.value }));
  };

  const handleRegister = async (e: React.SubmitEvent) => {
    e.preventDefault();
    setLoading(true);
    try {
      await axios.post('/api/auth/register', form);
      toast.success('Company registered! Please sign in.');
      router.push('/login');
    } catch (err: any) {
      toast.error(err?.response?.data?.message || 'Registration failed');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className='min-h-screen bg-background flex items-center justify-center p-8'>
      <div className='w-full max-w-sm'>
        <div className='flex items-center gap-3 mb-10'>
          <div className='w-9 h-9 bg-primary rounded flex items-center justify-center'>
            <Truck className='w-5 h-5 text-primary-foreground' />
          </div>
          <span className='text-xl font-bold tracking-tight'>TruckNest</span>
        </div>

        <div className='mb-8'>
          <h1 className='text-2xl font-black tracking-tight mb-1'>
            Register your company
          </h1>
          <p className='text-muted-foreground text-sm'>
            Get started with your fleet management
          </p>
        </div>

        <form onSubmit={handleRegister} className='space-y-4'>
          <div className='space-y-1.5'>
            <Label
              htmlFor='companyName'
              className='text-xs uppercase tracking-widest text-muted-foreground'
            >
              Company Name
            </Label>
            <Input
              id='companyName'
              name='companyName'
              placeholder='Petrović Transport d.o.o.'
              value={form.companyName}
              onChange={handleChange}
              required
              className='bg-secondary border-border h-11'
            />
          </div>

          <div className='grid grid-cols-2 gap-3'>
            <div className='space-y-1.5'>
              <Label
                htmlFor='ownerFirstName'
                className='text-xs uppercase tracking-widest text-muted-foreground'
              >
                First Name
              </Label>
              <Input
                id='ownerFirstName'
                name='ownerFirstName'
                placeholder='Marko'
                value={form.ownerFirstName}
                onChange={handleChange}
                required
                className='bg-secondary border-border h-11'
              />
            </div>
            <div className='space-y-1.5'>
              <Label
                htmlFor='ownerLastName'
                className='text-xs uppercase tracking-widest text-muted-foreground'
              >
                Last Name
              </Label>
              <Input
                id='ownerLastName'
                name='ownerLastName'
                placeholder='Petrović'
                value={form.ownerLastName}
                onChange={handleChange}
                required
                className='bg-secondary border-border h-11'
              />
            </div>
          </div>

          <div className='space-y-1.5'>
            <Label
              htmlFor='email'
              className='text-xs uppercase tracking-widest text-muted-foreground'
            >
              Email
            </Label>
            <Input
              id='email'
              name='email'
              type='email'
              placeholder='marko@petrovictransport.ba'
              value={form.email}
              onChange={handleChange}
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
              name='password'
              type='password'
              placeholder='••••••••'
              value={form.password}
              onChange={handleChange}
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
              'CREATE ACCOUNT'
            )}
          </Button>
        </form>

        <p className='text-center text-sm text-muted-foreground mt-6'>
          Already have an account?{' '}
          <Link
            href='/login'
            className='text-primary hover:underline font-medium'
          >
            Sign in
          </Link>
        </p>
      </div>
    </div>
  );
}
