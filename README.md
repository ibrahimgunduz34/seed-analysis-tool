# Tefas Fund Analysis Tool

## How To Use

* Start the project dependencies running `docker compose up -d` such as Redis and PostgreSQL
* Run one of the following commands you need.

**Note:** Do not forget to fill the database with initial data and complete the missing metadata and historical data using the synchronization commands specified below. 

## Commands

### 1. Synchronization Commands

#### MetaDataListSync:

```shell
$ mvn spring-boot:run \
-DskipTests=true \
-Dspring-boot.run.profiles=prod \
-Dspring-boot.run.jvmArguments="-Dtask=MetaDataListSync"
```

#### HistoricalDataListSync:

```shell
$ mvn spring-boot:run \
-DskipTests=true \
-Dspring-boot.run.profiles=prod \
-Dspring-boot.run.jvmArguments="-Dtask=HistoricalDataListSync"
```

#### HistoricalDataListSyncAll

```shell
$ mvn spring-boot:run \
-DskipTests=true \
-Dspring-boot.run.profiles=prod \
-Dspring-boot.run.jvmArguments="-Dtask=HistoricalDataListSyncAll" \
-Dspring-boot.run.arguments="2025-12-01 2025-12-19"
```

### 2. Report Commands

#### PeriodComparisonReport:

```shell
$ mvn spring-boot:run \
-DskipTests=true \
-Dspring-boot.run.profiles=prod \
-Dspring-boot.run.jvmArguments="-Dtask=PeriodComparisonReport" \
-Dspring-boot.run.arguments="BDS,AFT,MAC,NRC,IVY"
```

#### PeriodFundTypeComparisonReport:

##### Available report options:
* KARMA_SEMSIYE_FONU
* KIYMETLI_MADEN_SEMSIYE_FONU
* KATILIM_SEMSIYE_FONU
* DEGISKEN_SEMSIYE_FONU
* SERBEST_SEMSIYE_FONU
* FON_SEPETI_SEMSIYE_FONU
* BORCLANMA_ARACLARI_SEMSIYE_FONU
* HISSE_SENEDI_SEMSIYE_FONU
* PARA_PIYASASI_SEMSIYE_FONU

```shell
$ mvn spring-boot:run \
-DskipTests=true \
-Dspring-boot.run.profiles=prod \
-Dspring-boot.run.jvmArguments="-Dtask=PeriodFundTypeComparisonReport" \
-Dspring-boot.run.arguments="HISSE_SENEDI_SEMSIYE_FONU"
```

### Sample Report
```text
Analysis Period: 2024-12-16 to 2025-12-16
================================================================================================================================================================

┌──────────────┬──────────────┬──────────────┬──────────────┬──────────────┬─────────────┬─────────────┬─────────────┬─────────────┬─────────────┬─────────────┐
│Code          │Price Change %│Mean Return % │(+) Days %    │(-) Days %    │Avg. Gain %  │Avg Loss %   │MDD          │St. Dev      │Sharpe Ratio │Sortino Ratio│
├──────────────┼──────────────┼──────────────┼──────────────┼──────────────┼─────────────┼─────────────┼─────────────┼─────────────┼─────────────┼─────────────┤
│BDS           │0.85          │0.00          │0.63          │0.37          │0.01         │0.01         │-0.10        │0.01         │0.26         │0.24         │
├──────────────┼──────────────┼──────────────┼──────────────┼──────────────┼─────────────┼─────────────┼─────────────┼─────────────┼─────────────┼─────────────┤
│AFT           │0.61          │0.00          │0.62          │0.38          │0.01         │0.01         │-0.22        │0.02         │0.12         │0.11         │
├──────────────┼──────────────┼──────────────┼──────────────┼──────────────┼─────────────┼─────────────┼─────────────┼─────────────┼─────────────┼─────────────┤
│MAC           │0.17          │0.00          │0.52          │0.48          │0.01         │0.01         │-0.14        │0.01         │0.05         │0.05         │
├──────────────┼──────────────┼──────────────┼──────────────┼──────────────┼─────────────┼─────────────┼─────────────┼─────────────┼─────────────┼─────────────┤
│NRC           │0.02          │0.00          │0.51          │0.49          │0.01         │0.01         │-0.31        │0.02         │0.01         │0.01         │
├──────────────┼──────────────┼──────────────┼──────────────┼──────────────┼─────────────┼─────────────┼─────────────┼─────────────┼─────────────┼─────────────┤
│IVY           │0.36          │0.00          │0.58          │0.42          │0.01         │0.02         │-0.24        │0.02         │0.07         │0.07         │
└──────────────┴──────────────┴──────────────┴──────────────┴──────────────┴─────────────┴─────────────┴─────────────┴─────────────┴─────────────┴─────────────┘
Fon Performans & Risk Karşılaştırması
--------------------------------------------------
BDS    | ████████████████████ (Sharpe: 0.26, MDD: -9.92%, Return: 84.94%)
AFT    | ██████████░░░░░░░░░░ (Sharpe: 0.12, MDD: -21.94%, Return: 61.19%)
MAC    | ████████░░░░░░░░░░░░ (Sharpe: 0.05, MDD: -14.06%, Return: 17.04%)
IVY    | ██████░░░░░░░░░░░░░░ (Sharpe: 0.07, MDD: -23.97%, Return: 35.97%)
NRC    | █░░░░░░░░░░░░░░░░░░░ (Sharpe: 0.01, MDD: -31.26%, Return: 1.76%)

```

## Configuration

You can modify the performance calculation weights from the `performance` object in `src/main/resources/application.yaml` file.

```yaml
performance:
  funds:
    weight:
      sharpe: 0.4
      mdd: 0.4
      return-pct: 0.2
```

## Database Operations

To back up the database, run the following command

```shell
$ docker run --rm \
--network container:$(docker compose ps -q pgsql) \
postgres:17.0 \
pg_dump -h localhost -U appuser -F c appdb > backup.dump
```

To restore a backup previously generated, use the following command

```shell
docker run --rm \
--network container:$(docker compose ps -q pgsql) \
-v $(pwd)/backup.dump:/backup.dump \
postgres:17.0 \
pg_restore -h localhost -U appuser -d appdb /backup.dump
```

## REST API
Please check out [the API document](/openapi.yaml)

## How To Start The Project Locally:
You can start the application and its required dependencies locally by running the Docker Compose project with the following command:
```shell
$ docker compose up -d
```

### Example usage:
**Note:** Do not forgot to install `jq` for json formatting.

```shell
$ curl -XPOST \
-H "Content-Type:application/json" \
-d'{"codes": ["MAC", "NRC"], "startDate": "2025-09-01", "endDate": "2025-12-01"}' \
"http://localhost/funds/analysis" | jq
```

```json
[
  {
    "metaData": {
      "code": "MAC",
      "name": "MARMARA CAPITAL PORTFÖY HİSSE SENEDİ (TL) FONU (HİSSE SENEDİ YOĞUN FON)",
      "fundType": "Hisse Senedi Şemsiye Fonu",
      "currency": "TRY"
    },
    "statistics": {
      "priceChange": -0.0331378299,
      "mean": -0.0004338073,
      "weightOfPositiveDays": 0.5,
      "weightOfNegativeDays": 0.5,
      "avgGain": 0.0096850901,
      "avgLoss": 0.0105527046,
      "mdd": -0.0927548934,
      "stDev": 0.0137388537,
      "sharpeRatio": -0.0315752180,
      "sortino": -0.0315693314,
      "performanceRating": 100
    }
  },
  {
    "metaData": {
      "code": "NRC",
      "name": "NEO PORTFÖY BİRİNCİ DEĞİŞKEN FON",
      "fundType": "Değişken Şemsiye Fonu",
      "currency": "TRY"
    },
    "statistics": {
      "priceChange": -0.0508226582,
      "mean": -0.0006730497,
      "weightOfPositiveDays": 0.4444444444444444,
      "weightOfNegativeDays": 0.5555555555555556,
      "avgGain": 0.0139329404,
      "avgLoss": 0.0123770717,
      "mdd": -0.1294419487,
      "stDev": 0.0170118723,
      "sharpeRatio": -0.0395635288,
      "sortino": -0.0429784086,
      "performanceRating": 1
    }
  }
]
```