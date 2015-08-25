@echo off
set CP=.
set CP=%CP%;./classes
set CP=%CP%;./lib/log4j.jar
set CP=%CP%;./aisgedcom.jar

set PATH=%PATH%;.\jre\bin\

REM -Xdebug -Xnoagent -Djava.compiler=NONE -Xms128M -Xmx1024M
start javaw -classpath "%CP%;%CLASSPATH%" org.is.ais.gedcom.gui.MainFrame %1 %2 %3 %4 %5 %6 %7 %8 %9

REM TODO: try a launcher such as http://jsmooth.sourceforge.net instead of .bat
