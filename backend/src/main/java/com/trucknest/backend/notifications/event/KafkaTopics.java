package com.trucknest.backend.notifications.event;

public final class KafkaTopics {

    public static final String SERVICE_DUE = "trucknest.service.due";
    public static final String DOCUMENT_EXPIRY = "trucknest.document.expiry";
    public static final String INVOICE_OVERDUE = "trucknest.invoice.overdue";

    private KafkaTopics() {}
}