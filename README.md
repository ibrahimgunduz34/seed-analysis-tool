# Tefas Fund Analysis Tool


## Commands

You can find the supported commands below. To run the commands for the production environment, just run the commands with `-Dspring.profiles.active=prod` 
additionally.

### MetaDataListSync:

```shell
$ mvn spring-boot:run \
-DskipTests=true \
-Dspring-boot.run.profiles=prod \
-Dspring-boot.run.jvmArguments="-Dtask=MetaDataListSync"
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
$ mvn spring-boot:run \
-DskipTests=true \
-Dspring-boot.run.jvmArguments="-Dtask=HistoricalDataListSyncAll -Dspring.profiles.active=prod" \
-Dspring-boot.run.arguments="2025-12-01,2025-12-14"
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

## Sample Report
```text
Start date: 2025-03-02
End date: 2025-09-03
Report time: 2025-09-06

Fon                   Positive Days         Negative Days         Average Gain (%)      Average Loss (%)      Stdev (%)             Price Change (%)      Max Drawdown (%)      Sortino Ratio         Sharpe Ratio          
----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
AOY                   73 0.59%              50 0.41%              1.44                  1.33                  31.48                 43.37                 -16.53                2.58                  2.47                  
AFA                   72 0.58%              52 0.42%              1.04                  0.98                  25.48                 25.43                 -15.40                2.01                  1.92                  
AFV                   75 0.60%              49 0.40%              0.82                  0.82                  19.76                 22.47                 -14.02                1.99                  2.17                  


Fon Performans & Risk Karşılaştırması
--------------------------------------------------
AOY    | ██████████░░░░░░░░░░ (Sharpe: 2.47, MDD: -16.53%)
AFA    | █████░░░░░░░░░░░░░░░ (Sharpe: 1.92, MDD: -15.40%)
AFV    | ███████████████░░░░░ (Sharpe: 2.17, MDD: -14.02%)


Ozet:
--------------------------------------------------
En iyi risk-düzeltilmiş getiri (Sharpe): AOY 2.47
En yüksek fiyat değişimi: AOY 43.37%
En düşük fiyat değişimi: AFV 22.47%
En yüksek risk (MDD): AOY -16.53%
En düşük risk (MDD): AFV -14.02%
Tüm fonların ortalama volatilitesi: 25.57%
En iyi Sharpe/MDD dengesi olan ilk 3 fon:
 - AFV (Sharpe: 2.17, MDD: -14.02%) : Yüksek dalgalanma ve risk mevcut, yalnızca yüksek risk toleransı olan yatırımcılar için
 - AOY (Sharpe: 2.47, MDD: -16.53%) : Yüksek dalgalanma ve risk mevcut, yalnızca yüksek risk toleransı olan yatırımcılar için
 - AFA (Sharpe: 1.92, MDD: -15.40%) : Yüksek dalgalanma ve risk mevcut, yalnızca yüksek risk toleransı olan yatırımcılar için



Fon Yorumlari: 
--------------------------------------------------
AOY:
- Risk Değerlendirmesi: Yüksek Riskli, Dikkat: Maksimum kayıp yüksek (-16.53%)
- Getiri/Kayıp Dengesi: Getiri güçlü ancak dalgalı (Ortalama Kazanç: 1.44%, Ortalama Kayıp: 1.33%)
- Risk Düzeltilmiş Performans: Sharpe 2.4652670242, Sortino 2.5768026581 → Risk-düzeltilmiş performans dengeli
- Gün Bazlı Performans: Pozitif Gün Ağırlığı 59%, Negatif Gün Ağırlığı 41%
- Diğer Fonlarla Karşılaştırma: Liste içindeki en riskli fonlardan biri.

AFA:
- Risk Değerlendirmesi: Yüksek Riskli, Dikkat: Maksimum kayıp yüksek (-15.4%)
- Getiri/Kayıp Dengesi: Getiri güçlü ancak dalgalı (Ortalama Kazanç: 1.04%, Ortalama Kayıp: 0.98%)
- Risk Düzeltilmiş Performans: Sharpe 1.9198121802, Sortino 2.0085494800 → Risk-düzeltilmiş performans dengeli
- Gün Bazlı Performans: Pozitif Gün Ağırlığı 58%, Negatif Gün Ağırlığı 42%
- Diğer Fonlarla Karşılaştırma: Liste içindeki en riskli fonlardan biri.

AFV:
- Risk Değerlendirmesi: Orta Riskli, Dikkat: Maksimum kayıp yüksek (-14.02%)
- Getiri/Kayıp Dengesi: Getiri güçlü ve istikrarlı (Ortalama Kazanç: 0.82%, Ortalama Kayıp: 0.82%)
- Risk Düzeltilmiş Performans: Sharpe 2.1680807138, Sortino 1.9919565690 → Risk-düzeltilmiş performans dengeli
- Gün Bazlı Performans: Pozitif Gün Ağırlığı 60%, Negatif Gün Ağırlığı 40%
- Diğer Fonlarla Karşılaştırma: Ortalama veya iyi performans gösteren fon.

```