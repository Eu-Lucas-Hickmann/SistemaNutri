package dominio;

import excecao.DadoInvalidoException;

import java.time.LocalDate;

public class Avaliacao {
    private double altura;
    private double peso;
    private NivelAtividade nivel;
    private ClassificacaoImc imc;
    private LocalDate dataAtual;

    public ClassificacaoImc getImc() {
        return imc;
    }

    public void setImc(ClassificacaoImc imc) {
        this.imc = imc;
    }

    public Avaliacao(double altura, double peso) {
        this.altura = altura;
        this.peso = peso;
        //TODO removido o localdate temporariamente, adicionar futuramente
    }

    public NivelAtividade getNivel() {
        return nivel;
    }

    public void setNivel(NivelAtividade nivel) {
        this.nivel = nivel;
    }

    public Avaliacao() {

    }

    public double getAltura() {
        return altura;
    }

    public void setAltura(double altura) {
        if (altura < 0.50 || altura > 2.50) {
            throw new DadoInvalidoException("A altura está inválida: " + altura);
        }
        this.altura = altura;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        if (peso < 0 || peso > 500) {
            throw new DadoInvalidoException("O Peso está inválida: " + peso);
        }

        this.peso = peso;
    }

    public LocalDate getDataAtual() {
        return dataAtual;
    }

    public void setDataAtual(LocalDate dataAtual) {
        this.dataAtual = dataAtual;
    }

    public double calcularImc(double altura, double peso) {
        return peso / (altura * altura);
    }
}
