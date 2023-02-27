package com.yasuo.common.protocol;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author goku peng
 * @since 2023/2/10 15:26
 */
@Data
@AllArgsConstructor
public class Peer {
    private String host;
    private int port;
}
