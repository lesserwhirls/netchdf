# Netchdf Command Line Interface
5/24/2025

## Building

````
cd <your_build_dir>
clone https://github.com/JohnLCaron/netchdf.git
cd netchdf
./gradlew clean assemble
./gradlew :cli:uberJar
````

## Running ncdump

````
cd cli/build/libs
java -jar netchdf-uber.jar --filename your_file
````