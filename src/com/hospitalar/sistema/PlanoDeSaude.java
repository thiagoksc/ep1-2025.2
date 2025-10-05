package com.hospitalar.sistema;

import java.util.HashMap;
import java.util.Map;

public class PlanoDeSaude {
    private String nome;
    private Map<String, Double> descontosPorEspecialidade;
    private boolean cobreInternacaoCurta;

    public PlanoDeSaude(String nome, boolean cobreInternacaoCurta) {
        this.nome = nome;
        this.cobreInternacaoCurta = cobreInternacaoCurta;
        this.descontosPorEspecialidade = new HashMap<>();
    }
    public String getNome() {
        return nome;
    }
   public boolean isCobreInternacaoCurta() {
        return cobreInternacaoCurta;
   }

   public void adicionarOuAtualizarDesconto(String nomeEspecialidade, double percentual){
        if (percentual >= 0 && percentual <= 1) {
            this.descontosPorEspecialidade.put(nomeEspecialidade, percentual);
        } else{
            System.out.println("ERRO: Percentual de desconto deve ser entre 0.0 e 1.0 .");
        }
   }

   public double getDesconto(String nomeEspecialidade){
        return this.descontosPorEspecialidade.getOrDefault(nomeEspecialidade, 0.0);
   }

   public Map<String, Double> getDescontos(){
        return this.descontosPorEspecialidade;
   }
}
