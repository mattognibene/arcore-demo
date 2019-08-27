package com.mattognibene.ardemo

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.ar.sceneform.ux.ArFragment
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import com.google.ar.sceneform.rendering.ModelRenderable
import androidx.core.view.accessibility.AccessibilityRecordCompat.setSource
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.ux.TransformableNode

class MainActivity : AppCompatActivity() {

    private lateinit var arFragment: ArFragment
    private var spaceman: ModelRenderable? = null

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!checkIsSupportedDeviceOrFinish(this)) {
            return
        }

        arFragment = ux_fragment as ArFragment

        ModelRenderable.builder()
                .setSource(this, Uri.parse("scene.sfb"))
                .build()
                .thenAccept { renderable -> spaceman = renderable }
                .exceptionally { throwable ->
                    Timber.e("Unable to load Renderable.", throwable)
                    null
                }

        arFragment.setOnTapArPlaneListener { hitResult, plane, motionEvent ->
            if (spaceman == null) {
                return@setOnTapArPlaneListener
            }

            // Create the Anchor.
            val anchor = hitResult.createAnchor()
            val anchorNode = AnchorNode(anchor)
            anchorNode.setParent(arFragment.arSceneView.scene)

            // Create the transformable andy and add it to the anchor.
            val andy = TransformableNode(arFragment.transformationSystem)
            andy.scaleController.maxScale = .05f
            andy.scaleController.minScale = .03f
            andy.setParent(anchorNode)
            andy.renderable = spaceman
            andy.select()
        }
    }

    private fun checkIsSupportedDeviceOrFinish(activity: Activity): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            Timber.e("Sceneform requires Android N or later")
            Toast.makeText(activity,
                    "Sceneform requires Android N or later", Toast.LENGTH_LONG).show()
            activity.finish()
            return false
        }
        val openGlVersionString =
                (activity.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager)
                .deviceConfigurationInfo
                .glEsVersion
        if (java.lang.Double.parseDouble(openGlVersionString) < MIN_OPENGL_VERSION) {
            Timber.e("Sceneform requires OpenGL ES 3.0 later")
            Toast.makeText(activity, "Sceneform requires OpenGL ES 3.0 or later", Toast.LENGTH_LONG)
                    .show()
            activity.finish()
            return false
        }
        return true
    }

    companion object {
        private const val MIN_OPENGL_VERSION = 3.0
    }
}