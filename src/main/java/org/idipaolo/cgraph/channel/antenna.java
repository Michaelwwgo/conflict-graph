package org.idipaolo.cgraph.channel;

/**
 * Created by Igor on 22/06/2015.
 */
public class Antenna {

    static final private double m_epsilon = 10;

    private double m_beamwidth;
    private double m_orientation;

//    double GetGainDb(Angles a)
//    {
//
//        //NS_LOG_FUNCTION(this<<a);
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
