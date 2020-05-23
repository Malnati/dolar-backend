package br.com.malnati.dolar.repository;

import br.com.malnati.dolar.entity.CotacaoDolarDiaCache;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface CotacaoDolarDiaCacheRepository extends CrudRepository<CotacaoDolarDiaCache, String> {

    Iterable<CotacaoDolarDiaCache> findByDataCotacao(Date date);
}
