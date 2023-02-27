package com.yasuo.common.protocol;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author goku peng
 * @since 2023/2/11 18:58
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RpcRequest implements Serializable {
    private static final long serialVersionUID = 2124696393886730907L;

    private String requestId;
    private String className;
    private String methodName;
    private Class<?>[] parameterTypes;
    private Object[] parameters;
}
