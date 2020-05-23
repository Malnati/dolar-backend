package br.com.malnati.dolar.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ErrorHandlerController implements ErrorController{

	@Override
	@RequestMapping("/error")
	@ResponseBody
	public String getErrorPath() {
		return   "<center>"
				+ "<h2>Ops... Something went wrong</h2>" 
				+ "<h3>Call to developer!</h3>"
				+ "</center>";
	}
}