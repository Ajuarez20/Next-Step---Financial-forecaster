#!/usr/bin/env bash
set -euo pipefail

BASE_URL="http://localhost:8080"
EMAIL="locktest_$(date +%s)@example.com"
PASSWORD="CorrectPass123!"
WRONG_PASSWORD="__TOTALLY_WRONG__$(date +%s)__"

echo "== Registering test user: $EMAIL =="
register_resp=$(curl -s -w "\nHTTP_STATUS:%{http_code}" -X POST "$BASE_URL/register" \
  -H "Content-Type: application/json" \
  -d "{
    \"firstname\":\"Lock\",
    \"lastname\":\"Tester\",
    \"email\":\"$EMAIL\",
    \"password\":\"$PASSWORD\"
  }")
register_body="${register_resp%HTTP_STATUS:*}"
register_code="${register_resp##*HTTP_STATUS:}"
echo "Register -> HTTP $register_code"
echo "Body: $register_body"
echo

echo "== Running 10 failed login attempts (wrong password) =="
for i in $(seq 1 10); do
  resp=$(curl -s -w "\nHTTP_STATUS:%{http_code}" -X POST "$BASE_URL/login" \
    -H "Content-Type: application/json" \
    -d "{\"email\":\"$EMAIL\",\"password\":\"$WRONG_PASSWORD\"}")

  body="${resp%HTTP_STATUS:*}"
  code="${resp##*HTTP_STATUS:}"

  echo "Attempt $i -> HTTP $code"
  echo "Body: $body"
  echo "-----------------------------"
done

echo
echo "== Try correct password immediately after 10 failures (should be locked) =="
resp=$(curl -s -w "\nHTTP_STATUS:%{http_code}" -X POST "$BASE_URL/login" \
  -H "Content-Type: application/json" \
  -d "{\"email\":\"$EMAIL\",\"password\":\"$PASSWORD\"}")
body="${resp%HTTP_STATUS:*}"
code="${resp##*HTTP_STATUS:}"
echo "Immediate correct-login -> HTTP $code"
echo "Body: $body"

echo
echo "== Waiting 125 seconds for lock expiry =="
sleep 125

echo
echo "== Try correct password after wait (should succeed) =="
resp=$(curl -s -w "\nHTTP_STATUS:%{http_code}" -X POST "$BASE_URL/login" \
  -H "Content-Type: application/json" \
  -d "{\"email\":\"$EMAIL\",\"password\":\"$PASSWORD\"}")
body="${resp%HTTP_STATUS:*}"
code="${resp##*HTTP_STATUS:}"
echo "Post-wait correct-login -> HTTP $code"
echo "Body: $body"

echo
echo "== Test complete for user: $EMAIL =="
echo "Use this email to inspect DB row."
