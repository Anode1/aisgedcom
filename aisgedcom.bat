@echo off
set CP=.
set CP=%CP%;./classes
set CP=%CP%;./aisgedcom.jar

set PATH=%PATH%;.\jre\bin\

java -classpath "%CP%;%CLASSPATH%" org.is.ais.gedcom.Main %1 %2 %3 %4 %5 %6 %7 %8 %9

