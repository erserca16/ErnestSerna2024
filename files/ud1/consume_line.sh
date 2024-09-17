FILENAME=$1
i=0
SLEEP_TIME=1

if [ $# -gt 1 ]; then
    SLEEP_TIME=$2
fi

cat $FILENAME | while read line
do
    echo "Process $$ - Line $i: $line"
    ((i++))
    sleep $SLEEP_TIME
done