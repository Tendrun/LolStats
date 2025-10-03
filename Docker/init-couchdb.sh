#!/usr/bin/env sh
set -euo pipefail

COUCH_URL="${COUCH_URL:-http://couchdb:5984}"
COUCH_USER="${COUCH_USER:-admin}"
COUCH_PASSWORD="${COUCH_PASSWORD:-admin}"

proto="$(echo "$COUCH_URL" | cut -d: -f1)"
hostport="$(echo "$COUCH_URL" | cut -d/ -f3)"
auth_url="$proto://$COUCH_USER:$COUCH_PASSWORD@$hostport"

echo "Waiting for CouchDB at $COUCH_URL ..."
until curl -sf "$auth_url/"; do
  echo "CouchDB not ready yet, retrying..."
  sleep 2
done

for db in championdetails detailedmatches matches players; do
  echo "Ensuring database '$db' exists..."
  curl -sf -X PUT "$auth_url/$db" || true
done

echo "Init completed."
