import com.google.common.base.Supplier;

import java.util.UUID;

/**
 * Supplies a random id
 */
public class IdSupplier implements Supplier<String> {
	@Override
	public String get() {
		return UUID.randomUUID().toString();
	}
}
