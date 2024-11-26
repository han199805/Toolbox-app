package com.example.myapplication

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import android.view.SurfaceHolder
import android.widget.Toast
import androidx.core.app.ActivityCompat
import android.hardware.Camera
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.FragmentProtractorBinding
import android.view.Surface

/**
 * An example full-screen fragment that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class ProtractorFragment : Fragment() {
    private lateinit var binding: FragmentProtractorBinding
    private var camera: Camera? = null

    companion object {
        private const val CAMERA_REQUEST_CODE = 101
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProtractorBinding.inflate(inflater, container, false)

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestCameraPermission()
        } else {
            setupCamera()
        }

        return binding.root
    }

    private fun requestCameraPermission() {
        requestPermissions(
            arrayOf(Manifest.permission.CAMERA),
            CAMERA_REQUEST_CODE
        )
    }



    private fun setupCamera() {
        camera = Camera.open()
        setCameraDisplayOrientation()
        val surfaceHolder = binding.cameraPreview.holder

        surfaceHolder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                try {
                    camera?.setPreviewDisplay(holder)
                    camera?.startPreview()
                } catch (e: Exception) {
                    Log.e("CameraError", "Error setting camera preview", e)
                }
            }

            override fun surfaceChanged(
                holder: SurfaceHolder, format: Int, width: Int, height: Int
            ) {
                if (holder.surface == null) return
                try {
                    camera?.stopPreview()
                } catch (e: Exception) {
                }
                try {
                    camera?.setPreviewDisplay(holder)
                    camera?.startPreview()
                } catch (e: Exception) {
                    Log.e("CameraError", "Error restarting camera preview", e)
                }
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                camera?.release()
                camera = null
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        camera?.release()
        camera = null
    }

    private fun setCameraDisplayOrientation() {
        val rotation = requireActivity().windowManager.defaultDisplay.rotation
        val degrees = when (rotation) {
            Surface.ROTATION_0 -> 0
            Surface.ROTATION_90 -> 90
            Surface.ROTATION_180 -> 180
            Surface.ROTATION_270 -> 270
            else -> 0
        }

        val info = Camera.CameraInfo()
        Camera.getCameraInfo(Camera.CameraInfo.CAMERA_FACING_BACK, info)
        val result = if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            (info.orientation + degrees) % 360
        } else { // Back-facing camera
            (info.orientation - degrees + 360) % 360
        }
        camera?.setDisplayOrientation(result)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setupCamera()
            } else {
                Toast.makeText(requireContext(), "Camera permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}