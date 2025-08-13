/**
 * 
 */
package br.com.alura.leilao.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import br.com.alura.leilao.dao.PagamentoDao;
import br.com.alura.leilao.model.Lance;
import br.com.alura.leilao.model.Leilao;
import br.com.alura.leilao.model.Pagamento;

import static testdatafactory.LeilaoGenerator.GeraUmLeilaoUmlance;
import static testdatafactory.InstanteGerador.geraInstante;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;

/**
 * 
 */
public class GeradorDePagamentoTest {

	private GeradorDePagamento gerador;

	
	AutoCloseable closable;
	
	@Mock
	private PagamentoDao pagamentoDao;
	
	@Mock
	private Clock clock;
	
	@Captor
	private ArgumentCaptor<Pagamento> captor;
	
	@BeforeEach
	public void beforeEach() {
		closable = MockitoAnnotations.openMocks(this);
		this.gerador = new GeradorDePagamento(pagamentoDao, clock);

	}
	
	@AfterEach
	public void release() throws Exception {
		closable.close();
	}
	
	@Test
	void deveriaCriarPagamentoParaVencedorDoLeilaoNaSegunda() {
			
		Instant instant = geraInstante(2020, 12, 7);
		
		validaCriacaoPagamentoParaVencedorDoLeilao(instant,1);
	}

	@Test
	void deveriaCriarPagamentoParaVencedorDoLeilaoNaTerca() {
			
		Instant instant = geraInstante(2020, 12, 8);
		
		validaCriacaoPagamentoParaVencedorDoLeilao(instant,1);
	}
	
	@Test
	void deveriaCriarPagamentoParaVencedorDoLeilaoNaQuarta() {
			
		Instant instant = geraInstante(2020, 12, 9);
		
		validaCriacaoPagamentoParaVencedorDoLeilao(instant,1);
	}
	
	@Test
	void deveriaCriarPagamentoParaVencedorDoLeilaoNaQuinta() {
			
		Instant instant = geraInstante(2020, 12, 10);
		
		validaCriacaoPagamentoParaVencedorDoLeilao(instant,1);
	}
	
	@Test
	void deveriaCriarPagamentoParaVencedorDoLeilaoNaSexta() {
			
		Instant instant = geraInstante(2020, 12, 11);
		
		validaCriacaoPagamentoParaVencedorDoLeilao(instant,3);
	}
	
	@Test
	void deveriaCriarPagamentoParaVencedorDoLeilaoNoSabado() {
			
		Instant instant = geraInstante(2020, 12, 12);
		
		validaCriacaoPagamentoParaVencedorDoLeilao(instant,2);
	}
	
	@Test
	void deveriaCriarPagamentoParaVencedorDoLeilaoNoDomingo() {
			
		Instant instant = geraInstante(2020, 12, 13);
		
		validaCriacaoPagamentoParaVencedorDoLeilao(instant,1);
	}
	
	
	private void validaCriacaoPagamentoParaVencedorDoLeilao(Instant dataconsiderada, int qtddiasparavenceresperada) {
		Leilao leilao = GeraUmLeilaoUmlance();
		Lance vencedor = leilao.getLances().get(0);
	
	
		
		Mockito.when(clock.instant()).thenReturn(dataconsiderada);
		Mockito.when(clock.getZone()).thenReturn(ZoneId.systemDefault());
		
		gerador.gerarPagamento(vencedor);
		
		
		Mockito.verify(pagamentoDao).salvar(captor.capture());
		
		Pagamento pagamento = captor.getValue();
		
		assertEquals(dataconsiderada.atZone(ZoneId.systemDefault()).toLocalDate().plusDays(qtddiasparavenceresperada), pagamento.getVencimento());
		assertEquals(vencedor.getValor(),pagamento.getValor());
		assertFalse(pagamento.getPago());
		assertEquals(pagamento.getUsuario(),vencedor.getUsuario());
		assertEquals(leilao,pagamento.getLeilao());
	}
	
}
