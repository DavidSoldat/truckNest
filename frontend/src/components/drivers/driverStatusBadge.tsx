import { Badge } from '@/components/ui/badge';
import { DriverStatus } from '@/lib/types';

const statusColors: Record<DriverStatus, string> = {
  ACTIVE: 'bg-green-500/10 text-green-500 border-green-500/20',
  INACTIVE: 'bg-muted text-muted-foreground border-border',
  ON_LEAVE: 'bg-primary/10 text-primary border-primary/20',
  TERMINATED: 'bg-destructive/10 text-destructive border-destructive/20',
};

const statusLabels: Record<DriverStatus, string> = {
  ACTIVE: 'Active',
  INACTIVE: 'Inactive',
  ON_LEAVE: 'On Leave',
  TERMINATED: 'Terminated',
};

export function DriverStatusBadge({ status }: { status: DriverStatus }) {
  return (
    <Badge variant='outline' className={statusColors[status]}>
      {statusLabels[status]}
    </Badge>
  );
}
