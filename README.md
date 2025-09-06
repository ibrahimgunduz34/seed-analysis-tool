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

## Sample Report
```text
Start date: 2025-03-02
End date: 2025-09-03
Fon                   Positive Days         Negative Days         Average Gain (%)      Average Loss (%)      Stdev (%)             Price Change (%)      Max Drawdown (%)      Sortino Ratio         Sharpe Ratio          
----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
AOY                   73 0.59%              50 0.41%              1.44                  1.33                  31.48                 43.37                 -16.53                2.58                  2.47                  
AFA                   72 0.58%              52 0.42%              1.04                  0.98                  25.48                 25.43                 -15.40                2.01                  1.92                  
AFV                   75 0.60%              49 0.40%              0.82                  0.82                  19.76                 22.47                 -14.02                1.99                  2.17                  
ADP                   62 0.50%              62 0.50%              1.67                  1.54                  38.54                 4.67                  -30.23                0.42                  0.43                  
BDY                   75 0.61%              48 0.39%              1.02                  1.07                  24.01                 26.68                 -14.19                1.81                  2.11                  
AKU                   68 0.55%              56 0.45%              1.19                  1.20                  28.18                 12.01                 -17.37                0.88                  0.95                  
BUY                   72 0.58%              52 0.42%              0.95                  1.00                  22.94                 16.47                 -12.95                1.31                  1.46                  


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
- Diğer Fonlarla Karşılaştırma: Sharpe oranı diğer fonlara göre düşük.

AFV:
- Risk Değerlendirmesi: Orta Riskli, Dikkat: Maksimum kayıp yüksek (-14.02%)
- Getiri/Kayıp Dengesi: Getiri güçlü ve istikrarlı (Ortalama Kazanç: 0.82%, Ortalama Kayıp: 0.82%)
- Risk Düzeltilmiş Performans: Sharpe 2.1680807138, Sortino 1.9919565690 → Risk-düzeltilmiş performans dengeli
- Gün Bazlı Performans: Pozitif Gün Ağırlığı 60%, Negatif Gün Ağırlığı 40%
- Diğer Fonlarla Karşılaştırma: Ortalama veya iyi performans gösteren fon.

ADP:
- Risk Değerlendirmesi: Yüksek Riskli, Dikkat: Maksimum kayıp yüksek (-30.23%)
- Getiri/Kayıp Dengesi: Getiri güçlü ancak dalgalı (Ortalama Kazanç: 1.67%, Ortalama Kayıp: 1.54%)
- Risk Düzeltilmiş Performans: Sharpe 0.4315076847, Sortino 0.4221218949 → Risk-düzeltilmiş performans dengeli
- Gün Bazlı Performans: Pozitif Gün Ağırlığı 50%, Negatif Gün Ağırlığı 50%
- Diğer Fonlarla Karşılaştırma: Liste içindeki en riskli fonlardan biri.

BDY:
- Risk Değerlendirmesi: Orta Riskli, Dikkat: Maksimum kayıp yüksek (-14.19%)
- Getiri/Kayıp Dengesi: Kaybın getiriye oranı yüksek (Ortalama Kazanç: 1.02%, Ortalama Kayıp: 1.07%)
- Risk Düzeltilmiş Performans: Sharpe 2.1086293017, Sortino 1.8069767583 → Risk-düzeltilmiş performans dengeli
- Gün Bazlı Performans: Pozitif Gün Ağırlığı 61%, Negatif Gün Ağırlığı 39%
- Diğer Fonlarla Karşılaştırma: Ortalama veya iyi performans gösteren fon.

AKU:
- Risk Değerlendirmesi: Yüksek Riskli, Dikkat: Maksimum kayıp yüksek (-17.37%)
- Getiri/Kayıp Dengesi: Kaybın getiriye oranı yüksek (Ortalama Kazanç: 1.19%, Ortalama Kayıp: 1.2%)
- Risk Düzeltilmiş Performans: Sharpe 0.9534786678, Sortino 0.8789041995 → Risk-düzeltilmiş performans dengeli
- Gün Bazlı Performans: Pozitif Gün Ağırlığı 55%, Negatif Gün Ağırlığı 45%
- Diğer Fonlarla Karşılaştırma: Sharpe oranı diğer fonlara göre düşük.

BUY:
- Risk Değerlendirmesi: Orta Riskli, Dikkat: Maksimum kayıp yüksek (-12.95%)
- Getiri/Kayıp Dengesi: Kaybın getiriye oranı yüksek (Ortalama Kazanç: 0.95%, Ortalama Kayıp: 1.0%)
- Risk Düzeltilmiş Performans: Sharpe 1.4550164973, Sortino 1.3050688419 → Risk-düzeltilmiş performans dengeli
- Gün Bazlı Performans: Pozitif Gün Ağırlığı 58%, Negatif Gün Ağırlığı 42%
- Diğer Fonlarla Karşılaştırma: Sharpe oranı diğer fonlara göre düşük.



Fon Performans & Risk Karşılaştırması
-----------------------------
AOY    | ██████████████████░░ (Sharpe: 2.47, MDD: -16.53%)
AFA    | ████████████████░░░░ (Sharpe: 1.92, MDD: -15.40%)
AFV    | ██████████████████░░ (Sharpe: 2.17, MDD: -14.02%)
ADP    | █░░░░░░░░░░░░░░░░░░░ (Sharpe: 0.43, MDD: -30.23%)
BDY    | ██████████████████░░ (Sharpe: 2.11, MDD: -14.19%)
AKU    | ██████████░░░░░░░░░░ (Sharpe: 0.95, MDD: -17.37%)
BUY    | ███████████████░░░░░ (Sharpe: 1.46, MDD: -12.95%)


=== Özet Rapor ===

En iyi risk-düzeltilmiş getiri (Sharpe): AOY 2.47
En yüksek fiyat değişimi: AOY 43.37%
En düşük fiyat değişimi: ADP 4.67%
En yüksek risk (MDD): ADP -30.23%
En düşük risk (MDD): BUY -12.95%
Tüm fonların ortalama volatilitesi: 27.20%
En iyi Sharpe/MDD dengesi olan ilk 3 fon:
 - AFV (Sharpe: 2.17, MDD: -14.02%) : Yüksek dalgalanma ve risk mevcut, yalnızca yüksek risk toleransı olan yatırımcılar için
 - AOY (Sharpe: 2.47, MDD: -16.53%) : Yüksek dalgalanma ve risk mevcut, yalnızca yüksek risk toleransı olan yatırımcılar için
 - BDY (Sharpe: 2.11, MDD: -14.19%) : Yüksek dalgalanma ve risk mevcut, yalnızca yüksek risk toleransı olan yatırımcılar için

```