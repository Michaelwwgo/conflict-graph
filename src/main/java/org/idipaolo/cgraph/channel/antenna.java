package org.idipaolo.cgraph.channel;

import org.idipaolo.cgraph.Configuration;

/**
 * Created by Igor on 22/06/2015.
 */
public class Antenna {

    static final private double m_epsilon = Double.NEGATIVE_INFINITY;

    private double m_beamwidth;

    Antenna()
    {
        this.m_beamwidth = Configuration.getInstance().getBeamwidth();
    }


    public double getGainDb(double angle,double orientation)
    {
        double phi = angle - orientation;

        while(phi <= -Math.PI)
        {
            phi += 2*Math.PI;
        }

        while(phi > Math.PI)
        {
            phi -= 2*Math.PI;
        }

        double epsilon_w = Math.pow(10.0, m_epsilon / 10.0) ;

        if (phi <= m_beamwidth / 2.0 && phi >= -m_beamwidth / 2.0)
        {
            //Main lobe
            double r = (2 * Math.PI - (2 * Math.PI - m_beamwidth)*epsilon_w) / m_beamwidth;
            return 10 * Math.log10(r);
        }

        return m_epsilon;
    }

//    double GetGainDb(Angles a)
//    {
//
//
//        double phi = a.phi - m_orientation;
//
//        // make sure phi is in (-pi, pi]
//        while (phi <= -Math.PI)
//        {
//            phi += Math.PI + Math.PI;
//        }
//        while (phi > Math.PI)
//        {
//            phi -= Math.PI + Math.PI;
//        }
//
//        double epsilon_w = Math.pow(10.0, m_epsilon / 10.0) ;
//
//        if (phi <= m_beamwidth / 2.0 && phi >= -m_beamwidth / 2.0)
//        {
//            //NS_LOG_INFO("Inside main lobe");
//            double r = (2 * Math.PI - (2 * Math.PI - m_beamwidth)*epsilon_w) / m_beamwidth;
//            return 10 * Math.log10(r);
//        }
//
//        return m_epsilon;
//
//    }
}
