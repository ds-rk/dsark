@echo off
set dir="%cd%"
set HOME=%dir:"=%
echo Dir=%dir%
@echo Starting Mongodb server...
%HOME%\..\mongodb\bin\mongod --config %HOME%\..\mongodb\bin\mongod.cfg