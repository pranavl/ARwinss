/**
 * TrackerRenderer.java
 *
 * @author Pranav Lakshminarayanan
 */
package org.artoolkit.ar.tracker.ARTracker;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.microedition.khronos.opengles.GL10;
import org.artoolkit.ar.base.ARActivity;

import org.artoolkit.ar.base.ARToolKit;
import org.artoolkit.ar.base.rendering.ARRenderer;
import org.artoolkit.ar.base.rendering.Cube;
import org.artoolkit.ar.base.rendering.Pyramid;
import org.artoolkit.ar.base.rendering.STLSurface;

/**
 * A renderer that adds a marker and draws an object on it.
 */
public class TrackerRenderer extends ARRenderer {

    /**
     * Markers.
     */
    private int markerHiro = -1;
    private int markerD = -1;
    private int markerA = -1;

    /**
     * Sample cube visualization.
     */
    private Cube cube = new Cube(60.0f, 0.0f, 0.0f, 30.0f);

    /**
     * Sample pyramid visualization.
     */
    private Pyramid pyr = new Pyramid(40.0f, 0.0f, 0.0f, 0.0f, 10.0f, 10.0f);

    /**
     * STL Surface model.
     */
    private STLSurface sur;

    /**
     * ARActivity class that creates this renderer.
     */
    private ARTracker activity;

    /**
     * Frame data.
     */
    private HashMap<String, float[]> data = new HashMap<String, float[]>();

    // CONSTRUCTOR =============================================================
    /**
     * Constructor for SimpleRenderer.
     *
     * @param a ARActivity that calls it.
     */
    public TrackerRenderer(ARTracker a) {
        this.activity = a;
    }

    // METHODS =================================================================
    /**
     * Markers can be configured here.
     *
     * @return true if configured properly
     */
    @Override
    public boolean configureARScene() {

        markerHiro = configureMarker("single;Data/patt.hiro;80", "patt.hiro");
        markerD = configureMarker("single;Data/multi/patt.d;80", "patt.d");
        markerA = configureMarker("single;Data/multi/patt.a;80", "patt.a");

        // TRANSFORMATIONS so pyramid can be at tip of Kanji marker pointer
        //pyr.rotateX((float) (Math.PI / 2));
        //pyr.translate(0.0f, -155.0f, 0.0f);
        
        try {
            InputStream is = this.activity.getAssets().open("Artery.stl");
            this.sur = new STLSurface(is);
        } catch (IOException ex) {
            Logger.getLogger(TrackerRenderer.class.getName()).
                    log(Level.SEVERE, null, ex);
        }

        if (markerHiro < 0 || markerD < 0 || markerA < 0) {
            return false;
        }
        return true;
    }

    /**
     * Override the draw function from ARRenderer.
     *
     * @param gl .
     */
    @Override
    public void draw(GL10 gl) {

        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        // Apply the ARToolKit projection matrix
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadMatrixf(ARToolKit.getInstance().getProjectionMatrix(), 0);
        this.data.put("PROJECTION", ARToolKit.getInstance().getProjectionMatrix());

        gl.glEnable(GL10.GL_CULL_FACE);
        gl.glShadeModel(GL10.GL_SMOOTH);
        gl.glEnable(GL10.GL_DEPTH_TEST);
        gl.glFrontFace(GL10.GL_CW);

        //If the marker is visible, render the model
        if (ARToolKit.getInstance().queryMarkerVisible(markerA)) {
            gl.glMatrixMode(GL10.GL_MODELVIEW);
            gl.glLoadMatrixf(ARToolKit.getInstance().
                    queryMarkerTransformation(markerA), 0);
            sur.draw(gl);
            this.data.put("patt.a", ARToolKit.getInstance().
                    queryMarkerTransformation(markerA));
        }
//        if (ARToolKit.getInstance().queryMarkerVisible(markerHiro)) {
//            gl.glMatrixMode(GL10.GL_MODELVIEW);
//            gl.glLoadMatrixf(ARToolKit.getInstance().
//                    queryMarkerTransformation(markerHiro), 0);
//            sur.draw(gl);
//        }

//        if (ARToolKit.getInstance().queryMarkerVisible(markerKanji)) {
//            gl.glMatrixMode(GL10.GL_MODELVIEW);
//            gl.glLoadMatrixf(ARToolKit.getInstance().
//                    queryMarkerTransformation(markerKanji), 0);
//            pyr.draw(gl);
//        }
    }

    /**
     * Access a value from the data HashMap.
     *
     * @param k String key
     * @return value associated with k
     */
    public float[] get(String k) {
        return this.data.get(k);
    }

    private int configureMarker(String path, String key) {
        this.data.put(key, new float[1]);
        return ARToolKit.getInstance().addMarker(path);
    }

}
