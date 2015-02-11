mkdir -p classes
mkdir -p build/java
cd src/
#javac -d "../classes" -classpath ../jsyn_16_7_0.jar fap_java/*
/c/"Program Files"/Java/jdk1.8.0_31/bin/javac -d "../classes" fap_java/*
cd ../classes
jar cf0m ../FapJava.jar ../addmanifest *
cd ..