/*
 *
 *  *  Copyright 2010-2016 OrientDB LTD (http://orientdb.com)
 *  *
 *  *  Licensed under the Apache License, Version 2.0 (the "License");
 *  *  you may not use this file except in compliance with the License.
 *  *  You may obtain a copy of the License at
 *  *
 *  *       http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  *  Unless required by applicable law or agreed to in writing, software
 *  *  distributed under the License is distributed on an "AS IS" BASIS,
 *  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  See the License for the specific language governing permissions and
 *  *  limitations under the License.
 *  *
 *  * For more information: http://orientdb.com
 *
 */

package com.orientechnologies.common.serialization.types;

import com.orientechnologies.orient.core.storage.impl.local.paginated.wal.OWALChanges;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;

/**
 * Serializer for {@link BigDecimal} type.
 *
 * @author Andrey Lomakin (a.lomakin-at-orientdb.com)
 * @since 03.04.12
 */
public class ODecimalSerializer implements OBinarySerializer<BigDecimal> {

  public static final ODecimalSerializer INSTANCE = new ODecimalSerializer();
  public static final byte ID = 18;

  public int getObjectSize(BigDecimal object, Object... hints) {
    return OIntegerSerializer.INT_SIZE
        + OBinaryTypeSerializer.INSTANCE.getObjectSize(object.unscaledValue().toByteArray());
  }

  public int getObjectSize(byte[] stream, int startPosition) {
    return OIntegerSerializer.INT_SIZE
        + OBinaryTypeSerializer.INSTANCE.getObjectSize(
            stream, startPosition + OIntegerSerializer.INT_SIZE);
  }

  public void serialize(BigDecimal object, byte[] stream, int startPosition, Object... hints) {
    OIntegerSerializer.INSTANCE.serializeLiteral(object.scale(), stream, startPosition);
    startPosition += OIntegerSerializer.INT_SIZE;
    OBinaryTypeSerializer.INSTANCE.serialize(
        object.unscaledValue().toByteArray(), stream, startPosition);
  }

  public BigDecimal deserialize(final byte[] stream, int startPosition) {
    final int scale = OIntegerSerializer.INSTANCE.deserializeLiteral(stream, startPosition);
    startPosition += OIntegerSerializer.INT_SIZE;

    final byte[] unscaledValue = OBinaryTypeSerializer.INSTANCE.deserialize(stream, startPosition);

    return new BigDecimal(new BigInteger(unscaledValue), scale);
  }

  public byte getId() {
    return ID;
  }

  public int getObjectSizeNative(final byte[] stream, final int startPosition) {
    return OIntegerSerializer.INT_SIZE
        + OBinaryTypeSerializer.INSTANCE.getObjectSizeNative(
            stream, startPosition + OIntegerSerializer.INT_SIZE);
  }

  @Override
  public void serializeNativeObject(
      BigDecimal object, byte[] stream, int startPosition, Object... hints) {
    OIntegerSerializer.INSTANCE.serializeNative(object.scale(), stream, startPosition);
    startPosition += OIntegerSerializer.INT_SIZE;
    OBinaryTypeSerializer.INSTANCE.serializeNativeObject(
        object.unscaledValue().toByteArray(), stream, startPosition);
  }

  @Override
  public BigDecimal deserializeNativeObject(byte[] stream, int startPosition) {
    final int scale = OIntegerSerializer.INSTANCE.deserializeNative(stream, startPosition);
    startPosition += OIntegerSerializer.INT_SIZE;

    final byte[] unscaledValue =
        OBinaryTypeSerializer.INSTANCE.deserializeNativeObject(stream, startPosition);

    return new BigDecimal(new BigInteger(unscaledValue), scale);
  }

  public boolean isFixedLength() {
    return false;
  }

  public int getFixedLength() {
    return 0;
  }

  @Override
  public BigDecimal preprocess(BigDecimal value, Object... hints) {
    return value;
  }

  /** {@inheritDoc} */
  @Override
  public void serializeInByteBufferObject(BigDecimal object, ByteBuffer buffer, Object... hints) {
    buffer.putInt(object.scale());
    OBinaryTypeSerializer.INSTANCE.serializeInByteBufferObject(
        object.unscaledValue().toByteArray(), buffer);
  }

  /** {@inheritDoc} */
  @Override
  public BigDecimal deserializeFromByteBufferObject(ByteBuffer buffer) {
    final int scale = buffer.getInt();
    final byte[] unscaledValue =
        OBinaryTypeSerializer.INSTANCE.deserializeFromByteBufferObject(buffer);

    return new BigDecimal(new BigInteger(unscaledValue), scale);
  }

  @Override
  public BigDecimal deserializeFromByteBufferObject(int offset, ByteBuffer buffer) {
    final int scale = buffer.getInt(offset);
    offset += Integer.BYTES;

    final byte[] unscaledValue =
        OBinaryTypeSerializer.INSTANCE.deserializeFromByteBufferObject(offset, buffer);

    return new BigDecimal(new BigInteger(unscaledValue), scale);
  }

  /** {@inheritDoc} */
  @Override
  public int getObjectSizeInByteBuffer(ByteBuffer buffer) {
    buffer.position(buffer.position() + OIntegerSerializer.INT_SIZE);
    return OIntegerSerializer.INT_SIZE
        + OBinaryTypeSerializer.INSTANCE.getObjectSizeInByteBuffer(buffer);
  }

  @Override
  public int getObjectSizeInByteBuffer(int offset, ByteBuffer buffer) {
    return Integer.BYTES
        + OBinaryTypeSerializer.INSTANCE.getObjectSizeInByteBuffer(offset + Integer.BYTES, buffer);
  }

  /** {@inheritDoc} */
  @Override
  public BigDecimal deserializeFromByteBufferObject(
      ByteBuffer buffer, OWALChanges walChanges, int offset) {
    final int scale = walChanges.getIntValue(buffer, offset);
    offset += OIntegerSerializer.INT_SIZE;

    final byte[] unscaledValue =
        OBinaryTypeSerializer.INSTANCE.deserializeFromByteBufferObject(buffer, walChanges, offset);

    return new BigDecimal(new BigInteger(unscaledValue), scale);
  }

  /** {@inheritDoc} */
  @Override
  public int getObjectSizeInByteBuffer(ByteBuffer buffer, OWALChanges walChanges, int offset) {
    return OIntegerSerializer.INT_SIZE
        + OBinaryTypeSerializer.INSTANCE.getObjectSizeInByteBuffer(
            buffer, walChanges, offset + OIntegerSerializer.INT_SIZE);
  }
}
