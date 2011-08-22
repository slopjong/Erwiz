#!/bin/sh

. "`dirname $0`/messages.sh"

ERVIZ_CMD="`dirname $0`/erviz.sh"

${ERVIZ_CMD} -h

#echo 1>&2
#echo ${MSG_PRESS_ANY_KEY_TO_EXIT} 1>&2
#read x #pause

exit 0

