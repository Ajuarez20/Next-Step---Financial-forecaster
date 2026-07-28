#!/usr/bin/env bash
set -euo pipefail

BASE_URL="http://localhost:8080"
EMAIL="locktest_$(date +%s)@example.com"
PASSWORD="CorrectPass123!"
WRONG_PASSWORD="WrongPass999!"

echo "== Registering test user: $EMAIL =="
curl -s -X POST "$BASE_URL/register" \
  -H "Content-Type: application/json" \
  -d "{
    \"firstname\":\"Lock\",
    \"lastname\":\"Tester\",
    \"email\":\"$EMAIL\",
    \"password\":\"$PASSWORD\"
  }" >/dev/null || true

echo "== Running 10 failed login attempts =="
for i in $(seq 1 10); do
  code=$(curl -s -o /tmp/lock_resp.txt -w "%{http_code}" -X POST "$BASE_URL/login" \
    -H "Content-Type: application/json" \
    -d "{\"email\":\"$EMAIL\",\"password\":\"$WRONG_PASSWORD\"}")
  echo "Attempt $i -> HTTP $code"
done

echo "== Try correct password immediately after 10 failures (should be locked) =="
code=$(curl -s -o /tmp/lock_resp2.txt -w "%{http_code}" -X POST "$BASE_URL/login" \
  -H "Content-Type: application/json" \
  -d "{\"email\":\"$EMAIL\",\"password\":\"$PASSWORD\"}")
echo "Immediate correct-login after lock -> HTTP $code"

echo "== Waiting 125 seconds for lock to expire =="
sleep 125

echo "== Try correct password after wait (should succeed) =="
code=$(curl -s -o /tmp/lock_resp3.txt -w "%{http_code}" -X POST "$BASE_URL/login" \
  -H "Content-Type: application/json" \
  -d "{\"email\":\"$EMAIL\",\"password\":\"$PASSWORD\"}")
echo "Post-wait correct-login -> HTTP $code"

echo "== Done =="
