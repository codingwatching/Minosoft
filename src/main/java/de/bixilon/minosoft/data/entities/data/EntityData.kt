/*
 * Minosoft
 * Copyright (C) 2020-2025 Moritz Zwerger
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see <https://www.gnu.org/licenses/>.
 *
 * This software is not affiliated with Mojang AB, the original developer of Minecraft.
 */

package de.bixilon.minosoft.data.entities.data

import de.bixilon.kutil.bit.BitByte.isBitMask
import de.bixilon.kutil.cast.CastUtil.cast
import de.bixilon.kutil.cast.CastUtil.unsafeCast
import de.bixilon.kutil.concurrent.lock.RWLock
import de.bixilon.minosoft.data.text.ChatComponent
import de.bixilon.minosoft.protocol.network.session.play.PlaySession
import de.bixilon.minosoft.util.logging.Log
import de.bixilon.minosoft.util.logging.LogLevels
import de.bixilon.minosoft.util.logging.LogMessageType
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap

class EntityData(
    val session: PlaySession,
    data: Int2ObjectOpenHashMap<Any?>? = null,
) {
    private val lock = RWLock.rwlock()
    private val data: Int2ObjectOpenHashMap<Any> = Int2ObjectOpenHashMap()
    private val observers: Int2ObjectOpenHashMap<MutableSet<(Any?) -> Unit>> = Int2ObjectOpenHashMap()
    private val observersLock = RWLock.rwlock()

    init {
        data?.let { merge(it) }
    }

    fun merge(data: Int2ObjectOpenHashMap<Any?>) {
        lock.lock()
        for ((index, value) in data) {
            set(index, value)
        }
        lock.unlock()
    }

    operator fun set(field: EntityDataField, value: Any?) {
        val index = session.registries.getEntityDataIndex(field) ?: return
        lock.lock()
        set(index, value)
        lock.unlock()
    }

    operator fun set(index: Int, value: Any?) {
        if (value == null) {
            this.data.remove(index)
        } else {
            this.data[index] = value
        }

        val watchers = observers[index] ?: return
        observersLock.acquire()
        for (watcher in watchers) {
            try {
                watcher.invoke(value)
            } catch (error: Throwable) {
                error.printStackTrace()
            }
        }
        observersLock.release()
    }

    @Suppress("NON_PUBLIC_CALL_FROM_PUBLIC_INLINE")
    inline fun <reified K> get(field: EntityDataField, default: K): K {
        val index = session.registries.getEntityDataIndex(field) ?: return default // field is not present (in this version)
        lock.acquire()
        try {
            val data = this.data[index] ?: return default
            if (data !is K) {
                if (data is Number) {
                    when (K::class) {
                        Byte::class -> return data.toByte() as K
                        Int::class -> return data.toInt() as K
                    }
                }
                Log.log(LogMessageType.OTHER, LogLevels.VERBOSE) { "Entity data $data can not be casted to ${K::class}" }
                return default
            }
            return data
        } finally {
            lock.release()
        }
    }

    fun getBoolean(field: EntityDataField, default: Boolean): Boolean {
        val data: Any = this.get(field, default)
        if (data is Boolean) {
            return data
        }
        if (data is Number) {
            return data == 0x01
        }
        Log.log(LogMessageType.OTHER, LogLevels.VERBOSE) { "Invalid boolean $data" }
        return default
    }

    fun getBitMask(field: EntityDataField, bitMask: Int, default: Byte): Boolean {
        val byte: Byte = get(field, default)
        return byte.toInt().isBitMask(bitMask)
    }

    fun getChatComponent(field: EntityDataField, default: Any?): ChatComponent {
        return ChatComponent.of(get(field, default))
    }

    fun <K> observe(field: EntityDataField, watcher: (value: K?) -> Unit) {
        val index = session.registries.getEntityDataIndex(field) ?: return // field not available
        observersLock.lock()
        this.observers.getOrPut(index) { mutableSetOf() }.add(watcher.unsafeCast()) // TODO: use weakref?
        observersLock.unlock()
    }

    inline operator fun <reified V> invoke(field: EntityDataField, default: V, noinline converter: ((Any) -> V)? = null): EntityDataDelegate<V> {
        val raw = this.get<Any?>(field, default)
        val value = if (raw != null) converter?.invoke(raw) ?: raw.cast<V>() else null

        return EntityDataDelegate(value.unsafeCast(), field, this, converter)
    }
}
