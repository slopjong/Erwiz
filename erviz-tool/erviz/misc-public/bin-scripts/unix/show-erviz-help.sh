#!/bin/sh

MESSAGES="`dirname $0`/messages.sh"
if [ -f $MESSAGES ]; then
	. $MESSAGES
else
	. "/opt/erviz-*.*.*/bin/messages.sh"
fi

ERVIZ_CMD="`dirname $0`/erviz.sh"
if [ ! -f $ERVIZ_CMD ]; then
	ERVIZ_CMD="/opt/erviz-*.*.*/bin/erviz.sh"
fi

${ERVIZ_CMD} -h

#echo 1>&2
#echo ${MSG_PRESS_ANY_KEY_TO_EXIT} 1>&2
#read x #pause

exit 0

