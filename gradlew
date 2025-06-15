#!/usr/bin/env sh

# Licensed to the Apache Software Foundation (ASF) under one
# or more contributor license agreements.  See the NOTICE file
# distributed with this work for additional information
# regarding copyright ownership.  The ASF licenses this file
# to you under the Apache License, Version 2.0 (the
# "License"); you may not use this file except in compliance
# with the License.  You may obtain a copy of the License at
#
#     https://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

##############################################################################
##
##  Gradle start up script for UN*X
##
##############################################################################

# Determine the Java command to use to start the JVM.
if [ -n "$JAVA_HOME" ] ; then
    if [ -x "$JAVA_HOME/jre/bin/java" ] ; then
        JAVACMD="$JAVA_HOME/jre/bin/java"
    else
        JAVACMD="$JAVA_HOME/bin/java"
    fi
    if [ ! -x "$JAVACMD" ] ; then
        die "ERROR: JAVA_HOME is set to an invalid directory: $JAVA_HOME

Please set the JAVA_HOME variable in your environment to match the
location of your Java installation."
    fi
else
    JAVACMD="java"
    which java >/dev/null || die "ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.

Please set the JAVA_HOME variable in your environment to match the
location of your Java installation."
fi

# Determine the project's root directory.
APP_HOME=$(dirname "$0")

# Add default JVM options here. You can also use JAVA_OPTS and GRADLE_OPTS to pass JVM options to this script.
DEFAULT_JVM_OPTS=""

# Collect all arguments for the Java command.
CMD_LINE_ARGS=""

# Add JVM options.
for arg in "$@"; do
    if expr "$arg" : "-D.*" >/dev/null || expr "$arg" : "-X.*" >/dev/null || expr "$arg" : "-javaagent.*" >/dev/null || expr "$arg" : "--add-opens.*" >/dev/null || expr "$arg" : "--add-exports.*" >/dev/null; then
        CMD_LINE_ARGS="$CMD_LINE_ARGS $arg"
        shift
    else
        break
    fi
done

# Add Gradle-specific options.
CMD_LINE_ARGS="$CMD_LINE_ARGS -Dorg.gradle.appname=$(basename "$0") -classpath "$APP_HOME/gradle/wrapper/gradle-wrapper.jar" org.gradle.wrapper.GradleWrapperMain"

# Add remaining arguments to the Java command.
CMD_LINE_ARGS="$CMD_LINE_ARGS $@"

# Execute the Java command.
exec "$JAVACMD" $DEFAULT_JVM_OPTS $JAVA_OPTS $GRADLE_OPTS $CMD_LINE_ARGS

# A utility function to print an error message and exit.
die() {
    echo "$@"
    exit 1
}