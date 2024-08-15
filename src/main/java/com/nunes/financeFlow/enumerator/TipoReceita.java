package com.nunes.financeFlow.enumerator;

public enum TipoReceita {

    SALARIO("SALARIO"),
    DIVIDENDOS("DIVIDENDOS"),
    ALUGUEL("ALUGUEL"),
    VENDA_DE_PRODUTOS("VENDA_DE_PRODUTOS"),
    JUROS("JUROS"),
    APOSENTADORIA("APOSENTADORIA"),
    FREENLANCE("FREENLANCE"),
    PENSAO("PENSAO"),
    RENDIMENTOS("RENDIMENTOS");

    private String tipoReceita;

    TipoReceita(String tipoReceita) {
        this.tipoReceita = tipoReceita;
    }

    public String getTipoReceita() {
        return tipoReceita;
    }
}
