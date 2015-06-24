package org.idipaolo.cgraph;


/**
 * Created by Igor on 17/04/2015.
 */
public class Configuration {

    private static Configuration instance = null;

    private double beamwidth;
    private double areaSize;

    public long getSeed() {
        return seed;
    }

    public void setSeed(long seed) {
        this.seed = seed;
    }

    private long seed;

    public double getObstacleMaxSize() {
        return obstacleMaxSize;
    }

    public void setObstacleMaxSize(double obstacleMaxSize) {
        this.obstacleMaxSize = obstacleMaxSize;
    }

    private double obstacleMaxSize;

    public double getAreaSize() {
        return areaSize;
    }

    public void setAreaSize(double areaSize) {
        this.areaSize = areaSize;
    }

    public double getBeamwidth() {
        return beamwidth;
    }

    public void setBeamwidth(double beamwidth) {

        if(beamwidth < 0 || beamwidth > 2*Math.PI)
        {
            throw new IllegalArgumentException("Beamwidth");
        }

        this.beamwidth = beamwidth;
    }

    public static Configuration getInstance()
    {
        if(instance == null)
        {
            instance = new Configuration();
        }

        return instance;
    }
}
