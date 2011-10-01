@echo off

if "X%GLOBUS_LOCATION%" == "X" goto nopath
goto path

:nopath

    echo ERROR: environment variable GLOBUS_LOCATION not defined
    goto end

:path

for %%i in ("%GLOBUS_LOCATION%\lib\*.jar") do call %GLOBUS_LOCATION%\etc\globus-devel-setcp.bat %%i
set CLASSPATH=.;%GLOBUS_LOCATION%;%GLOBUS_LOCATION%\build\classes;%CLASSPATH%

:end
