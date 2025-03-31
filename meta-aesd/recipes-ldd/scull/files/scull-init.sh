#!/bin/sh

### BEGIN INIT INFO
# Provides:          scull
# Required-Start:    $remote_fs $syslog
# Required-Stop:     $remote_fs $syslog
# Default-Start:     2 3 4 5
# Default-Stop:      0 1 6
# Short-Description: Initialize SCULL module
# Description:       Loads the SCULL kernel module at boot time
### END INIT INFO

case "$1" in
  start)
    echo "Loading SCULL module..."
    modprobe scull
    # Wait for module to be loaded
    sleep 1

    # Find major number
    MAJOR=$(awk '$2=="scull" {print $1}' /proc/devices)

    if [ -n "$MAJOR" ]; then
        echo "SCULL major number: $MAJOR"

        # Create device nodes
        mknod /dev/scull0 c $MAJOR 0
        mknod /dev/scull1 c $MAJOR 1
        mknod /dev/scull2 c $MAJOR 2
        mknod /dev/scull3 c $MAJOR 3

        chmod 666 /dev/scull*
    else
        echo "Failed to find SCULL major number!"
    fi
    ;;

  stop)
    echo "Removing SCULL module..."
    rmmod scull

    # Remove device nodes
    rm -f /dev/scull*
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

