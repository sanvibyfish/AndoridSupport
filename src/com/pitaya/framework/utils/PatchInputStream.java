package com.pitaya.framework.utils;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Sanvi E-mail:sanvibyfish@gmail.com
 * @version 创建时间：2010-9-1 下午03:23:20
 */
public class PatchInputStream extends FilterInputStream {
	public PatchInputStream(InputStream in) {
		super(in);
	}

	public long skip(long n) throws IOException {
		long m = 0L;
		while (m < n) {
			long _m = in.skip(n - m);
			if (_m == 0L)
				break;
			m += _m;
		}
		return m;
	}
}
