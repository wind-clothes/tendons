package org.tendons.common.server;

import org.tendons.common.codec.TendonsRpcCodec;
import org.tendons.common.config.TendonsTcpServerConfig;
import org.tendons.common.extension.ExtensionLoader;

/**
 * @author: chengweixiong@uworks.cc
 * @date: 2017年6月7日 下午9:58:05
 */
public abstract class AbstractServer implements RpcServer {

    protected final TendonsRpcCodec codec;

    protected final TendonsTcpServerConfig config;

    protected AbstractServer(TendonsTcpServerConfig config) {
        this.config = config;
        this.codec = LoadTendonsRpcCodec(config);
    }

    @Override
    public void start() throws Throwable {
        doStart();
    }

    @Override
    public void stop() throws Throwable {
        doStop();
    }

    public TendonsRpcCodec getCodec() {
        return codec;
    }

    public TendonsTcpServerConfig getConfig() {
        return config;
    }

    protected abstract void doStart() throws Throwable;

    protected abstract void doStop() throws Throwable;

    protected TendonsRpcCodec LoadTendonsRpcCodec(TendonsTcpServerConfig config) {
        final String name = config.getCodecName();
        final TendonsRpcCodec codec =
            ExtensionLoader.getExtensionLoader(TendonsRpcCodec.class).getExtension(name);
        if (codec == null) {
            throw new IllegalArgumentException("load codec is not found name:" + name);
        }
        return codec;
    }
}
