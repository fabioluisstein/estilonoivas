package loja.springboot.dto;

import java.util.Date;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.format.annotation.DateTimeFormat;

public class PagamentoDTO {

    private Long id;
    private String tipo;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Temporal(TemporalType.DATE)
    private Date data;
    private String fornecedor;
    private String moeda;
    private String origem;
    private Double valor;

    public PagamentoDTO(Long id, String moeda, String origem, Double valor) {
        this.id = id;

        this.moeda = moeda;
        this.origem = origem;
        this.valor = valor;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(String fornecedor) {
        this.fornecedor = fornecedor;
    }

    public String getMoeda() {
        return moeda;
    }

    public void setMoeda(String moeda) {
        this.moeda = moeda;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public String getOrigem() {
        return origem;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

}
