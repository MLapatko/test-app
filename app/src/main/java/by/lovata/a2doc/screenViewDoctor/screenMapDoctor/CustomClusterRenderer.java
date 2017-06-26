package by.lovata.a2doc.screenViewDoctor.screenMapDoctor;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

/**
 * Created by kroos on 6/26/17.
 */

class CustomClusterRenderer<T extends AbstractMarker> extends DefaultClusterRenderer<T> {

    private GoogleMap googleMap;

    CustomClusterRenderer(Context context, GoogleMap map, ClusterManager<T> clusterManager) {
        super(context, map, clusterManager);
        this.googleMap = map;
    }

    @Override
    protected boolean shouldRenderAsCluster(Cluster<T> cluster) {
        //start clustering if at least 2 items overlap
        float currentZoom = googleMap.getCameraPosition().zoom;
        float currentMaxZoom = googleMap.getMaxZoomLevel();
        return currentZoom < currentMaxZoom && cluster.getSize() > 1;
    }

}
