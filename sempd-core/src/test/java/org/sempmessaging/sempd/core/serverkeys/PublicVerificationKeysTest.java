package org.sempmessaging.sempd.core.serverkeys;

import org.junit.Test;
import org.sempmessaging.libsemp.key.PublicVerificationKey;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

public class PublicVerificationKeysTest {
	@Test
	public void forEachDoesNotCallConsumerWhenKeysListIsEmpty() {
		PublicVerificationKeys keys = new PublicVerificationKeys(Collections.emptyList());

		keys.forEach((key) -> fail("The consumer must not be called when processing an empty list!"));
	}

	@Test
	public void forEachSendsKeysFromKeyListToConsumer() {
		PublicVerificationKey key1 = mock(PublicVerificationKey.class);
		PublicVerificationKey key2 = mock(PublicVerificationKey.class);
		List<PublicVerificationKey> resultList = new ArrayList<>();

		PublicVerificationKeys keys = new PublicVerificationKeys(Arrays.asList(key1, key2));

		keys.forEach((key) -> resultList.add(key));

		assertEquals(2, resultList.size());
		assertSame(key1, resultList.get(0));
		assertSame(key2, resultList.get(1));
	}
}