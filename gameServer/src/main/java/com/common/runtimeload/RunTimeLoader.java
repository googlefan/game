package com.common.runtimeload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RunTimeLoader implements JLoaderListener{
	
	private static final Logger logger = LoggerFactory.getLogger(RunTimeLoader.class);
	
	ClassLoader classLoader;
	String runtimeJarPath;
	String srcJarPath;
	
	   private URLClassLoader load(URL url) throws Exception {
	        URLClassLoader loader = new URLClassLoader(new URL[] { url }, this.getClass().getClassLoader());
	        return loader;
	    }
	
	@Override
    public void reload() {
        FileOutputStream fos = null;
        FileInputStream fis = null;
        try {
            if (runtimeJarPath != null && srcJarPath != null) {
                File srcJar = new File(srcJarPath);
                if (!srcJar.exists()) {
                    logger.info("{}, 不存在, 使用默认加载器[{}]", srcJarPath, getClass().getClassLoader());
                    this.classLoader = getClass().getClassLoader();
                    return;
                }
                File runtimedir = new File(runtimeJarPath);
                if (!runtimedir.exists()) {
                    boolean succ = runtimedir.mkdir();
                    if (!succ) {
                        return;
                    }
                }

                File file = new File(runtimeJarPath, new SimpleDateFormat("yyyyMMddHHmm").format(new Date()) + "_" + srcJar.getName());
                fos = new FileOutputStream(file);// 目标
                fis = new FileInputStream(srcJar);

                transferIOStream(fis, fos);
                fis.close();
                fos.close();
                URLClassLoader urlclassloader = load(file.toURI().toURL());
                this.classLoader = urlclassloader;
                logger.info("找到资源{}, 使用自定义加载器[{}]", srcJarPath, this.classLoader);
            } else {
                this.classLoader = getClass().getClassLoader();
            }
        } catch (Exception e) {
            logger.error("加载外部jar失败，使用了默认的加载器");
            this.classLoader = getClass().getClassLoader();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
	
	
    public static void transferIOStream(InputStream is, OutputStream os) throws IOException {
        int bytesRead = 0;
        byte[] buffer = new byte[2048];
        while ((bytesRead = is.read(buffer, 0, 2048)) != -1) {
            if (os != null) {
                os.write(buffer, 0, bytesRead);
            }
        }
    }

	public String getRuntimeJarPath() {
		return runtimeJarPath;
	}

	public void setRuntimeJarPath(String runtimeJarPath) {
		this.runtimeJarPath = runtimeJarPath;
	}

	public String getSrcJarPath() {
		return srcJarPath;
	}

	public void setSrcJarPath(String srcJarPath) {
		this.srcJarPath = srcJarPath;
	}

	public ClassLoader getClassLoader() {
		return classLoader;
	}

	public void setClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

    
    

}
