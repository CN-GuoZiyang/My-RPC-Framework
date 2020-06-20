package top.guoziyang.rpc.provider;

/**
 * 保存和提供服务实例对象
 * @author ziyang
 */
public interface ServiceProvider {


    <T> void addServiceProvider(T service, Class<T> serviceClass);

    Object getServiceProvider(String serviceName);

}
