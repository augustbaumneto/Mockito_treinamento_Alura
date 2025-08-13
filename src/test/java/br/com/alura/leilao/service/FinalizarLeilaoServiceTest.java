package br.com.alura.leilao.service;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static testdatafactory.LeilaoGenerator.GeraListaLeiloes;
import br.com.alura.leilao.dao.LeilaoDao;
import br.com.alura.leilao.model.Lance;
import br.com.alura.leilao.model.Leilao;
import br.com.alura.leilao.model.Usuario;

class FinalizarLeilaoServiceTest {

	private FinalizarLeilaoService service;

	@Mock
	private LeilaoDao leilaoDao;
	@Mock
	private EnviadorDeEmails enviadoremails;

	private AutoCloseable createdmocks;

	@BeforeEach
	public void beforeEach() {
		createdmocks = MockitoAnnotations.openMocks(this);
		this.service = new FinalizarLeilaoService(leilaoDao, enviadoremails);

	}

	@Test
	public void deveriaFinalizarUmLeilao() {
		List<Leilao> leiloes = GeraListaLeiloes();

		Mockito.when(leilaoDao.buscarLeiloesExpirados()).thenReturn(leiloes);

		service.finalizarLeiloesExpirados();

		Leilao leilao = leiloes.get(0);

		assertTrue(leilao.isFechado());
		assertEquals(new BigDecimal(700), leilao.getLanceVencedor().getValor());

		Mockito.verify(leilaoDao).salvar(leilao);

	}

	@Test
	public void deveriaEnviarEmailParaVencedorDoLeilao() {
		List<Leilao> leiloes = GeraListaLeiloes();

		Mockito.when(leilaoDao.buscarLeiloesExpirados()).thenReturn(leiloes);

		service.finalizarLeiloesExpirados();

		Leilao leilao = leiloes.get(0);
		Lance lancevencedor = leilao.getLanceVencedor();

		Mockito.verify(enviadoremails).enviarEmailVencedorLeilao(lancevencedor);

	}

	@Test
	public void naoDeveriaEnviarEmailParaVencedorDoLeilaoEmCasoDeErroAoEncerrarOLeilao() {
		List<Leilao> leiloes = GeraListaLeiloes();

		Mockito
			.when(leilaoDao.buscarLeiloesExpirados())
			.thenReturn(leiloes);

		Mockito
			.when(leilaoDao.salvar(Mockito.any()))
			.thenThrow(RuntimeException.class);

		try {
			service.finalizarLeiloesExpirados();
		} catch (Exception e) {

		}

		Mockito.verifyNoInteractions(enviadoremails);

	}


}
