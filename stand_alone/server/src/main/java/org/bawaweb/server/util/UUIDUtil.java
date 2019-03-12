/**
 * 
 */
package org.bawaweb.server.util;

import java.util.UUID;

/**
 * @author medhoran
 * Used for generating unique identifiers for card id
 */
public final class UUIDUtil {
	
	private UUIDUtil() {
		
	}

	public static String generateUUID() {
		return UUID.randomUUID().toString();
	}
}
