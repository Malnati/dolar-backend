package br.com.malnati.dolar.service.impl;

import br.com.malnati.dolar.dto.CotacaoDolarDiaDto;
import br.com.malnati.dolar.entity.CotacaoDolarDiaCache;
import br.com.malnati.dolar.repository.CotacaoDolarDiaCacheRepository;
import br.com.malnati.dolar.service.CacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CacheServiceImpl implements CacheService {

  @Value("${app.cache-ttl}")
  private Long defaultCacheTtl;

  private final CotacaoDolarDiaCacheRepository cotacaoDolarDiaCacheRepository;

  @Override
  public boolean isCacheStillValid(LocalDateTime generatedWhen) {
    final LocalDateTime expirationTime = generatedWhen.plusMinutes(defaultCacheTtl);
    return expirationTime.isAfter(LocalDateTime.now());
  }

  @Override
  public CotacaoDolarDiaCache findCotacaoDolarDiaCacheByDate(Date date) {
    Iterable<CotacaoDolarDiaCache> caches = cotacaoDolarDiaCacheRepository.findByDataCotacao(date);
    if (caches == null) {
      return null;
    }
    for (CotacaoDolarDiaCache cache : caches) {
      if (this.isCacheStillValid(cache.getGeneratedWhen())) return cache;
    }
    return null;
  }

  @Override
  public void saveCotacaoDolarDiaCache(CotacaoDolarDiaDto result) {
    ArrayList<CotacaoDolarDiaDto> results = new ArrayList<CotacaoDolarDiaDto>();
    results.add(result);
    CotacaoDolarDiaCache cache =
        CotacaoDolarDiaCache.builder() 
            .cachedResults(results)
            .generatedWhen(LocalDateTime.now())
            .build();
    cotacaoDolarDiaCacheRepository.save(cache);
  }
}
