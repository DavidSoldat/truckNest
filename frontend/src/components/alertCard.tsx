export function AlertCard({
  title,
  icon,
  count,
  empty,
  children,
}: {
  title: string;
  icon: React.ReactNode;
  count: number;
  empty: string;
  children: React.ReactNode;
}) {
  return (
    <div className='bg-card border border-border rounded p-4 flex flex-col gap-3'>
      <div className='flex items-center justify-between'>
        <div className='flex items-center gap-2'>
          <span className='text-muted-foreground'>{icon}</span>
          <span className='text-sm font-bold uppercase tracking-widest'>
            {title}
          </span>
        </div>
        {count > 0 && (
          <span className='text-xs bg-destructive/10 text-destructive border border-destructive/20 rounded px-2 py-0.5 font-bold'>
            {count}
          </span>
        )}
      </div>

      <div className='flex flex-col gap-2'>
        {count === 0 ? (
          <p className='text-xs text-muted-foreground py-4 text-center'>
            {empty}
          </p>
        ) : (
          children
        )}
      </div>
    </div>
  );
}
