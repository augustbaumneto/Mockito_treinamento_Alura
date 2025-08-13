package testdatafactory;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public class InstanteGerador {

	public static Instant geraInstante (int ano, int mes, int dia) {
		
		LocalDate data = LocalDate.of(ano, mes, dia);
		
		return data.atStartOfDay(ZoneId.systemDefault()).toInstant();
		
		
	}
}
