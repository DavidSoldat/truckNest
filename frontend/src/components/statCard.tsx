export function StatCard({
  label,
  value,
  icon,
  highlight = false,
}: {
  label: string;
  value: string | number;
  icon: React.ReactNode;
  highlight?: boolean;
}) {
  return (
    <div
      className={`
      rounded border border-border p-4 flex flex-col gap-3
      ${highlight ? 'border-primary/30 bg-primary/5' : 'bg-card'}
    `}
    >
      <div className='flex items-center justify-between'>
        <span className='text-xs uppercase tracking-widest text-muted-foreground'>
          {label}
        </span>
        <span className={highlight ? 'text-primary' : 'text-muted-foreground'}>
          {icon}
        </span>
      </div>
      <span
        className={`text-3xl font-black tracking-tight ${highlight ? 'text-primary' : 'text-foreground'}`}
      >
        {value}
      </span>
    </div>
  );
}
