#!/usr/bin/env bash
set -Eeuo pipefail

mkdir -p smoke-test-results

capture_diagnostics() {
  adb logcat -d -v time > smoke-test-results/logcat.txt 2>&1 || true
  adb shell dumpsys activity activities > smoke-test-results/activities.txt 2>&1 || true
  adb shell dumpsys meminfo com.dskushwahagroup.shop > smoke-test-results/meminfo.txt 2>&1 || true
  adb shell screencap -p /sdcard/dskg-home.png >/dev/null 2>&1 && \
    adb pull /sdcard/dskg-home.png smoke-test-results/home.png >/dev/null 2>&1 || true
}
trap capture_diagnostics EXIT

gradle installDebug
adb logcat -c
adb shell am force-stop com.dskushwahagroup.shop || true
adb shell am start -W -n com.dskushwahagroup.shop/.MainActivity

launched=false
for attempt in $(seq 1 60); do
  if adb shell pidof com.dskushwahagroup.shop | tr -d '\r' | grep -q '[0-9]'; then
    launched=true
    break
  fi
  sleep 2
done

capture_diagnostics
if [[ "$launched" != "true" ]]; then
  echo "App process did not remain alive after launch. See uploaded logcat and activity dump."
  grep -E -A 35 -B 8 'FATAL EXCEPTION|AndroidRuntime|Fatal signal|Process: com.dskushwahagroup.shop' smoke-test-results/logcat.txt || true
  exit 1
fi

adb shell dumpsys activity activities > /tmp/dskg-activities.txt
if ! grep -E "mResumedActivity|topResumedActivity" /tmp/dskg-activities.txt | grep -q "com.dskushwahagroup.shop"; then
  echo "App process exists but MainActivity is not resumed."
  cat /tmp/dskg-activities.txt
  exit 1
fi

if grep -E -q 'FATAL EXCEPTION|Fatal signal' smoke-test-results/logcat.txt; then
  grep -E -A 45 -B 8 'FATAL EXCEPTION|Fatal signal' smoke-test-results/logcat.txt
  exit 1
fi

echo "Android 14 launch smoke test passed."
