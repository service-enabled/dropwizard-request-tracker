import org.junit.Test;

import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.text.IsEmptyString.isEmptyOrNullString;
import static org.junit.Assert.*;

public class IdSupplierTest {
	@Test
	public void suppliesAnId() throws Exception {
		IdSupplier idSupplier = new IdSupplier();
		String id = idSupplier.get();

		assertThat(id, not(isEmptyOrNullString()));
	}
}