package com.deledzis.messenger.room.mapper

interface EntityMapper<T, V> {
    fun mapFromEntity(type: T): V
    fun mapToEntity(type: V): T
}