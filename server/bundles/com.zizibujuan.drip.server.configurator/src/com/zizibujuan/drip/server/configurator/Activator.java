package com.zizibujuan.drip.server.configurator;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.service.packageadmin.PackageAdmin;
import org.osgi.util.tracker.ServiceTracker;

/**
 * bundle入口
 * @author 金正伟
 * @since 0.0.1
 */
public class Activator implements BundleActivator {

	private static BundleContext context;
	static Activator singleton;
	// TODO:替换到PackageAdmin
	private ServiceTracker<PackageAdmin, PackageAdmin> packageAdminTracker;

	static BundleContext getContext() {
		return context;
	}
	
	public static Activator getDefault() {
		return singleton;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		singleton = this;
		Activator.context = bundleContext;
		packageAdminTracker = new ServiceTracker<PackageAdmin, PackageAdmin>(context, PackageAdmin.class.getName(), null);
		packageAdminTracker.open();
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
		
		if (packageAdminTracker != null) {
			packageAdminTracker.close();
			packageAdminTracker = null;
		}
	}

	protected Bundle getBundle(String symbolicName) {
		PackageAdmin packageAdmin = packageAdminTracker.getService();
		if (packageAdmin == null)
			return null;
		Bundle[] bundles = packageAdmin.getBundles(symbolicName, null);
		if (bundles == null)
			return null;
		// Return the first bundle that is not installed or uninstalled
		for (int i = 0; i < bundles.length; i++) {
			if ((bundles[i].getState() & (Bundle.INSTALLED | Bundle.UNINSTALLED)) == 0) {
				return bundles[i];
			}
		}
		return null;
	}

}
