package net.openhft.chronicle.bytes;

import net.openhft.chronicle.core.Jvm;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * @author Rob Austin.
 */
public interface BytesRingBuffer extends BytesRingBufferStats {

    Logger LOG = LoggerFactory.getLogger(BytesRingBuffer.class);

    /**
     * clears the ring buffer but moving the read position to the write position
     */
    void clear();


    /**
     * Inserts the specified element at the tail of this queue if it is possible to do so
     * immediately without exceeding the queue's capacity,
     *
     * @param bytes0 the {@code bytes0} that you wish to add to the ring buffer
     * @return returning {@code true} upon success and {@code false} if this queue is full.
     */
    boolean offer(@NotNull BytesStore bytes0) throws InterruptedException;

    /**
     * Retrieves and removes the head of this queue, or returns {@code null} if this queue is
     * empty.
     *
     * @return false if this queue is empty, or a populated buffer if the element was retried
     * @throws IllegalStateException is the {@code using} buffer is not large enough
     */
    boolean read(@NotNull Bytes using) throws
            InterruptedException,
            IllegalStateException;

    long readRemaining();

    @NotNull
    static BytesRingBuffer newInstance(@NotNull NativeBytesStore<Void> bytesStore) {
        try {
            @NotNull final Class<BytesRingBuffer> aClass = clazz();
            final Constructor<BytesRingBuffer> constructor = aClass.getDeclaredConstructor(BytesStore.class);
            return constructor.newInstance(bytesStore);
        } catch (Exception e) {
            LOG.error("This is a a commercial feature, please contact " +
                    "sales@higherfrequencytrading.com to unlock this feature.");

            throw Jvm.rethrow(e);
        }
    }

    @NotNull
    static Class<BytesRingBuffer> clazz() throws ClassNotFoundException {
        //noinspection AccessStaticViaInstance
        return (Class<BytesRingBuffer>) Class.forName(
                "software.chronicle.enterprise.queue.EnterpriseRingBuffer");
    }


    static long sizeFor(long capacity) {
        try {
            final Method sizeFor = clazz().getMethod("sizeFor", long.class);
            return (long) sizeFor.invoke(null, capacity);
        } catch (Exception e) {
            LOG.error("This is a a commercial feature, please contact " +
                    "sales@higherfrequencytrading.com to unlock this feature.");

            throw Jvm.rethrow(e);
        }

    }


}
