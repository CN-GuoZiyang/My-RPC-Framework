package top.guoziyang.rpc.loadbalancer;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;

/**
 * @author ziyang
 */
public interface LoadBalancer {

    Instance select(List<Instance> instances);

}
