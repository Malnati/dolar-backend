package br.com.malnati.dolar.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.time.LocalDateTime;

/**
 * ## API
 * 
 * ### URL: https://olinda.bcb.gov.br/olinda/servico/PTAX/versao/v1/odata/CotacaoDolarDia(dataCotacao=@dataCotacao)?@dataCotacao='05-21-2020'&$top=100&$format=json&$select=cotacaoCompra,cotacaoVenda,dataHoraCotacao
 * ### JSON return sample:
 * {
 *     "@odata.context": "https://was-p.bcnet.bcb.gov.br/olinda/servico/PTAX/versao/v1/odata$metadata#_CotacaoDolarDia(cotacaoCompra,cotacaoVenda,dataHoraCotacao)",
 *     "value": [
 *         {
 *             "cotacaoCompra": 5.6013,
 *             "cotacaoVenda": 5.6019,
 *             "dataHoraCotacao": "2020-05-21 13:02:36.901"
 *         }
 *     ]
 * }  
 * 
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CotacaoDolarDiaDto {

  /**
   * Cotação de compra
   */
  @JsonProperty("cotacaoCompra")
  private String cotacaoCompra;

  /**
   * Cotação de venda
   */
  @JsonProperty("cotacaoVenda")
  private String cotacaoVenda;

  /**
   * Data e Hora da Cotação
   */
  @JsonProperty("dataHoraCotacao")
  private Date dataHoraCotacao;

  /**
   * Data da cotação do dólar
   */
  private Date dataCotacao;

  /**
   * timestamp da requisição
   */
  private LocalDateTime dataHoraRequisicao;

  /**
   * id da requisição
   */
  // private String id;
}
