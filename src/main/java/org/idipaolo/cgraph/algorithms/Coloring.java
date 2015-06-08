/*
 * Copyright University of Orleans - ENSI de Bourges
 * This software is governed by the CeCILL  license under French law and
 * abiding by the rules of distribution of free software.  You can  use, 
 * modify and/ or redistribute the software under the terms of the CeCILL
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 */
package org.idipaolo.cgraph.algorithms;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import agape.algos.Algorithms;
import agape.algos.MIS;
import org.apache.commons.collections15.Factory;

import agape.tools.Operations;

import com.google.common.collect.Sets;

import edu.uci.ics.jung.graph.Graph;

/**
 * The goal of this class is to compute the minimum number of colors to properly color an undirected graph.
 * @author V. Levorato
 * @author J.-F. Lalande
 * @author P. Berthome
 * @param <V> Vertices type
 * @param <E> Edges type
 */
public class Coloring<V,E> extends Algorithms<V,E> {

    //some constants
    final double alpha=0.19903; // for chromatic number

    /**
     * Constructor of the Coloring algorithms.
     * Needs a factory in order to create new graphs.
     * @param factory
     */
    public Coloring(Factory<Graph<V,E>> factory) {
        graphFactory = factory;
    };

    /**
     * @param Ginit the graph
     * @return a coloring assignment using a greedy algorithm that removes iteratively greedy MISs.
     * @author P. Berthome, J.-F. Lalande 
     */
    public Set<Set<V>> greedyGraphColoring(Graph<V,E> Ginit)
    {
        Graph<V, E> G = Operations.copyGraph(Ginit, this.graphFactory);
        Set<Set<V>> sol = new HashSet<Set<V>>();

        MIS<V, E> mis = new MIS<V, E>(this.graphFactory, this.vertexFactory, this.edgeFactory);

        while (G.getVertexCount() != 0)
        {
            Set<V> misSet = mis.maximalIndependentSetGreedy(G);
            sol.add(misSet);
            Operations.removeAllVertices(G, misSet);
        }

        return sol;

    }

    /**
     * Returns the chromatic number of a graph G. This method is based on the
     * Bodlaender and Kratsch work ("An exact algorithm for graph coloring
     * with polynomial memory" 2006). The algorithm solves the problem in PSPACE
     * and in time O(5.283^n).
     * This algorithm is limited to graphs having no more than 63 vertices. 
     * @param Ginit graph
     * @return chromatic number of G
     * @author P. Berthome, J.-F. Lalande, V. Levorato
     */
    public Set<Set<V>> graphColoring(Graph<V,E> Ginit)
    {
        Graph<V, E> G = Operations.copyGraph(Ginit, this.graphFactory);

        return graphColoringInternal(G);
    }

    /**
     * Returns the cpartition of the vertices into color classes of a graph G. 
     * This method is based on the
     * Bodlaender and Kratsch work ("An exact algorithm for graph coloring
     * with polynomial memory" 2006). The algorithm solves the problem in PSPACE
     * and in time O(5.283^n).
     * This algorithm is limited to graphs having no more than 63 vertices. 
     * @param G graph
     * @return the partition of the vertices into color classes.
     * @author P. Berthome, J.-F. Lalande, V. Levorato
     */
    protected Set<Set<V>> graphColoringInternal(Graph<V,E> G)
    {
        double n=G.getVertexCount();
        int best=(int) n;
        Set<Set<V>> bestSol = new HashSet<Set<V>>();
        long ncomb=(long) Math.pow(2,best);
        ArrayList<V> nodesArray = new ArrayList<V>();
        nodesArray.addAll(G.getVertices());

        for(long i=1; i<ncomb ; i++)
        {
            Set<V> S=new HashSet<V>();
            String comb=Long.toBinaryString(i);
            for(int k=0; k<comb.length() ; k++)
                if(comb.charAt(k)=='1')
                    S.add(nodesArray.get(comb.length() - k -1));

            if(MIS.isMaximalIndependentSet(G, S) && S.size()>=alpha*n)
            {
                Graph<V,E> GS=graphFactory.create();
                Operations.subGraph(G,GS,S);
                Operations.removeAllVertices(G, S);
                Set<Set<V>> sol = graphColoringInternal(G);
                Operations.mergeGraph(G,GS);
                best=Math.min(best, sol.size() +1);
                if (best == sol.size() +1) {
                    bestSol = sol;
                    bestSol.add(S);
                }
            }

            if( (n-alpha*n)/2 <= S.size() && S.size() <= (n+alpha*n)/2 )
            {
                Graph<V,E> GS=graphFactory.create();
                Set<V> V_S=new HashSet<V>();
                Sets.difference(new HashSet<V>(G.getVertices()), S).copyInto(V_S);
                Operations.subGraph(G,GS,V_S);
                Operations.removeAllVertices(G, V_S);
                Set<Set<V>> sol = graphColoringInternal(G);
                int cnGS = sol.size();
                Operations.mergeGraph(G,GS);

                Graph<V,E> G_S=graphFactory.create();
                Operations.subGraph(G,G_S,S);
                Operations.removeAllVertices(G, S);
                Set<Set<V>> sol2=graphColoringInternal(G);
                int cnG_S = sol2.size();
                Operations.mergeGraph(G,G_S);

                best=Math.min(best,cnGS + cnG_S);

                if(best == cnG_S + cnGS)
                {
                    bestSol = sol;
                    sol.addAll(sol2);
                }
            }

        }

        // If the graph is complete, no best solution have been found and best = n
        // The solution has to be constructed
        if(best==n){
            for(V v:G.getVertices()){
                Set<V> toAdd = new HashSet<V>();
                toAdd.add(v);
                bestSol.add(toAdd);
            }
        }

        return bestSol;
    }

    /**
     * Returns the chromatic number of a graph G. This method is based on the
     * Bodlaender and Kratsch work ("An exact algorithm for graph coloring
     * with polynomial memory" 2006). The algorithm solves the problem in PSPACE
     * and in time O(5.283^n).
     * This algorithm is limited to graphs having no more than 63 vertices. 
     * @param Ginit graph
     * @return chromatic number of G
     * @author P. Berthome, J.-F. Lalande, V. Levorato
     */
    public int chromaticNumber(Graph<V,E> Ginit)
    {
        Graph<V, E> G = Operations.copyGraph(Ginit, this.graphFactory);

        return chromaticNumberInternal(G);
    }

    /**
     * Returns the chromatic number of a graph G. This method is based on the
     * Bodlaender and Kratsch work ("An exact algorithm for graph coloring
     * with polynomial memory" 2006). The algorithm solves the problem in PSPACE
     * and in time O(5.283^n).
     * This algorithm is limited to graphs having no more than 63 vertices. 
     * @param G graph
     * @return chromatic number of G
     * @author P. Berthome, J.-F. Lalande, V. Levorato
     */
    protected int chromaticNumberInternal(Graph<V,E> G)
    {
        int n = G.getVertexCount();
        int best = n;

        long ncomb=(long) Math.pow(2,best);
        ArrayList<V> nodesArray = new ArrayList<V>();
        nodesArray.addAll(G.getVertices());

        for(long i=1; i<ncomb ; i++)
        {
            Set<V> S=new HashSet<V>();
            String comb=Long.toBinaryString(i);
            for(int k=0; k<comb.length() ; k++)
                if(comb.charAt(k)=='1')
                    S.add(nodesArray.get(comb.length() - k-1));

            if(MIS.isMaximalIndependentSet(G, S) && S.size()>=alpha*n)
            {
                Graph<V,E> GS=graphFactory.create();
                Operations.subGraph(G,GS,S);
                Operations.removeAllVertices(G, S);
                int cnG_S=chromaticNumberInternal(G);
                Operations.mergeGraph(G,GS);
                best=Math.min(best, 1+cnG_S);
            }

            if( (n-alpha*n)/2 <= S.size() && S.size() <= (n+alpha*n)/2 )
            {
                Graph<V,E> GS=graphFactory.create();

                Set<V> V_S=new HashSet<V>();
                Sets.difference(new HashSet<V>(G.getVertices()), S).copyInto(V_S);
                Operations.subGraph(G,GS,V_S);
                Operations.removeAllVertices(G, V_S);
                int cnGS=chromaticNumberInternal(G);
                Operations.mergeGraph(G,GS);

                Graph<V,E> G_S=graphFactory.create();
                Operations.subGraph(G,G_S,S);
                Operations.removeAllVertices(G, S);
                int cnG_S=chromaticNumberInternal(G);
                Operations.mergeGraph(G,G_S);

                best=Math.min(best,cnGS + cnG_S);
            }
        }

        return best;
    }
}
