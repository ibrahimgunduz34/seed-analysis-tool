package com.seed.fund.command;

public enum FundTypeEnum {
    KARMA_SEMSIYE_FONU("Karma Şemsiye Fonu"),
    KIYMETLI_MADEN_SEMSIYE_FONU("Kıymetli Madenler Şemsiye Fonu"),
    KATILIM_SEMSIYE_FONU("Katılım Şemsiye Fonu"),
    DEGISKEN_SEMSIYE_FONU("Değişken Şemsiye Fonu"),
    SERBEST_SEMSIYE_FONU("Serbest Şemsiye Fonu"),
    FON_SEPETI_SEMSIYE_FONU("Fon Sepeti Şemsiye Fonu"),
    BORCLANMA_ARACLARI_SEMSIYE_FONU("Borçlanma Araçları Şemsiye Fonu"),
    HISSE_SENEDI_SEMSIYE_FONU("Hisse Senedi Şemsiye Fonu"),
    PARA_PIYASASI_SEMSIYE_FONU("Para Piyasası Şemsiye Fonu");

    private final String value;

    FundTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {return value;}
}
