package com.zjutjh.ijh.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import com.zjutjh.ijh.datastore.model.WeJhPreference
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

class WeJhPreferenceSerializer @Inject constructor() : Serializer<WeJhPreference> {

    override val defaultValue: WeJhPreference = WeJhPreference.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): WeJhPreference =
        try {
            WeJhPreference.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }

    override suspend fun writeTo(t: WeJhPreference, output: OutputStream) =
        t.writeTo(output)
}