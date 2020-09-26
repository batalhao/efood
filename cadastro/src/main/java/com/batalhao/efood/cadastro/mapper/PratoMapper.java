package com.batalhao.efood.cadastro.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import com.batalhao.efood.cadastro.dto.AdicionarPratoDTO;
import com.batalhao.efood.cadastro.dto.AtualizarPratoDTO;
import com.batalhao.efood.cadastro.dto.PratoDTO;
import com.batalhao.efood.cadastro.model.Prato;

@Mapper(componentModel = "cdi")
public interface PratoMapper {

  PratoDTO toDTO(Prato p);

  Prato toPrato(AdicionarPratoDTO dto);

  void toPrato(AtualizarPratoDTO dto, @MappingTarget Prato prato);

}
