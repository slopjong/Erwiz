#!/bin/sh

# language selection -------------------------------------------------------------------------------
echo Select language number. \(Enter \'q\' to quit.\)
echo
echo 1: English
echo 2: Japanese
echo

while :
do
	echo -n "Language: "
	read LANG_NUM
	
	if [ "${LANG_NUM}" = "Q" -o "${LANG_NUM}" = "q" ]
	then
		exit 0
	elif [ "${LANG_NUM}" = "1" ]
	then
		LANG_SELECT=en
		break
	elif [ "${LANG_NUM}" = "2" ]
	then
		LANG_SELECT=ja
		break
	else
		continue
	fi
done


# change directory ---------------------------------------------------------------------------------
PREV_CD=`pwd`
cd "`dirname $0`"


# copy files ---------------------------------------------------------------------------------------
SETUP_DIR=_setup_
TARGET_DIR_LIST_1="common unix ${LANG_SELECT} ${LANG_SELECT}-unix"
TARGET_DIR_LIST_2="bin docs html-docs work"

for TARGET_DIR_1 in ${TARGET_DIR_LIST_1}
do
	for TARGET_DIR_2 in ${TARGET_DIR_LIST_2}
	do
		SRC_DIR=./${SETUP_DIR}/${TARGET_DIR_1}/${TARGET_DIR_2}
		DST_DIR=.
		
		if [ -e "${SRC_DIR}" ]
		then
			echo copying... [${SRC_DIR}/*] -\> [${DST_DIR}/${TARGET_DIR_2}/*]
			
			if [ ! -e "${DST_DIR}" ]
			then
				mkdir "${DST_DIR}"
			fi
			
			cp -R "${SRC_DIR}" "${DST_DIR}"
			if [ $? -gt 0 ]; then
				echo
				echo Error
				cd ${PREV_CD}
				exit 1
			fi
			
		fi
	
	done
done


# iconv --------------------------------------------------------------------------------------------
if [ ${LANG_SELECT} = ja ]
then
	case ${LANG} in
		ja_JP.eucJP | ja_JP.EUC | ja_JP.ujis | ja_JP.EUC-JP)
			TO_CODE=EUC-JP;;
		ja_JP.SJIS | ja_JP.PCK)
			TO_CODE=SJIS;;
		*)
			TO_CODE=UTF-8
			;;
	esac
	
	if [ ! ${TO_CODE} = UTF-8 ]
	then
		MSG_SH="./bin/message.sh"
		echo converting... [${MSG_SH}] \(UTF-8 -\> ${TO_CODE}\)
		iconv -f UTF-8 -t ${TO_CODE} < "${MSG_SH}" > "${MSG_SH}~"
		cp "${MSG_SH}~" "${MSG_SH}"
		rm "${MSG_SH}~"
	fi
fi


# set permission (sh only) -------------------------------------------------------------------------
for DIR_NAME in bin work/scripts
do
	echo permission changing... [./${DIR_NAME}/*.sh]
	chmod u+x `find ./${DIR_NAME} -name "*.sh"`
done


# copy/move files ----------------------------------------------------------------------------------

# ./work/scripts -> ./work
for FILE_NAME in text2png-ie.sh text2png-idef1x.sh
do
	SRC=./work/scripts/${FILE_NAME}
	DST=./work/${FILE_NAME}
	
	echo copying... [${SRC}] -\> [${DST}]
	cp "${SRC}" "${DST}"
done

# move readme.txt
FILE_NAME="readme.txt"
SRC=./docs/${FILE_NAME}
DST=./${FILE_NAME}
echo moving... [${SRC}] -\> [${DST}]
mv "${SRC}" "${DST}"


# completed message --------------------------------------------------------------------------------
echo
echo Completed

README="./readme.txt"
echo
echo See ${README}

#echo -n "Show now? (y/N): "
#read YN
#if [ "${YN}" = "Y" -o "${YN}" = "y" ]
#then
#	less ${README}
#fi

cd ${PREV_CD}
exit 0

