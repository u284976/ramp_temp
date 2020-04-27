find -name "*.java" > sources.txt
javac -d classes -cp "./lib/*" @sources.txt -Xlint

rm ~/ramp/1-node/test/ -r
rm ~/ramp/2-node/test/ -r
rm ~/ramp/3-node/test/ -r
rm ~/ramp/4-node/test/ -r
rm ~/ramp/1-node/it/ -r
rm ~/ramp/2-node/it/ -r
rm ~/ramp/3-node/it/ -r
rm ~/ramp/4-node/it/ -r


cp ~/master-ramp/classes/test/ ~/ramp/1-node/ -r
cp ~/master-ramp/classes/test/ ~/ramp/2-node/ -r
cp ~/master-ramp/classes/test/ ~/ramp/3-node/ -r
cp ~/master-ramp/classes/test/ ~/ramp/4-node/ -r
cp ~/master-ramp/classes/it/ ~/ramp/1-node/ -r
cp ~/master-ramp/classes/it/ ~/ramp/2-node/ -r
cp ~/master-ramp/classes/it/ ~/ramp/3-node/ -r
cp ~/master-ramp/classes/it/ ~/ramp/4-node/ -r
