#!/bin/bash

NAME="allez"
FLASKDIR=/home/erik/server
SOCKFILE=/home/erik/server/sock
USER=root
GROUP=root
NUM_WORKERS=3

echo "Starting $NAME"

# Create the run directory if it doesn't exist
RUNDIR=$(dirname $SOCKFILE)
test -d $RUNDIR || mkdir -p $RUNDIR

# Start your gunicorn
exec gunicorn server:app -b 0.0.0.0:8080 \
  --name $NAME \
  --workers $NUM_WORKERS \
  --user=$USER --group=$GROUP \
  --bind=unix:$SOCKFILE