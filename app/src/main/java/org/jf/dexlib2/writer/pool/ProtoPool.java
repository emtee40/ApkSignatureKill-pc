/*
 * Copyright 2013, Google Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 *     * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following disclaimer
 * in the documentation and/or other materials provided with the
 * distribution.
 *     * Neither the name of Google Inc. nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.jf.dexlib2.writer.pool;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jf.dexlib2.iface.reference.MethodProtoReference;
import org.jf.dexlib2.util.MethodUtil;
import org.jf.dexlib2.writer.ProtoSection;

import java.util.Collection;
import java.util.List;

public class ProtoPool extends BaseIndexPool<MethodProtoReference>
        implements ProtoSection<CharSequence, CharSequence, MethodProtoReference,
        TypeListPool.Key<? extends Collection<? extends CharSequence>>> {

    public ProtoPool(@NonNull DexPool dexPool) {
        super(dexPool);
    }

    public void intern(@NonNull MethodProtoReference reference) {
        Integer prev = internedItems.put(reference, 0);
        if (prev == null) {
            dexPool.stringSection.intern(getShorty(reference));
            dexPool.typeSection.intern(reference.getReturnType());
            dexPool.typeListSection.intern(reference.getParameterTypes());
        }
    }

    @NonNull
    @Override
    public CharSequence getShorty(@NonNull MethodProtoReference reference) {
        return MethodUtil.getShorty(reference.getParameterTypes(), reference.getReturnType());
    }

    @NonNull
    @Override
    public CharSequence getReturnType(@NonNull MethodProtoReference protoReference) {
        return protoReference.getReturnType();
    }

    @Nullable
    @Override
    public TypeListPool.Key<List<? extends CharSequence>> getParameters(
            @NonNull MethodProtoReference methodProto) {
        return new TypeListPool.Key<List<? extends CharSequence>>(methodProto.getParameterTypes());
    }
}