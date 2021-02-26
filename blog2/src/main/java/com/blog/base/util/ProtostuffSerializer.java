package com.blog.base.util;


import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.util.Objects;

/**
 * @Auther: wanglu
 * @Date: 2019/5/18 14:45
 * @Description:
 */
public class ProtostuffSerializer implements RedisSerializer<Object> {
    private final Schema<ProtoWrapper> schema = RuntimeSchema.getSchema(ProtostuffSerializer.ProtoWrapper.class);
    private final ProtostuffSerializer.ProtoWrapper wrapper = new ProtostuffSerializer.ProtoWrapper();
    private final LinkedBuffer buffer = LinkedBuffer.allocate(512);

    private boolean isEmpty(byte[] data) {
        return Objects.isNull(data) || data.length == 0;
    }

    public ProtostuffSerializer() {
    }

    public byte[] serialize(Object t) throws SerializationException {
        if (Objects.isNull(t)) {
            return new byte[0];
        } else {
            this.wrapper.data = t;

            byte[] var2;
            try {
                var2 = ProtostuffIOUtil.toByteArray(this.wrapper, this.schema, this.buffer);
            } finally {
                this.buffer.clear();
            }

            return var2;
        }
    }

    public Object deserialize(byte[] bytes) throws SerializationException {
        if (this.isEmpty(bytes)) {
            return null;
        } else {
            ProtostuffSerializer.ProtoWrapper newMessage = (ProtostuffSerializer.ProtoWrapper) this.schema.newMessage();
            ProtostuffIOUtil.mergeFrom(bytes, newMessage, this.schema);
            return newMessage.data;
        }
    }

    private static class ProtoWrapper {
        public Object data;

        private ProtoWrapper() {
        }
    }
}