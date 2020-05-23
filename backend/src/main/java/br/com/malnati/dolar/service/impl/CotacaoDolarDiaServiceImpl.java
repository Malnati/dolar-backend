
package br.com.malnati.dolar.service.impl;

import br.com.malnati.dolar.dto.CotacaoDolarDiaDto;
import br.com.malnati.dolar.entity.CotacaoDolarDiaCache;
import br.com.malnati.dolar.service.CacheService;
import br.com.malnati.dolar.service.CotacaoDolarDiaService;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter; 

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.codehaus.jackson.map.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.annotation.JsonFormat; 
import com.fasterxml.jackson.annotation.JsonProperty; 

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.xml.ws.ResponseWrapper;

import java.text.SimpleDateFormat; 
import java.time.LocalDateTime;
import java.text.ParseException;
import java.util.Locale;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CotacaoDolarDiaServiceImpl implements CotacaoDolarDiaService {
  
  private static final Logger logger = LoggerFactory.getLogger(CotacaoDolarDiaServiceImpl.class);
   
  private final RestTemplate restTemplate;
  private final CacheService cacheService;

  @Value("${dolar.url-pattern.cotacaodolardia}")
  private final String serviceUrl = "https://olinda.bcb.gov.br/olinda/servico/PTAX/versao/v1/odata/CotacaoDolarDia(dataCotacao=@dataCotacao)?@dataCotacao='%s'&$top=100&$skip=0&$format=json&$select=cotacaoCompra,cotacaoVenda,dataHoraCotacao";

  @Override
  public CotacaoDolarDiaDto listByDate(@NotNull String date) throws Exception{
    CotacaoDolarDiaDto result = null;
    Date dt = null;
    try {
      SimpleDateFormat cacheFormatter = new SimpleDateFormat("ddMMyyyy");
      dt = cacheFormatter.parse(date); 
      // gravando todos os requests para auditoria
      // CotacaoDolarDiaCache cache = cacheService.findCotacaoDolarDiaCacheByDate(dt);
      // if (cache != null) {
      //   result = cache.getCachedResults();
      // }      
      SimpleDateFormat requestFormatter = new SimpleDateFormat("MM-dd-yyyy");
      String dateStr = requestFormatter.format(dt);
      result = fetchFromRestService(dateStr, dt);
      cacheService.saveCotacaoDolarDiaCache(result);
      logger.info("Result cotacaoCompra: ", result.getCotacaoCompra());
    } catch (Exception e) {
      logger.error("CotacaoDolarDiaServiceImpl Error! " + e.getMessage(), e);
      throw e;
    }
    return result;
  }
  
  private CotacaoDolarDiaDto fetchFromRestService(String dateStr, Date date) throws ParseException {
    String url = String.format(serviceUrl, dateStr);
    logger.info("Raw URL: " + url);
    LocalDateTime dataHoraRequisicao = LocalDateTime.now();
    ResponseEntity<DTO> response =
        restTemplate.exchange(
            url, HttpMethod.GET, null, new ParameterizedTypeReference<DTO>() {});
    DTO dto = response.getBody();
    logger.info("Response: " + dto);
    CotacaoDolarDiaDto result = new CotacaoDolarDiaDto();
    dto.values.forEach(val -> {
      result.setDataCotacao(date);
      result.setDataHoraRequisicao(dataHoraRequisicao);
      result.setCotacaoCompra(val.getCotacaoCompra());
      result.setCotacaoVenda(val.getCotacaoVenda());
      try {
        SimpleDateFormat responseFormatter = new SimpleDateFormat("yyyy-MM-dd");
        Date tmp = responseFormatter.parse(val.getDataHoraCotacao());
        result.setDataHoraCotacao(tmp);  
      } catch (ParseException e) {
        logger.error("CotacaoDolarDiaServiceImpl Error! " + e.getMessage(), e);
      }      
    });
    return result;
  }
}

/**
 * {
 *  "@odata.context": "https://was-p.bcnet.bcb.gov.br/olinda/servico/PTAX/versao/v1/odata$metadata#_CotacaoDolarDia(cotacaoCompra,cotacaoVenda,dataHoraCotacao)",
 *  "value": [
 *    {
 *      "cotacaoCompra": 5.69620,
 *      "cotacaoVenda": 5.69680,
 *      "dataHoraCotacao": "2020-05-20 13:11:28.89"
 *    }
 *  ]
 * }
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
class DTO {

  @JsonProperty("@odata.context")  
  String context;

  @JsonProperty("value")  
  List<DTOValue> values = new ArrayList<DTOValue>();
}

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
class DTOValue {
  @JsonProperty("cotacaoCompra")  
  String cotacaoCompra;

  @JsonProperty("cotacaoVenda")  
  String cotacaoVenda;

  @JsonProperty("dataHoraCotacao")  
  String dataHoraCotacao;
}