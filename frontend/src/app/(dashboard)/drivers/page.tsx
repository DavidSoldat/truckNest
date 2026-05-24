'use client'

import { useState } from 'react'
import { Button } from '@/components/ui/button'
import { Plus } from 'lucide-react'
import { DriverResponse } from '@/lib/types'
import { DriversTable } from '@/components/drivers/driversTable'
import { AddDriverDialog } from '@/components/drivers/addDriverForm'

export default function DriversPage() {
  const [dialogOpen, setDialogOpen] = useState(false)
  const [editTarget, setEditTarget] = useState<DriverResponse | null>(null)

  const handleEditClick = (driver: DriverResponse) => {
    setEditTarget(driver)
    setDialogOpen(true)
  }

  const handleOpenChange = (open: boolean) => {
    setDialogOpen(open)
    if (!open) setEditTarget(null)
  }

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-black tracking-tight">Drivers</h1>
          <p className="text-muted-foreground text-sm mt-1">
            Manage your drivers
          </p>
        </div>
        <Button onClick={() => setDialogOpen(true)} className="font-bold">
          <Plus className="w-4 h-4 mr-2" />
          Add Driver
        </Button>
      </div>

      <DriversTable
        onAddClick={() => setDialogOpen(true)}
        onEditClick={handleEditClick}
      />
      <AddDriverDialog
        key={editTarget?.id ?? 'new'}
        open={dialogOpen}
        onOpenChange={handleOpenChange}
        editTarget={editTarget}
      />
    </div>
  )
}