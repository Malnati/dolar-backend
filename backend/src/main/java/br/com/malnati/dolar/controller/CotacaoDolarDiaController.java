package br.com.malnati.dolar.controller;

import br.com.malnati.dolar.dto.CotacaoDolarDiaDto;
import br.com.malnati.dolar.service.CotacaoDolarDiaService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Date;
import java.lang.Exception;

@RestController
@RequestMapping("/cotacaodolardia")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CotacaoDolarDiaController {

  private final CotacaoDolarDiaService cotacaoDolarDiaService;

  @GetMapping("/{dt}")
  public CotacaoDolarDiaDto listByDate( @PathVariable(name = "dt") String dt) throws Exception{
    CotacaoDolarDiaDto result = null;
    try {
      result = cotacaoDolarDiaService.listByDate(dt);      
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
    return result;
  }

}
