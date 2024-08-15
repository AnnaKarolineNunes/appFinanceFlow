package com.nunes.financeFlow.enumerator;


//enumeração que define os tipos de categoria
public enum TipoCategoria {

    RECEITA("RECEITA"),
    DESPESA("DESPESA");


    // atributo
    private String tipoCategoria;

    //construtor
    TipoCategoria(String tipoCategoria) {
        this.tipoCategoria = tipoCategoria;
    }

    //get para pegar o tipo de categoria
    public String getTipoCategoria() {
        return tipoCategoria;
    }
}
