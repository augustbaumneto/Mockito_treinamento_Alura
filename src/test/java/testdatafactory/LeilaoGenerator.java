package testdatafactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import br.com.alura.leilao.model.Lance;
import br.com.alura.leilao.model.Leilao;
import br.com.alura.leilao.model.Usuario;

public class LeilaoGenerator {

	// Cria uma lista de leiloes fakes para o mock
		public static List<Leilao> GeraListaLeiloes() {
			List<Leilao> lista = new ArrayList<>();

			Leilao leilao1 = new Leilao("Celular", new BigDecimal("500"), new Usuario("Fulano"));

			Lance primeiro = new Lance(new Usuario("Beltrano"), new BigDecimal("600"));
			Lance segundo = new Lance(new Usuario("Ciclano"), new BigDecimal("700"));

			leilao1.propoe(primeiro);
			leilao1.propoe(segundo);

			lista.add(leilao1);

			return lista;
		}
		
		public static Leilao GeraUmLeilaoUmlance() {

			Leilao leilao1 = new Leilao("Celular", new BigDecimal("500"), new Usuario("Fulano"));

			Lance primeiro = new Lance(new Usuario("Ciclano"), new BigDecimal("900"));

			leilao1.propoe(primeiro);

			return leilao1;
		}
	
}
