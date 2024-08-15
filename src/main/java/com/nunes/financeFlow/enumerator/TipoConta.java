package com.nunes.financeFlow.enumerator;


//enumeração que define os tipos de conta
public enum TipoConta {

    CONTA_CORRENTE("CONTA CORRENTE"),
    CONTA_POUPANCA("CONTA POUPANÇA");

    //atributo
    private String tipoConta;

    //construtor
    TipoConta(String tipoConta) {
        this.tipoConta = tipoConta;
    }

    // getter para acessar o valor
    public String getTipoConta() {
        return tipoConta;
    }
}
