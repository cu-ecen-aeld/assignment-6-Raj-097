#!/bin/sh

### BEGIN INIT INFO
# Provides:          misc-modules
# Required-Start:    $remote_fs $syslog
# Required-Stop:     $remote_fs $syslog
# Default-Start:     2 3 4 5
# Default-Stop:      0 1 6
# Short-Description: Load Misc Modules at boot
# Description:       Loads hello and faulty kernel modules and creates /dev entry for faulty.
### END INIT INFO

MODULES_DIR="/lib/modules/$(uname -r)/"
FAULTY_DEV="/dev/faulty"

case "$1" in
  start)
    echo "Loading misc-modules..."
    
    # Load hello module
    modprobe hello || echo "Failed to load hello"

    # Load faulty module using insmod (modprobe may fail due to built-in kernel conflicts)
    insmod ${MODULES_DIR}/faulty.ko || echo "Failed to load faulty"

    # Get major number for faulty
    MAJOR=$(awk '$2=="faulty" {print $1}' /proc/devices)
    
    if [ -n "$MAJOR" ]; then
      echo "Creating faulty device node with major number $MAJOR..."
      rm -f $FAULTY_DEV  # Remove any existing node
      mknod $FAULTY_DEV c $MAJOR 0
      chmod 666 $FAULTY_DEV
    else
      echo "Failed to determine faulty major number"
    fi
    ;;

  stop)
    echo "Unloading misc-modules..."
    rmmod hello || echo "Failed to unload hello"
    rmmod faulty || echo "Failed to unload faulty"
    rm -f $FAULTY_DEV
    ;;

  restart)
    $0 stop
    $0 start
    ;;

  *)
    echo "Usage: $0 {start|stop|restart}"
    exit 1
    ;;
esac

exit 0

