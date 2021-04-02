/*
 * (C) Copyright 2020-2021, by Dimitrios Michail.
 *
 * JGraphT C-API
 *
 * See the CONTRIBUTORS.md file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0, or the
 * GNU Lesser General Public License v2.1 or later
 * which is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1-standalone.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR LGPL-2.1-or-later
 */
package org.jgrapht.capi.impl;

import org.graalvm.nativeimage.IsolateThread;
import org.graalvm.nativeimage.ObjectHandle;
import org.graalvm.nativeimage.ObjectHandles;
import org.graalvm.nativeimage.c.function.CEntryPoint;
import org.graalvm.nativeimage.c.type.CCharPointerPointer;
import org.graalvm.nativeimage.c.type.CDoublePointer;
import org.graalvm.nativeimage.c.type.CIntPointer;
import org.graalvm.nativeimage.c.type.CLongPointer;
import org.graalvm.nativeimage.c.type.CTypeConversion.CCharPointerHolder;
import org.graalvm.nativeimage.c.type.WordPointer;
import org.graalvm.word.PointerBase;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.util.Pair;
import org.jgrapht.alg.util.Triple;
import org.jgrapht.capi.Constants;
import org.jgrapht.capi.JGraphTContext.PPToIFunctionPointer;
import org.jgrapht.capi.JGraphTContext.PToLFunctionPointer;
import org.jgrapht.capi.JGraphTContext.Status;
import org.jgrapht.capi.error.StatusReturnExceptionHandler;
import org.jgrapht.capi.graph.ExternalRef;
import org.jgrapht.capi.graph.HashAndEqualsResolver;

public class HandlesApi {

	private static ObjectHandles globalHandles = ObjectHandles.getGlobal();

	/**
	 * Destroy a handle
	 * 
	 * @param thread the thread
	 * @param handle the handle
	 */
	@CEntryPoint(name = Constants.LIB_PREFIX + "handles_destroy", exceptionHandler = StatusReturnExceptionHandler.class)
	public static int destroy(IsolateThread thread, ObjectHandle handle) {
		globalHandles.destroy(handle);
		return Status.STATUS_SUCCESS.getCValue();
	}

	/**
	 * Create a reference to an external object.
	 * 
	 * @param thread    the isolate thread
	 * @param refPtr    external object pointer
	 * @param hashPtr   external object hash function pointer
	 * @param equalsPtr external object equals function pointer
	 * @param res       the resulting handle
	 * @return status code
	 */
	@CEntryPoint(name = Constants.LIB_PREFIX
			+ "handles_put2_ref", exceptionHandler = StatusReturnExceptionHandler.class)
	public static int put2RefHandle(IsolateThread thread, PointerBase refPtr, PToLFunctionPointer hashPtr,
			PPToIFunctionPointer equalsPtr, WordPointer res) {
		ExternalRef ref = new ExternalRef(refPtr, equalsPtr, hashPtr);
		if (res.isNonNull()) {
			res.write(globalHandles.create(ref));
		}
		return Status.STATUS_SUCCESS.getCValue();
	}

	/**
	 * Create a reference to an external object.
	 * 
	 * @param thread    the isolate thread
	 * @param refPtr    external object pointer
	 * @param hashPtr   external object hash function pointer
	 * @param equalsPtr external object equals function pointer
	 * @param res       the resulting handle
	 * @return status code
	 */
	@CEntryPoint(name = Constants.LIB_PREFIX + "handles_put_ref", exceptionHandler = StatusReturnExceptionHandler.class)
	public static int putRefHandle(IsolateThread thread, PointerBase refPtr, ObjectHandle hashEqualsResolverHandle,
			WordPointer res) {
		HashAndEqualsResolver resolver = globalHandles.get(hashEqualsResolverHandle);
		ExternalRef ref = resolver.toExternalRef(refPtr);
		if (res.isNonNull()) {
			res.write(globalHandles.create(ref));
		}
		return Status.STATUS_SUCCESS.getCValue();
	}

	/**
	 * Read attributes of an external reference to an object.
	 * 
	 * @param thread       the isolate thread
	 * @param refHandle    the handle to the external reference
	 * @param refPtrRes    the pointer
	 * @param hashPtrRes   the hash function pointer
	 * @param equalsPtrRes the equals function pointer
	 * @return
	 */
	@CEntryPoint(name = Constants.LIB_PREFIX + "handles_get_ref", exceptionHandler = StatusReturnExceptionHandler.class)
	public static int getRefHandle(IsolateThread thread, ObjectHandle refHandle, WordPointer refPtrRes,
			WordPointer hashPtrRes, WordPointer equalsPtrRes) {
		ExternalRef ref = globalHandles.get(refHandle);
		if (refPtrRes.isNonNull()) {
			refPtrRes.write(ref.getPtr());
		}
		if (hashPtrRes.isNonNull()) {
			hashPtrRes.write(ref.getHashPtr());
		}
		if (equalsPtrRes.isNonNull()) {
			equalsPtrRes.write(ref.getEqualsPtr());
		}
		return Status.STATUS_SUCCESS.getCValue();
	}

	/**
	 * Access a CCharPointerHolder which has been previously kept in the global
	 * handles.
	 * 
	 * @param thread
	 * @param handle
	 * @param res
	 * @return
	 */
	@CEntryPoint(name = Constants.LIB_PREFIX
			+ "handles_get_ccharpointer", exceptionHandler = StatusReturnExceptionHandler.class)
	public static int getHandleAsString(IsolateThread thread, ObjectHandle handle, CCharPointerPointer res) {
		CCharPointerHolder cstr = globalHandles.get(handle);
		if (cstr != null && res.isNonNull()) {
			res.write(cstr.get());
		}
		return Status.STATUS_SUCCESS.getCValue();
	}

	@CEntryPoint(name = Constants.LIB_PREFIX
			+ "handles_get_edge_pair", exceptionHandler = StatusReturnExceptionHandler.class)
	public static int getHandleAsEdgePair(IsolateThread thread, ObjectHandle handle, CIntPointer source,
			CIntPointer target) {
		Pair<Integer, Integer> pair = globalHandles.get(handle);
		if (pair != null) {
			if (source.isNonNull()) {
				source.write(pair.getFirst());
			}
			if (target.isNonNull()) {
				target.write(pair.getSecond());
			}
		}
		return Status.STATUS_SUCCESS.getCValue();
	}

	@CEntryPoint(name = Constants.LIB_PREFIX
			+ "handles_get_long_edge_pair", exceptionHandler = StatusReturnExceptionHandler.class)
	public static int getHandleAsLongEdgePair(IsolateThread thread, ObjectHandle handle, CLongPointer source,
			CLongPointer target) {
		Pair<Long, Long> pair = globalHandles.get(handle);
		if (pair != null) {
			if (source.isNonNull()) {
				source.write(pair.getFirst());
			}
			if (target.isNonNull()) {
				target.write(pair.getSecond());
			}
		}
		return Status.STATUS_SUCCESS.getCValue();
	}

	@CEntryPoint(name = Constants.LIB_PREFIX
			+ "handles_get_edge_triple", exceptionHandler = StatusReturnExceptionHandler.class)
	public static int getHandleAsEdgeTriple(IsolateThread thread, ObjectHandle handle, CIntPointer source,
			CIntPointer target, CDoublePointer weight) {
		Triple<Integer, Integer, Double> triple = globalHandles.get(handle);
		if (triple != null) {
			if (source.isNonNull()) {
				source.write(triple.getFirst());
			}
			if (target.isNonNull()) {
				target.write(triple.getSecond());
			}
			if (weight.isNonNull()) {
				weight.write(triple.getThird());
			}
		}
		return Status.STATUS_SUCCESS.getCValue();
	}

	@CEntryPoint(name = Constants.LIB_PREFIX
			+ "handles_get_long_edge_triple", exceptionHandler = StatusReturnExceptionHandler.class)
	public static int getHandleAsLongEdgeTriple(IsolateThread thread, ObjectHandle handle, CLongPointer source,
			CLongPointer target, CDoublePointer weight) {
		Triple<Long, Long, Double> triple = globalHandles.get(handle);
		if (triple != null) {
			if (source.isNonNull()) {
				source.write(triple.getFirst());
			}
			if (target.isNonNull()) {
				target.write(triple.getSecond());
			}
			if (weight.isNonNull()) {
				weight.write(triple.getThird());
			}
		}
		return Status.STATUS_SUCCESS.getCValue();
	}

	@CEntryPoint(name = Constants.LIB_PREFIX
			+ "handles_get_str_edge_triple", exceptionHandler = StatusReturnExceptionHandler.class)
	public static int getHandleAsStringEdgeTriple(IsolateThread thread, ObjectHandle handle, CCharPointerPointer source,
			CCharPointerPointer target, CDoublePointer weight) {
		Triple<CCharPointerHolder, CCharPointerHolder, Double> triple = globalHandles.get(handle);
		if (triple != null) {
			if (source.isNonNull()) {
				source.write(triple.getFirst().get());
			}
			if (target.isNonNull()) {
				target.write(triple.getSecond().get());
			}
			if (weight.isNonNull()) {
				weight.write(triple.getThird());
			}
		}
		return Status.STATUS_SUCCESS.getCValue();
	}

	@CEntryPoint(name = Constants.LIB_PREFIX + Constants.INT_ANY
			+ "handles_get_graphpath", exceptionHandler = StatusReturnExceptionHandler.class)
	public static int getHandleAsGraphPath(IsolateThread thread, ObjectHandle handle, CDoublePointer weightRes,
			CIntPointer startVertexRes, CIntPointer endVertexRes, WordPointer edgeItRes) {
		GraphPath<Integer, ?> gp = globalHandles.get(handle);
		if (weightRes.isNonNull()) {
			weightRes.write(gp.getWeight());
		}
		if (startVertexRes.isNonNull()) {
			startVertexRes.write(gp.getStartVertex());
		}
		if (endVertexRes.isNonNull()) {
			endVertexRes.write(gp.getEndVertex());
		}
		if (edgeItRes.isNonNull()) {
			edgeItRes.write(globalHandles.create(gp.getEdgeList().iterator()));
		}
		return Status.STATUS_SUCCESS.getCValue();
	}

	@CEntryPoint(name = Constants.LIB_PREFIX + Constants.LONG_ANY
			+ "handles_get_graphpath", exceptionHandler = StatusReturnExceptionHandler.class)
	public static int getHandleAsGraphPath(IsolateThread thread, ObjectHandle handle, CDoublePointer weightRes,
			CLongPointer startVertexRes, CLongPointer endVertexRes, WordPointer edgeItRes) {
		GraphPath<Long, ?> gp = globalHandles.get(handle);
		if (weightRes.isNonNull()) {
			weightRes.write(gp.getWeight());
		}
		if (startVertexRes.isNonNull()) {
			startVertexRes.write(gp.getStartVertex());
		}
		if (endVertexRes.isNonNull()) {
			endVertexRes.write(gp.getEndVertex());
		}
		if (edgeItRes.isNonNull()) {
			edgeItRes.write(globalHandles.create(gp.getEdgeList().iterator()));
		}
		return Status.STATUS_SUCCESS.getCValue();
	}

	@CEntryPoint(name = Constants.LIB_PREFIX + Constants.DREF_DREF
			+ "handles_get_graphpath", exceptionHandler = StatusReturnExceptionHandler.class)
	public static int getHandleAsGraphPath(IsolateThread thread, ObjectHandle handle, CDoublePointer weightRes,
			WordPointer startVertexRes, WordPointer endVertexRes, WordPointer edgeItRes) {
		GraphPath<ExternalRef, ExternalRef> gp = globalHandles.get(handle);
		if (weightRes.isNonNull()) {
			weightRes.write(gp.getWeight());
		}
		if (startVertexRes.isNonNull()) {
			startVertexRes.write(gp.getStartVertex().getPtr());
		}
		if (endVertexRes.isNonNull()) {
			endVertexRes.write(gp.getEndVertex().getPtr());
		}
		if (edgeItRes.isNonNull()) {
			edgeItRes.write(globalHandles.create(gp.getEdgeList().iterator()));
		}
		return Status.STATUS_SUCCESS.getCValue();
	}

}
