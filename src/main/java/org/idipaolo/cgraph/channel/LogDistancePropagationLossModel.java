package org.idipaolo.cgraph.channel;

/**
 * Created by Igor on 22/06/2015.
 */
public class LogDistancePropagationLossModel {

    private static final double m_referenceDistance = 1.0;
    private static final double m_exponent = 2.0;

//    double calcRx(double txPowerDbm)
//    {
//        double distance = a->GetDistanceFrom (b);
//
//        if (distance <= m_referenceDistance)
//        {
//            return txPowerDbm;
//        }
//        /**
//         * The formula is:
//         * rx = 10 * log (Pr0(tx)) - n * 10 * log (d/d0)
//         *
//         * Pr0: rx power at reference distance d0 (W)
//         * d0: reference distance: 1.0 (m)
//         * d: distance (m)
//         * tx: tx power (dB)
//         * rx: dB
//         *
//         * Which, in our case is:
//         *
//         * rx = rx0(tx) - 10 * n * log (d/d0)
//         */
//        double pathLossDb = 10 * m_exponent * std::log10 (distance / m_referenceDistance);
//        double rxc = -m_referenceLoss - pathLossDb;
//        NS_LOG_DEBUG ("distance="<<distance<<"m, reference-attenuation="<< -m_referenceLoss<<"dB, "<<
//                "attenuation coefficient="<<rxc<<"db");
//        return txPowerDbm + rxc;
//    }
}
