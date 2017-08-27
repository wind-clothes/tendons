package org.tendons.registry;

/**
 * @author: chengweixiong@uworks.cc
 * @date: 2017年5月30日 下午3:01:59
 */
public abstract class AbstractRegistry implements RegistryCenter {

    @Override
    public void connection(RegistryCenterConfig centerConfig) {
        doConnection(centerConfig);
    }

    @Override
    public void close() {
        doClose();
    }

    protected abstract void doConnection(RegistryCenterConfig centerConfig);

    protected abstract void doClose();
}
