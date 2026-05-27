export interface TruckServiceDueDto {
  id: string;
  plateNumber: string;
  nextServiceDate: string;
  companyId: string;
}

export interface DriverDocumentDto {
  id: string;
  fullName: string;
  licenseExpiry: string | null;
  visaExpiry: string | null;
  companyId: string;
}

export interface InvoiceOverdueDto {
  id: string;
  invoiceNumber: string;
  clientId: string;
  dueDate: string;
  amount: number;
  companyId: string;
}

export interface DashboardStats {
  totalTrucks: number;
  totalDrivers: number;
  pendingInvoicesCount: number;
  pendingInvoicesTotal: number;
}

export interface DashboardResponse {
  servicesDue: TruckServiceDueDto[];
  documentsExpiring: DriverDocumentDto[];
  overdueInvoices: InvoiceOverdueDto[];
  stats: DashboardStats;
}

export type TruckStatus = 'ACTIVE' | 'INACTIVE' | 'IN_SERVICE' | 'RETIRED';
export type EuroStandard =
  | 'EURO_1'
  | 'EURO_2'
  | 'EURO_3'
  | 'EURO_4'
  | 'EURO_5'
  | 'EURO_6';

export interface TruckResponse {
  id: string;
  plateNumber: string;
  make: string;
  model: string;
  year: number;
  vin: string | null;
  nextServiceDate: string | null;
  status: TruckStatus;
  euroStandard: EuroStandard | null;
  notes: string | null;
  createdAt: string;
  updatedAt: string;
}

export interface TruckRequest {
  plateNumber: string;
  make: string;
  model: string;
  year: number;
  vin?: string;
  nextServiceDate?: string;
  status?: TruckStatus;
  euroStandard?: EuroStandard;
  notes?: string;
}

export type DriverStatus = 'ACTIVE' | 'INACTIVE' | 'ON_LEAVE' | 'TERMINATED';

export interface DriverResponse {
  id: string;
  firstName: string;
  lastName: string;
  dateOfBirth: string | null;
  phone: string | null;
  email: string | null;
  licenseNumber: string | null;
  licenseExpiry: string | null;
  visaExpiry: string | null;
  status: DriverStatus;
  monthlySalary: number | null;
  notes: string | null;
  createdAt: string;
  updatedAt: string;
}

export interface DriverRequest {
  firstName: string;
  lastName: string;
  dateOfBirth?: string;
  phone?: string;
  email?: string;
  licenseNumber?: string;
  licenseExpiry?: string;
  visaExpiry?: string;
  status?: DriverStatus;
  monthlySalary?: number;
  notes?: string;
}

export interface Client {
  id: string;
  name: string;
  contactPerson: string | null;
  contactEmail: string | null;
  phone: string | null;
  paymentTermsDays: number;
  notes: string | null;
  createdAt: string;
  updatedAt: string;
}

export interface ClientRequest {
  name: string;
  contactPerson?: string;
  contactEmail?: string;
  phone?: string;
  paymentTermsDays: number;
  notes?: string;
}

export type InvoiceStatus = 'PENDING' | 'PAID' | 'OVERDUE' | 'CANCELLED';

export interface Invoice {
  id: string;
  clientId: string;
  transportJobId: string | null;
  invoiceNumber: string;
  issueDate: string;
  dueDate: string;
  amount: number;
  status: InvoiceStatus;
  paymentDate: string | null;
  amountPaid: number | null;
  reminderSentAt: string | null;
  notes: string | null;
  createdAt: string;
  updatedAt: string;
}

export interface InvoiceRequest {
  clientId: string;
  transportJobId?: string;
  invoiceNumber: string;
  issueDate: string;
  amount: number;
  notes?: string;
}

export interface MarkPaidRequest {
  paymentDate: string;
  amountPaid: number;
}
