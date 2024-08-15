package com.nunes.financeFlow.enumerator;

public enum TipoDespesa {

    ALIMENTACAO("ALIMENTAÇÃO"),
    MORADIA("MORADIA"),
    TRANSPORTE("TRANSPORTE"),
    LAZER("LAZER"),
    EDUCACAO("EDUCAÇÃO"),
    SAÚDE("SAÚDE"),
    VESTUARIO("VESTUARIO"),
    VIAGENS("VIAGENS"),
    DIVIDAS("DIVIDAS"),
    IMPREVISTOS("IMPREVISTOS");

    private String tipoDespesa;

    TipoDespesa(String tipoDespesa) {
        this.tipoDespesa = tipoDespesa;
    }

    public String getTipoDespesa() {
        return tipoDespesa;
    }
}
