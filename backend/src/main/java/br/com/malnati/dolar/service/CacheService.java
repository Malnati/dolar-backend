package br.com.malnati.dolar.service;

import br.com.malnati.dolar.dto.CotacaoDolarDiaDto;
import br.com.malnati.dolar.entity.CotacaoDolarDiaCache;

import java.time.LocalDateTime;
import java.util.Date;

public interface CacheService {

  boolean isCacheStillValid(LocalDateTime generatedWhen);
  
  void saveCotacaoDolarDiaCache(CotacaoDolarDiaDto result);
  
  CotacaoDolarDiaCache findCotacaoDolarDiaCacheByDate(Date date);

}
