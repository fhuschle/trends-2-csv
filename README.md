# trends-2-csv

Pull multiple google trends data for fixed country/region and export to csv

## Build

First of all you should build the project via maven

```shell
mvn clean install
```

without tests

```shell
mvn clean install -DskipTests
```

Afterwards you should find the result in target/trends-2-csv-&lt;version&gt;.jar

## Usage

```shell
java -jar target/trends-2-csv-<version>.jar <param>=<value>
```

e.g.

```shell
java -jar target/trends-2-csv-0.0.1-SNAPSHOT.jar keywords=corona,virus,putin,astrazeneca start=2019-01-01 end=2021-03-15 filename=export.csv
```

run in debug mode

```shell
 java -jar -Dlogging.level.root=DEBUG target/trends-2-csv-0.0.1-SNAPSHOT.jar keywords=corona,virus,putin,astrazeneca start=2019-01-01 end=2021-03-15 filename=export.csv
```

## Configuration

Configuration can be done using command-line arguments or environmental variables. If environment variables and command-line arguments are set, the command-line arguments are used.

| Command-line argument|Enviroment variable| Description   | Default value |Example | 
|----------------------|-------------------|---------------|---------------|--------|
|keywords              |TRENDS2CSV_KEYWORDS|array of target keywords|example1,example2|corona,virus|
|start                 |TRENDS2CSV_FILENAME|interval start|2019-09-01|2019-01-01|
|end                   |TRENDS2CSV_START   |interval end|today|2021-03-18|
|filename              |TRENDS2CSV_END     |name of the exported file|trends_export.csv|export.csv|

## Docker-compose

You could build and run the application via docker-compose.

```shell
docker-compose up
```

After a build/run the CSV result should be available in /result directory. To rebuild you could run

```shell
docker-compose up --build
```
