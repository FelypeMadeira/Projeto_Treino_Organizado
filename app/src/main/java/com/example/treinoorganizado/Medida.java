package com.example.treinoorganizado;

public class Medida {
    public double peso, peito, cintura, quadril, braco, coxa;
    public String data_registro;
    public String peso_evolucao,peito_evolucao, cintura_evolucao, quadril_evolucao, braco_evolucao, coxa_evolucao;

    public Medida(){}

    public double getPeso() {
        return peso;
    }
    public void setPeso(double peso) {
        this.peso = peso;
    }
    public double getPeito(){
        return peito;
    }
    public void setPeito(double peito){
        this.peito = peito;
    }
    public double getCintura(){
      return cintura;}

    public  void  setCintura(double cintura){
        this.cintura = cintura;
    }
    public double getBraco(){
        return braco;
    }
    public void setBraco(double braco){
        this.braco = braco;
    }
    public double getCoxa(){
        return coxa;
    }
    public void setCoxa(double coxa){
        this.coxa = coxa;
    }
    public double getQuadril(){
        return quadril;
    }
    public void setQuadril(double quadril){
        this.quadril = quadril;
    }


}
