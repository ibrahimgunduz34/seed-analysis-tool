# Tefas Fund Analysis Tool


## Commands

You can find the supported commands below. To run the commands for the production environment, just run the commands with `-Dspring.profiles.active=prod` 
additionally.

### MetaDataListSync:

```shell
$ java \
  -Dspring.main.web-application-type=none \
  -Djava.rmi.server.hostname=localhost \
  -Dtask=MetaDataListSync \
  -jar target/seed.jar
```

### HistoricalDataListSync:

```shell
$ java \
  -Dspring.main.web-application-type=none \
  -Djava.rmi.server.hostname=localhost \
  -Dtask=HistoricalDataListSync \
  -jar target/seed.jar
```

### HistoricalDataListSyncAll

```shell
$ java \
  -Dspring.main.web-application-type=none \
  -Djava.rmi.server.hostname=localhost \
  -Dtask=HistoricalDataListSyncAll \
  -jar target/seed.jar "2025-03-01" "2025-09-02"
```

### GenerateReport:

```shell
$ java \
  -Dspring.main.web-application-type=none \
  -Djava.rmi.server.hostname=localhost \
  -Dtask=GenerateReport \
  -jar target/seed.jar \
  2025-03-02 \
  2025-09-02 \
  BDS,MAC
```

### GenerateReportAll:

```shell
$ java \
-Dspring.profiles.active=prod \
-Dspring.main.web-application-type=none \
-Djava.rmi.server.hostname=localhost \
-Dtask=GenerateReportAll \
-jar target/seed.jar \
2025-03-02 \
2025-09-02
```