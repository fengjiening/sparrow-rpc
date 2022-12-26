package com.fengjiening.sparrow.config.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Objects;

/**
 * @ClassName: Channel
 * @Description: TODO
 * @Date: 2022/11/3 15:51
 * @Author: fengjiening::joko
 * @Version: 1.0
 */
@AllArgsConstructor
@Data
public class Channel {
    private String host;
    private int port;

    public String getFkey(){
        return String.format("%s:%d",host,port);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Channel channel = (Channel) o;
        return port == channel.port &&
                Objects.equals(host, channel.host);
    }

    @Override
    public int hashCode() {
        return Objects.hash(host, port);
    }
}
