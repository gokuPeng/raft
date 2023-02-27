package com.yasuo.common.protocol;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author goku peng
 * @since 2023/2/14 9:28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RpcResponse implements Serializable {
    private static final long serialVersionUID = -8576495949225093023L;

    private String requestId;
    private String error;
    private Object result;

    public boolean isError() {
        return error != null;
    }
}
