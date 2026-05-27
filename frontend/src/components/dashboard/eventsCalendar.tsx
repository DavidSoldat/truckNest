'use client';

import { useState, useMemo } from 'react';
import {
  ChevronLeft,
  ChevronRight,
  Wrench,
  IdCard,
  AlertTriangle,
} from 'lucide-react';
import { DashboardResponse } from '@/lib/types';
import { formatDate } from '@/lib/helpers';

type CalendarEvent = {
  date: string; // YYYY-MM-DD
  label: string;
  type: 'service' | 'document' | 'invoice';
};

interface DashboardCalendarProps {
  data: DashboardResponse;
}

const TYPE_CONFIG = {
  service: {
    color: 'bg-yellow-500/20 text-yellow-400 border-yellow-500/30',
    dot: 'bg-yellow-500',
    icon: <Wrench className='w-3 h-3' />,
  },
  document: {
    color: 'bg-blue-500/20 text-blue-400 border-blue-500/30',
    dot: 'bg-blue-500',
    icon: <IdCard className='w-3 h-3' />,
  },
  invoice: {
    color: 'bg-destructive/20 text-destructive border-destructive/30',
    dot: 'bg-destructive',
    icon: <AlertTriangle className='w-3 h-3' />,
  },
};

const DAYS = ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'];
const MONTHS = [
  'January',
  'February',
  'March',
  'April',
  'May',
  'June',
  'July',
  'August',
  'September',
  'October',
  'November',
  'December',
];

function toYMD(date: string): string {
  return date.split('T')[0];
}

export function DashboardCalendar({ data }: DashboardCalendarProps) {
  const today = new Date();
  const [year, setYear] = useState(today.getFullYear());
  const [month, setMonth] = useState(today.getMonth()); // 0-indexed
  const [selected, setSelected] = useState<string | null>(null);

  const events = useMemo<CalendarEvent[]>(() => {
    const list: CalendarEvent[] = [];

    data.servicesDue.forEach((truck) => {
      if (truck.nextServiceDate) {
        list.push({
          date: toYMD(truck.nextServiceDate),
          label: `${truck.plateNumber} — Service`,
          type: 'service',
        });
      }
    });

    data.documentsExpiring.forEach((driver) => {
      if (driver.licenseExpiry) {
        list.push({
          date: toYMD(driver.licenseExpiry),
          label: `${driver.fullName} — License`,
          type: 'document',
        });
      }
      if (driver.visaExpiry) {
        list.push({
          date: toYMD(driver.visaExpiry),
          label: `${driver.fullName} — Visa`,
          type: 'document',
        });
      }
    });

    data.overdueInvoices.forEach((invoice) => {
      if (invoice.dueDate) {
        list.push({
          date: toYMD(invoice.dueDate),
          label: `${invoice.invoiceNumber} — €${invoice.amount}`,
          type: 'invoice',
        });
      }
    });

    return list;
  }, [data]);

  const eventsByDate = useMemo(() => {
    const map: Record<string, CalendarEvent[]> = {};
    events.forEach((e) => {
      if (!map[e.date]) map[e.date] = [];
      map[e.date].push(e);
    });
    return map;
  }, [events]);

  const firstDay = new Date(year, month, 1);
  const startOffset = (firstDay.getDay() + 6) % 7;
  const daysInMonth = new Date(year, month + 1, 0).getDate();
  const daysInPrevMonth = new Date(year, month, 0).getDate();

  const totalCells = Math.ceil((startOffset + daysInMonth) / 7) * 7;

  const cells: { dateStr: string | null; day: number; inMonth: boolean }[] = [];
  for (let i = 0; i < totalCells; i++) {
    if (i < startOffset) {
      const day = daysInPrevMonth - startOffset + i + 1;
      const d = new Date(year, month - 1, day);
      cells.push({ dateStr: toYMD(d.toISOString()), day, inMonth: false });
    } else if (i < startOffset + daysInMonth) {
      const day = i - startOffset + 1;
      const d = new Date(year, month, day);
      cells.push({ dateStr: toYMD(d.toISOString()), day, inMonth: true });
    } else {
      const day = i - startOffset - daysInMonth + 1;
      const d = new Date(year, month + 1, day);
      cells.push({ dateStr: toYMD(d.toISOString()), day, inMonth: false });
    }
  }

  const todayStr = toYMD(today.toISOString());

  const prevMonth = () => {
    if (month === 0) {
      setMonth(11);
      setYear((y) => y - 1);
    } else setMonth((m) => m - 1);
    setSelected(null);
  };

  const nextMonth = () => {
    if (month === 11) {
      setMonth(0);
      setYear((y) => y + 1);
    } else setMonth((m) => m + 1);
    setSelected(null);
  };

  const selectedEvents = selected ? (eventsByDate[selected] ?? []) : [];

  return (
    <div className='border border-border rounded bg-card p-4 space-y-4'>
      <div className='flex items-center justify-between'>
        <h2 className='font-black tracking-tight text-lg'>
          {MONTHS[month]} {year}
        </h2>
        <div className='flex items-center gap-1'>
          <div className='flex items-center gap-3 mr-4'>
            {(['service', 'document', 'invoice'] as const).map((type) => (
              <div
                key={type}
                className='flex items-center gap-1.5 text-xs text-muted-foreground'
              >
                <span
                  className={`w-2 h-2 rounded-full ${TYPE_CONFIG[type].dot}`}
                />
                {type === 'service'
                  ? 'Service'
                  : type === 'document'
                    ? 'Document'
                    : 'Invoice'}
              </div>
            ))}
          </div>
          <button
            onClick={prevMonth}
            className='h-8 w-8 flex items-center justify-center rounded hover:bg-secondary transition-colors text-muted-foreground hover:text-foreground'
          >
            <ChevronLeft className='w-4 h-4' />
          </button>
          <button
            onClick={() => {
              setMonth(today.getMonth());
              setYear(today.getFullYear());
              setSelected(null);
            }}
            className='px-2 h-8 rounded text-xs font-semibold hover:bg-secondary transition-colors text-muted-foreground hover:text-foreground'
          >
            Today
          </button>
          <button
            onClick={nextMonth}
            className='h-8 w-8 flex items-center justify-center rounded hover:bg-secondary transition-colors text-muted-foreground hover:text-foreground'
          >
            <ChevronRight className='w-4 h-4' />
          </button>
        </div>
      </div>

      <div className='grid grid-cols-7 gap-px'>
        {DAYS.map((d) => (
          <div
            key={d}
            className='text-center text-xs uppercase tracking-widest text-muted-foreground py-1'
          >
            {d}
          </div>
        ))}
      </div>

      <div className='grid grid-cols-7 gap-px bg-border rounded overflow-hidden'>
        {cells.map(({ dateStr, day, inMonth }, i) => {
          const cellEvents = dateStr ? (eventsByDate[dateStr] ?? []) : [];
          const isToday = dateStr === todayStr;
          const isSelected = dateStr === selected;

          return (
            <button
              key={i}
              onClick={() =>
                dateStr && setSelected(isSelected ? null : dateStr)
              }
              className={`
                bg-card min-h-18 p-1.5 text-left flex flex-col gap-1 transition-colors
                ${inMonth ? 'hover:bg-secondary/50' : 'opacity-40 hover:bg-secondary/30'}
                ${isSelected ? 'bg-secondary' : ''}
              `}
            >
              <span
                className={`
                  text-xs font-semibold w-6 h-6 flex items-center justify-center rounded-full
                  ${isToday ? 'bg-primary text-primary-foreground' : inMonth ? 'text-foreground' : 'text-muted-foreground'}
                `}
              >
                {day}
              </span>

              <div className='flex flex-col gap-0.5 w-full'>
                {cellEvents.slice(0, 2).map((e, ei) => (
                  <div
                    key={ei}
                    className={`
                      flex items-center gap-1 px-1 py-0.5 rounded text-[10px] font-medium border truncate
                      ${TYPE_CONFIG[e.type].color}
                    `}
                  >
                    {TYPE_CONFIG[e.type].icon}
                    <span className='truncate'>{e.label.split(' — ')[0]}</span>
                  </div>
                ))}
                {cellEvents.length > 2 && (
                  <span className='text-[10px] text-muted-foreground pl-1'>
                    +{cellEvents.length - 2} more
                  </span>
                )}
              </div>
            </button>
          );
        })}
      </div>

      {selected && selectedEvents.length > 0 && (
        <div className='border border-border rounded p-3 space-y-2'>
          <p className='text-xs uppercase tracking-widest text-muted-foreground font-semibold'>
            {formatDate(selected)}
          </p>
          <div className='space-y-1.5'>
            {selectedEvents.map((e, i) => (
              <div
                key={i}
                className={`flex items-center gap-2 px-2 py-1.5 rounded border text-sm ${TYPE_CONFIG[e.type].color}`}
              >
                {TYPE_CONFIG[e.type].icon}
                <span>{e.label}</span>
              </div>
            ))}
          </div>
        </div>
      )}

      {selected && selectedEvents.length === 0 && (
        <div className='border border-border rounded p-3'>
          <p className='text-xs text-muted-foreground'>
            {formatDate(selected)} — no events
          </p>
        </div>
      )}
    </div>
  );
}
