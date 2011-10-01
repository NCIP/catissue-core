
if ( ! $?GLOBUS_LOCATION ) then
    echo "ERROR: environment variable GLOBUS_LOCATION not defined"
    exit 1
endif

if ( ! -e "${GLOBUS_LOCATION}" ) then
    echo "ERROR: invalid GLOBUS_LOCATION set: $GLOBUS_LOCATION"
    exit 2
endif

set DIRLIBS=(${GLOBUS_LOCATION}/lib/*.jar)
set CP=".:${GLOBUS_LOCATION}:${GLOBUS_LOCATION}/build/classes"
foreach i (${DIRLIBS})
     set CP = ${CP}:"$i"
end

if ( ! $?CLASSPATH ) then
    setenv CLASSPATH ${CP}
else
    setenv CLASSPATH ${CP}:${CLASSPATH}
endif
