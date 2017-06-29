package by.lovata.a2doc.screenViewDoctor.screenMapDoctor;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

class CustomClusterRenderer<T extends AbstractMarker> extends DefaultClusterRenderer<T> implements
        GoogleMap.OnCameraMoveListener {

    private float currentZoomLevel, maxZoomLevel;
    private GoogleMap map;

    CustomClusterRenderer(Context context, GoogleMap map, ClusterManager<T> clusterManager,
                          float currentZoomLevel, float maxZoomLevel) {
        super(context, map, clusterManager);
        this.map = map;
        this.currentZoomLevel = currentZoomLevel;
        this.maxZoomLevel = maxZoomLevel;
    }

    @Override
    protected boolean shouldRenderAsCluster(Cluster<T> cluster) {
        boolean superWouldCluster = shouldRenderAsClusterSize(cluster);

        // if it would, then determine if you still want it to based on zoom level
        if (superWouldCluster) {
            superWouldCluster = currentZoomLevel < maxZoomLevel;
        }

        return superWouldCluster;
    }

    @Override
    public void onCameraMove() {
        currentZoomLevel = map.getCameraPosition().zoom;
    }

    private boolean shouldRenderAsClusterSize(Cluster<T> cluster) {
        return cluster.getSize() > 1;
    }
}
