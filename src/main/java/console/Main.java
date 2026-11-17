package console;

import dominio.Avaliacao;

public class Main {
    public static void main(String[] args){
        Avaliacao pessoa1 = new Avaliacao();

        pessoa1.setAltura(2.50);
        pessoa1.setPeso(500);

        System.out.println(pessoa1.calcularImc(pessoa1.getAltura(),pessoa1.getPeso()));
    }
}
