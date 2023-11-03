package com.zjutjh.ijh.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import com.zjutjh.ijh.datastore.model.IJhPreference
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

class WeJhPreferenceSerializer @Inject constructor() : Serializer<IJhPreference> {

    override val defaultValue: IJhPreference = IJhPreference.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): IJhPreference =
        try {
            IJhPreference.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }

    override suspend fun writeTo(t: IJhPreference, output: OutputStream) =
        t.writeTo(output)
}