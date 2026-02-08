# Invoice Builder App

## Overview

Invoice Builder is an application that helps businesses create, manage, and download professional invoices from their business data. It turns customer records, sender details, and line items into clear, accurate invoices with correct totals, taxes, and payment terms.

The system is designed to support every-day invoicing needs while remaining extensible for reporting, payments, and future integrations.

## What the App Does

### Core Uses

- Create and manage customers and senders
- Build invoices using customer data, sender details, dates, and line items
- Automatically calculate totals and taxes
- Generate, preview and download invoice documents (PDF)
- Track invoice status and payment information
- Provide data for summaries, reports, and dashboards

## Who This App Is For

- Small and medium-sized businesses that need a simple way to generate and manage invoices
- Freelancers and consultants who invoice clients regularly
- Internal finance or operations teams that need consistent, reliable invoice records

## Business Requirements

- Create and manage Customers and Senders
- Create Invoices using:
  - Invoice and date details
  - Existing customer and sender data
  - One or more line items
- Calculate invoice totals accurately
- Generate and download invoice documents
- Support reporting and summaries over invoices and payments

## Architecture Overview
The application follows a Modular Monolith architecture. All modules are deployed as a single application, but each module has clear boundaries and responsibilities.

### Modules

- **Invoice Module**

    Handles invoices, line items, senders, and customers

- **Users Module**

    Manages user accounts, roles, and authentication

- **Payments Module**

    Tracks payment records, payment status, and payment methods

- **Reports Module**

    Generates summaries, reports, and dashboard metrics

## Database Design

The system uses a relational database to ensure data consistency and integrity.

Core tables include:

- senders
- customers
- invoices
- invoice_line_items
- payments
- users

Each module owns its data and is responsible for how it is stored and accessed.

## Domain Model

The application is designed around clear domain concepts that reflect real-world business meaning.

#### Invoice Domain
Represents the core invoicing logic of the system.

Key Concepts:
- Invoice: A business document issued to a customer by a sender
- InvoiceLineItem: An individual billed item on an invoice
- InvoiceStatus: Draft, Issued, Paid, Overdue, Cancelled

Responsibilities:
- Hold invoice details and dates
- Calculate totals from line items
- Enforce invoice lifecycle rules

#### Payments Domain
Represents how invoices are paid and tracked.

Key Concepts:
- Payment: A record of money received against an invoice
- PaymentMethod: Card, bank transfer, cash, etc.
- PaymentStatus: Pending, Completed, Failed

Responsibilities:
- Track payments linked to invoices
- Reflect payment state and history

#### Users Domain
Represents system users and access control.

Key Concepts:
- UserAccount: A registered user of the system
- Role: Defines permissions and access levels

Responsibilities:
- Manage user identity
- Control access to application features

#### Reports Domain
Represents analytical views derived from invoices and payments.

Key Concepts:
- InvoiceSummary: Aggregated invoice data
- PaymentSummary: Aggregated payment data
- DashboardMetrics: High-level business indicators

Responsibilities:
- Provide read-only views and summaries
- Support dashboards and reporting use cases

## Performance & Optimization

For detailed information about database query performance and optimizations used in this application, see the [Performance Benchmark Documentation](backend/docs/PERFORMANCE_BENCHMARK.md).
