package com.batalhao.efood.cadastro.dto;

import java.math.BigDecimal;
import com.batalhao.efood.cadastro.infra.DTO;
import com.batalhao.efood.cadastro.infra.ValidDTO;

@ValidDTO
public class AdicionarPratoDTO implements DTO {

  public String nome;

  public String descricao;

  public BigDecimal preco;

}
