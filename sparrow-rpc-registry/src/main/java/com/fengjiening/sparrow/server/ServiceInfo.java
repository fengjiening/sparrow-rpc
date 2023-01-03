package com.fengjiening.sparrow.server;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

/**
 * <p>
 *  RPC服务信息
 * </p>
 *
 * @author Jay
 * @date 2022/02/06 20:37
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
public class ServiceInfo {
    private Class<?> type;
    private Class<?> impType;
    private int version;
    public ServiceInfo(Class<?> type,int version){
        this.type=type;
        this.version=version;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ServiceInfo that = (ServiceInfo) o;
        return version == that.version && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(version, type);
    }
}
