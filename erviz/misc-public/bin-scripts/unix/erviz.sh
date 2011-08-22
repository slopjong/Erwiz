#!/bin/sh

#  A wrapper for erviz-cui-*.*.*.jar

#  Tested on Ubuntu 8.04

. "`dirname $0`/messages.sh"
. "`dirname $0`/lang.sh"

CUI_JAR_NAME_PATTERN="erviz-cui-*.*.*.jar"
CUI_JAR_PATH_PATTERN="`dirname $0`/${CUI_JAR_NAME_PATTERN}";

CORE_JAR_NAME_PATTERN="erviz-core-*.*.*.jar"
CORE_JAR_PATH_PATTERN="`dirname $0`/${CORE_JAR_NAME_PATTERN}";

MAIN_CLASS=jp.gr.java_conf.simply.erviz.cui.Main

#  searches the last modified jar file (cui)
for CUI_JAR_NAME in `ls --sort=time ${CUI_JAR_PATH_PATTERN} 2> /dev/null`
do
	break
done

if [ "${CUI_JAR_NAME}" = "" ]
then
	echo ${MSG_ERROR}: ${MSG_JAR_NOT_FOUND} \(${CUI_JAR_NAME_PATTERN}\) 1>&2
	exit 1
fi

#  searches the last modified jar file (core)
for CORE_JAR_NAME in `ls --sort=time ${CORE_JAR_PATH_PATTERN} 2> /dev/null`
do
	break
done

if [ "${CORE_JAR_NAME}" = "" ]
then
	echo ${MSG_ERROR}: ${MSG_JAR_NOT_FOUND} \(${CORE_JAR_NAME_PATTERN}\) 1>&2
	exit 1
fi


#  executes the jar file
USER_PROPERTIES="-Duser.language=${USER_LANGUAGE} -Duser.country=${USER_COUNTRY}"
java ${USER_PROPERTIES} -cp "${CUI_JAR_NAME}":"${CORE_JAR_NAME}" ${MAIN_CLASS} "$@"

if [ $? -ge 1 ]
then
	exit 1
fi

exit 0

