package com.batalhao.efood.cadastro.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import com.batalhao.efood.cadastro.dto.AdicionarRestauranteDTO;
import com.batalhao.efood.cadastro.dto.AtualizarRestauranteDTO;
import com.batalhao.efood.cadastro.dto.RestauranteDTO;
import com.batalhao.efood.cadastro.model.Restaurante;

@Mapper(componentModel = "cdi")
public interface RestauranteMapper {

  @Mapping(target = "nome", source = "nomeFantasia")
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "dataCriacao", ignore = true)
  @Mapping(target = "dataAtualizacao", ignore = true)
  @Mapping(target = "localizacao.id", ignore = true)
  public Restaurante toRestaurante(AdicionarRestauranteDTO dto);

  @Mapping(target = "nome", source = "nomeFantasia")
  public void toRestaurante(AtualizarRestauranteDTO dto, @MappingTarget Restaurante restaurante);

  @Mapping(target = "nomeFantasia", source = "nome")
  @Mapping(target = "dataCriacao", dateFormat = "dd/MM/yyyy HH:mm:ss")
  @Mapping(target = "dataAtualizacao", dateFormat = "dd/MM/yyyy HH:mm:ss")
  public RestauranteDTO toRestauranteDTO(Restaurante r);

}

