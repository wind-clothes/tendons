package org.tendons.common.serialization.hessian;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.tendons.common.serialization.DeserializeParam;
import org.tendons.common.serialization.RpcSerializer;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;

/**
 * @author: xiongchengwei
 * @date:2017年5月17日 下午1:03:07
 */
public class HessianSerializer<T> implements RpcSerializer<T> {

  @Override
  public byte[] serialize(T object) throws Exception {
    final Hessian2Output hessian2Output = new Hessian2Output();
    try (final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();) {
      hessian2Output.init(outputStream);
      hessian2Output.writeObject(object);
      return outputStream.toByteArray();
    } finally {
      if (hessian2Output != null) {
        hessian2Output.close();
      }
    }
  }

  @Override
  public T deserialize(DeserializeParam<T> param) throws Exception {
    final Hessian2Input hessian2Input = new Hessian2Input();
    try (final ByteArrayInputStream inputStream = new ByteArrayInputStream(param.getBytes())) {
      final Object object = hessian2Input.readObject();
      return param.getClazz().cast(object);
    } finally {
      if (hessian2Input != null) {
        hessian2Input.close();
      }
    }
  }

}
