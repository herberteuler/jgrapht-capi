package org.jgrapht.nlib.api;

import java.util.HashMap;
import java.util.Map;

import org.graalvm.nativeimage.IsolateThread;
import org.graalvm.nativeimage.ObjectHandle;
import org.graalvm.nativeimage.ObjectHandles;
import org.graalvm.nativeimage.c.function.CEntryPoint;
import org.graalvm.word.WordFactory;
import org.jgrapht.nlib.Constants;
import org.jgrapht.nlib.Errors;
import org.jgrapht.nlib.Status;

public class MapAPI {

	private static ObjectHandles globalHandles = ObjectHandles.getGlobal();
	
	/**
	 * Create a map
	 * 
	 * @param thread the thread
	 * @return the handle
	 */
	@CEntryPoint(name = Constants.LIB_PREFIX + "map_create")
	public static ObjectHandle createMap(IsolateThread thread) {
		try {
			return globalHandles.create(new HashMap<>());
		} catch (Exception e) {
			Errors.setError(Status.GENERIC_ERROR);
			return WordFactory.nullPointer();
		}
	}

	@CEntryPoint(name = Constants.LIB_PREFIX + "map_long_double_put")
	public static void mapLongDoublePut(IsolateThread thread, ObjectHandle mapHandle, long key, double value) {
		try {
			Map<Long, Double> map = globalHandles.get(mapHandle);
			map.put(key, value);
		} catch (IllegalArgumentException e) {
			Errors.setError(Status.INVALID_REFERENCE);
		} catch (Exception e) {
			Errors.setError(Status.GENERIC_ERROR);
		}
	}

	@CEntryPoint(name = Constants.LIB_PREFIX + "map_long_double_get")
	public static double mapLongDoubleGet(IsolateThread thread, ObjectHandle mapHandle, long key) {
		try {
			Map<Long, Double> map = globalHandles.get(mapHandle);
			Double value = map.get(key);
			if (value == null) {
				Errors.setError(Status.MAP_NO_SUCH_KEY);
				return 0d;
			}
			return value;
		} catch (IllegalArgumentException e) {
			Errors.setError(Status.INVALID_REFERENCE);
		} catch (Exception e) {
			Errors.setError(Status.GENERIC_ERROR);
		}
		return 0d;
	}

	@CEntryPoint(name = Constants.LIB_PREFIX + "map_long_double_contains_key")
	public static boolean mapLongDoubleContains(IsolateThread thread, ObjectHandle mapHandle, long key) {
		try {
			Map<Long, Double> map = globalHandles.get(mapHandle);
			return map.containsKey(key);
		} catch (IllegalArgumentException e) {
			Errors.setError(Status.INVALID_REFERENCE);
		} catch (Exception e) {
			Errors.setError(Status.GENERIC_ERROR);
		}
		return false;
	}

}