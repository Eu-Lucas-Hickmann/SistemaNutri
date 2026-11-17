package dominio;

public enum Sexo {
    HOMEM('H'),
    MULHER('M'),
    OUTROS('O'),
    NAO_INFORMADO('U');

    private final char sigla;

    private Sexo(char sigla){
        this.sigla = sigla;
    }
    public char getSigla() {
        return sigla;
    }
}

