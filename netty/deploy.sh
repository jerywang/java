#!/bin/sh

# t: package type 
# c: clean

OUTPUT=output
PACKAGE_TYPE="zip war"
CLEAN_TYPE=""
GRADLE_OPTS=""
GRADLE_CMD="./gradlew"
AFTER_PACKAGE=""

function put2output() {
	IFS=" " read -a array <<< "$*"
	rm -rf ${OUTPUT}
	mkdir -p ${OUTPUT}
	for type in "${array[@]}"
	do
		echo "putting $type to ${OUTPUT}..."
		find . -name *.${type} | xargs -i mv {} $OUTPUT
	done
}

while getopts :ct:he:o: OPT
do
	case $OPT
	in
		t)	PACKAGE_TYPE=$OPTARG;;
		c)	CLEAN_TYPE="clean";;
		e)	AFTER_PACKAGE=$OPTARG;;
		o)	OUTPUT=$OPTARG;;
		h)
			echo 'cmd: sh deploy.sh -c -t zip -o output [dev|qa|online|sb]'
			echo "-c: clean or not default not clean"
			echo "-t: package type(zip|war|zip war) default [zip war]"
			echo "-o: output if output is empty then do nothing"
			echo "-e: (shell path) execute shell after package"
			echo "mode: dev|test|online|sb if is empty, no mode"
			exit 0;;
		\?) echo 'Usage: deploy [-c] [-t zip|war|zip war] [-o output] [-e shell] [mode]...';
			exit 100;;
	esac
done

shift $(($OPTIND - 1))
MODE=$1

if [ -n "$MODE" ]; then
	GRADLE_OPTS="$GRADLE_OPTS -Pconf=$MODE"
fi
if [ -n "$CLEAN_TYPE" ]; then
	GRADLE_OPTS="$GRADLE_OPTS $CLEAN_TYPE"
fi
if [ -n "$PACKAGE_TYPE" ]; then
	GRADLE_OPTS="$GRADLE_OPTS $PACKAGE_TYPE"
fi

echo "build with GRADLE_OPTS: $GRADLE_OPTS..."
echo "output: $OUTPUT"
$GRADLE_CMD $GRADLE_OPTS

if [ -n "$OUTPUT" ]; then
	put2output $PACKAGE_TYPE
fi

if [ -n "$AFTER_PACKAGE" ]; then
	echo "runnig $AFTER_PACKAGE..."
	sh $AFTER_PACKAGE
fi

