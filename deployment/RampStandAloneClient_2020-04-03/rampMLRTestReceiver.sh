#!/bin/bash

#ulimit -n 10240
#"$JAVA_HOME"/bin/java -Djava.security.egd=file:/dev/./urandom -classpath .:./lib/*: it.unibo.deis.lia.ramp.RampEntryPoint &
#echo $! > $PID_FILE

java -classpath .:./lib/* test.sdncontroller.ControllerClientMLRTestReceiver
