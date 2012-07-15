package com.zizibujuan.drip.server.configurator;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;

/**
 * 应用程序执行入口类
 * @author 金正伟
 * @since 0.0.1
 */
public class WebApplication implements IApplication {
	private static final String EQUINOX_HTTP_JETTY = "org.eclipse.equinox.http.jetty"; //$NON-NLS-1$
	private static final String EQUINOX_HTTP_REGISTRY = "org.eclipse.equinox.http.registry"; //$NON-NLS-1$
	private IApplicationContext applicationContext;
	
	@Override
	public Object start(IApplicationContext context) throws Exception {
		applicationContext = context;
		ensureBundleStarted(EQUINOX_HTTP_JETTY);
		ensureBundleStarted(EQUINOX_HTTP_REGISTRY);
		return IApplicationContext.EXIT_ASYNC_RESULT;
	}

	@Override
	public void stop() {
		if (applicationContext != null)
			applicationContext.setResult(EXIT_OK, this);
	}
	
	private void ensureBundleStarted(String symbolicName) throws BundleException {
		Bundle bundle = Activator.getDefault().getBundle(symbolicName);
		if (bundle != null) {
			if (bundle.getState() == Bundle.RESOLVED || bundle.getState() == Bundle.STARTING) {
				bundle.start();
			}
		}
	}

}
