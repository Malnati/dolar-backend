package br.com.malnati.dolar.entity;

import br.com.malnati.dolar.dto.CotacaoDolarDiaDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document("cotacaodolardia_cache")
public class CotacaoDolarDiaCache {

  /**
   * Cotação de compra
   */
  private String cotacaoCompra;

  /**
   * Cotação de venda
   */
  private String cotacaoVenda;

  /**
   * Data e Hora da Cotação
   */
  private Date dataHoraCotacao;

  /**
   * Data da cotação do dólar
   */
  private Date dataCotacao;

  /**
   * timestamp da requisição
   */
  private Date dataHoraRequisicao;

  /**
   * id da requisição
   */
  @Id
  private String id;
  private LocalDateTime generatedWhen;
  private List<CotacaoDolarDiaDto> cachedResults;
}
