package org.qtum.mromanovsky.qtum.btc;

import android.os.Build;
import android.os.SystemClock;
import android.util.Log;

import org.spongycastle.crypto.digests.SHA256Digest;
import org.spongycastle.crypto.prng.DigestRandomGenerator;
import org.spongycastle.crypto.prng.ThreadedSeedGenerator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class TrulySecureRandom extends java.security.SecureRandom {
    private static final String TAG = "SecureRandom";
    private final DigestRandomGenerator generator;
    private boolean initialized;


    public TrulySecureRandom() {
        generator = new DigestRandomGenerator(new SHA256Digest());
    }

    public void addSeedMaterial(long seed) {
        generator.addSeedMaterial(seed);
    }

    private void addSeedMaterial(byte[] seed) {
        generator.addSeedMaterial(seed);
    }

    @Override
    public int nextInt() {
        byte[] buf = new byte[4];
        nextBytes(buf);
        return ((buf[0] & 0xff) << 24) | ((buf[1] & 0xff) << 16) | ((buf[2] & 0xff) << 8) | (buf[3] & 0xff);
    }

    @Override
    public int nextInt(int n) {
        return Math.abs(nextInt()) % n;
    }

    @Override
    public synchronized void nextBytes(byte[] bytes) {
        if (!initialized) {
            long start = System.currentTimeMillis();
            ThreadedSeedGenerator threadedSeedGenerator = new ThreadedSeedGenerator();
            do {
                addSeedMaterial(threadedSeedGenerator.generateSeed(64, true));
                try {
                    Thread.sleep(1);
                } catch (InterruptedException ignored) {
                }
                addSeedMaterial(threadedSeedGenerator.generateSeed(32, false));
            } while (Math.abs(System.currentTimeMillis() - start) < 1000);
            addSeedMaterial(System.nanoTime());
            addSeedMaterial(System.currentTimeMillis());
            addSeedMaterial(SystemClock.elapsedRealtime());
            addSeedMaterial(SystemClock.currentThreadTimeMillis());
            addSeedMaterial(new java.security.SecureRandom().generateSeed(128));
            addSeedMaterial(("" + Build.DEVICE + Build.MODEL + Build.TIME + Build.VERSION.SDK_INT).getBytes());

            ExecutorService executor = Executors.newSingleThreadExecutor();
            try {
                Future future = executor.submit(new Runnable() {
                    @Override
                    public void run() {
                        byte[] devRandomSeed = getDevRandomSeed();
                        if (devRandomSeed != null) {
                            addSeedMaterial(devRandomSeed);
                        }
                    }
                });
                future.get(3, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Log.v(TAG, "/dev/random read interrupted");
            } catch (ExecutionException e) {
                Log.e(TAG, "/dev/random read error");
            } catch (TimeoutException e) {
                Log.w(TAG, "/dev/random read timeout");
            } finally {
                executor.shutdownNow();
            }
            initialized = true;
        }
        generator.nextBytes(bytes);
    }


    @Override
    public String getAlgorithm() {
        return "BouncyCastle";
    }

    private byte[] getDevRandomSeed() {
        byte[] buf = null;
        File file = new File("/dev/random");
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            buf = new byte[16];
            for (int i = 0; i < buf.length; i++) {
                int ch = inputStream.read();
                if (ch == -1) {
                    return null;
                }
                buf[i] = (byte) ch;
            }
        } catch (Exception ignored) {
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ignored) {

                }
            }
        }
        return buf;
    }

    @Override
    public void setSeed(byte[] seed) {
        Log.w("SecureRandom", "setting seed " + BTCUtils.toHex(seed) + " was ignored");
    }

    @Override
    public void setSeed(long seed) {
        Log.w("SecureRandom", "setting seed " + seed + " was ignored");
    }

    @Override
    public byte[] generateSeed(int numBytes) {
        throw new RuntimeException("not supported");
    }

    @Override
    public boolean nextBoolean() {
        throw new RuntimeException("not supported");
    }

    @Override
    public double nextDouble() {
        throw new RuntimeException("not supported");
    }

    @Override
    public float nextFloat() {
        throw new RuntimeException("not supported");
    }

    @Override
    public synchronized double nextGaussian() {
        throw new RuntimeException("not supported");
    }

    @Override
    public long nextLong() {
        throw new RuntimeException("not supported");
    }

}
