package br.com.malnati.dolar.service;

import br.com.malnati.dolar.dto.CotacaoDolarDiaDto;

import javax.validation.constraints.NotNull;

public interface CotacaoDolarDiaService {

    CotacaoDolarDiaDto listByDate(@NotNull String date) throws Exception;
}
