package dominio;

public enum ClassificacaoImc {
    BAIXO_PESO("Baixo peso", 0.0, 18.49),
    EUTROFIA("Eutrofia (peso adequado)", 18.5, 24.99),
    SOBREPESO("Sobrepeso", 25.0, 29.99),
    OBESO_I("Obesidade grau I", 30.0, 34.99),
    OBESO_II("Obesidade grau II", 35.0, 39.99),
    OBESO_III("Obesidade grau III", 40.0, Double.MAX_VALUE);

    private final String descricao;
    private final double min;
    private final double max;

    ClassificacaoImc(String descricao, double min, double max) {
        this.descricao = descricao;
        this.min = min;
        this.max = max;
    }

    public String getDescricao() {
        return descricao;
    }

    public static ClassificacaoImc classificar(double imc) {
        for (ClassificacaoImc faixa : values()) {
            if (imc >= faixa.min && imc <= faixa.max) {
                return faixa;
            }
        }
        throw new IllegalArgumentException("IMC inválido: " + imc);
    }
}





