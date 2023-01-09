package com.zjutjh.ijh.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import com.zjutjh.ijh.datastore.model.LocalWeJhUser
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

class LocalWeJhUserSerializer @Inject constructor() : Serializer<LocalWeJhUser> {

    override val defaultValue: LocalWeJhUser = LocalWeJhUser.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): LocalWeJhUser =
        try {
            LocalWeJhUser.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }

    override suspend fun writeTo(t: LocalWeJhUser, output: OutputStream) =
        t.writeTo(output)
}