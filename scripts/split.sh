#!/bin/sh

mediaFile=$1;
chunkSize=${2:-1500k}
chunksDir="output";
basename=$(basename "$mediaFile")
#chunkPrefix="$basename"".chunk.";
chunkPrefix=o;


if [ $# -lt 1 ]
then
echo "Usage: $0 mediafile [chunksize]";
exit 1;
fi

split -a 1 -d -b $chunkSize "$mediaFile" $chunksDir/"$chunkPrefix"

if [ $? -eq 0 ]
then
echo "Successfully splitted "$mediaFile" to $chunksDir/$chunkPrefix"*""
else
echo "Something is wrong because the split utility exitted with error code: "$?
fi
