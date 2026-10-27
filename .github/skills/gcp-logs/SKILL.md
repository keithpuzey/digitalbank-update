---
name: gcp-logs
description: Search GCP Cloud Logging for the prod API service (prod-simo-deployment). Use when something went wrong in production, when the user wants to check logs, find errors, or trace a request. Wraps `gcloud logging read` with sensible defaults for this project.
---

# GCP Logs

Search Cloud Logging for the `prod-simo-deployment` Kubernetes service.

**Project:** `esd-blazect-svcs-prd`  
**Cluster:** `svcs-prod` (us-east4)  
**Namespace:** `ns-env-prod`  
**Pod label:** `k8s-pod/role=simo`

## Quick start

```bash
gcloud logging read \
  'resource.type="k8s_container" AND resource.labels.project_id="esd-blazect-svcs-prd" AND resource.labels.location="us-east4" AND resource.labels.cluster_name="svcs-prod" AND resource.labels.namespace_name="ns-env-prod" AND labels."k8s-pod/role"="simo" AND severity>=ERROR' \
  --project=esd-blazect-svcs-prd \
  --limit=50 \
  --freshness=1h \
  --format='value(timestamp,severity,jsonPayload.message,textPayload)'
```

## Step 1 — Resolve intent

Translate the user's description into filter clauses:

| User says | Filter addition |
|-----------|----------------|
| "errors", "exceptions" | `severity>=ERROR` (default) |
| "warnings" | `severity>=WARNING` |
| "all logs" | _(drop severity clause)_ |
| keyword or message fragment | `AND (textPayload=~"<word>" OR jsonPayload.message=~"<word>")` |
| time window ("last 4 hours") | `--freshness=4h` |
| specific date/time | `--date-filter="..." ` e.g. `2026-06-12T10:00:00Z..2026-06-12T11:00:00Z` |

Default to `--freshness=1h` and `severity>=ERROR` when the user doesn't specify. Use `severity>=DEFAULT` to see all log levels including INFO/DEBUG.

## Step 2 — Build and run the query

Construct the full `gcloud logging read` command and run it via Bash.

**If authentication fails** (error contains "not authenticated", "credentials", or "permission denied"):
> Run `! gcloud auth login` or `! gcloud auth application-default login` to authenticate, then retry.

**Useful flags:**

| Flag | Purpose |
|------|---------|
| `--limit=N` | Max entries to return (default 50, raise to 200 for wider searches) |
| `--freshness=Xh` | How far back to look (`1h`, `6h`, `24h`, `7d`) |
| `--order=desc` | Newest first (default) |
| `--format=json` | Full structured output for deep inspection |

## Step 3 — Parse and surface findings

From the output:

1. **Group by severity** — list ERROR/CRITICAL first, then WARNING
2. **Deduplicate** — show the 5 most recent *unique* messages with their timestamps
3. **Stack traces** — if a stack trace appears, extract the top 3 frames and show them inline
4. **No results?** — automatically widen `--freshness` to `6h` and retry once; if still empty, tell the user

Present findings as a compact table:

```
TIMESTAMP            SEVERITY  MESSAGE
2026-06-12 10:43:12  ERROR     Cannot connect to database: connection refused
2026-06-12 10:41:05  ERROR     Unhandled exception in /api/suites: TypeError: ...
```

## Step 4 — Suggest next steps

After showing results, offer:
- Re-run with broader filter (`--freshness=24h`, lower severity, no keyword filter)
- Show full JSON for a specific entry (ask the user for the timestamp)
- Correlate with recent deploys: `gcloud container clusters describe svcs-prod --region=us-east4 --project=esd-blazect-svcs-prd`

## Pre-requisites

`gcloud` CLI must be installed and authenticated:

```bash
gcloud auth login
gcloud config set project esd-blazect-svcs-prd
```

Check auth status: `gcloud auth list`
