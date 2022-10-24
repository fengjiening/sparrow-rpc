package com.fengjiening.sparrow.registry;

import lombok.*;

import java.util.Objects;


/**
 * <p>
 *  Provider信息
 * </p>
 *
 * @author Jay
 * @date 2022/02/07 14:29
 */
@Builder
@Getter
@Setter
@AllArgsConstructor
@ToString
public class ProviderNode {
    private String url;
    private int weight;
    private long lastHeartBeatTime;

    public ProviderNode(){

    }

    public void setLastHeartBeatTime(long time){
        this.lastHeartBeatTime = time;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProviderNode that = (ProviderNode) o;
        return url.equals(that.url);
    }


    @Override
    public int hashCode() {
        return Objects.hash(url, weight, lastHeartBeatTime);
    }
}
