import { Badge } from '@/components/ui/badge';
import { TruckStatus } from '@/lib/types';

const statusColors: Record<TruckStatus, string> = {
  ACTIVE: 'bg-green-500/10 text-green-500 border-green-500/20',
  INACTIVE: 'bg-muted text-muted-foreground border-border',
  IN_SERVICE: 'bg-primary/10 text-primary border-primary/20',
  RETIRED: 'bg-destructive/10 text-destructive border-destructive/20',
};

const statusLabels: Record<TruckStatus, string> = {
  ACTIVE: 'Active',
  INACTIVE: 'Inactive',
  IN_SERVICE: 'In Service',
  RETIRED: 'Retired',
};

export function TruckStatusBadge({ status }: { status: TruckStatus }) {
  return (
    <Badge variant='outline' className={statusColors[status]}>
      {statusLabels[status]}
    </Badge>
  );
}
