package br.com.ezatta.model;

import java.io.Serializable;

public class EzattaProdutoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private EzattaProduto prodvo;

    public EzattaProduto getProdvo() {
        return prodvo;
    }

    public void setProdvo(EzattaProduto prodvo) {
        this.prodvo = prodvo;
    }
    
    @Override
    public String toString() {
        return prodvo.getNome() + ":  qtd.: " + prodvo.getQuantidade();
    }

    
}
