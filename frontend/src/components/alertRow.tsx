export function AlertRow({
  title,
  subtitle,
  badge,
  badgeText,
}: {
  title: string;
  subtitle: string;
  badge: 'urgent' | 'warning';
  badgeText: string;
}) {
  return (
    <div className='flex items-center justify-between py-2 border-b border-border last:border-0'>
      <div>
        <p className='text-sm font-semibold'>{title}</p>
        <p className='text-xs text-muted-foreground'>{subtitle}</p>
      </div>
      <span
        className={`
        text-xs font-bold px-2 py-0.5 rounded border shrink-0
        ${
          badge === 'urgent'
            ? 'bg-destructive/10 text-destructive border-destructive/20'
            : 'bg-primary/10 text-primary border-primary/20'
        }
      `}
      >
        {badgeText}
      </span>
    </div>
  );
}
