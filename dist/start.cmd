@echo off

java -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=8000 -jar spigot.jar
pause