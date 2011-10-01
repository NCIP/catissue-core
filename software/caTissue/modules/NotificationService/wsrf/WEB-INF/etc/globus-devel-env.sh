
if [ -z "${GLOBUS_LOCATION}" ]; then
    echo "ERROR: environment variable GLOBUS_LOCATION not defined"  1>&2
    return 1
fi

if [ ! -d "${GLOBUS_LOCATION}" ]; then
    echo "ERROR: invalid GLOBUS_LOCATION set: $GLOBUS_LOCATION" 1>&2
    return 1
fi

CP=.:${GLOBUS_LOCATION}:${GLOBUS_LOCATION}/build/classes
for i in ${GLOBUS_LOCATION}/lib/*.jar
do
   CP=$CP:"$i"
done

if [ -z "$CLASSPATH" ] ; then
  CLASSPATH=$CP
else
  CLASSPATH=$CP:$CLASSPATH
fi
export CLASSPATH
