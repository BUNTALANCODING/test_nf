package common.map

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.readValue
import platform.AVFoundation.*
import platform.CoreGraphics.CGRectZero
import platform.UIKit.UIView
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue

@OptIn(ExperimentalForeignApi::class)
class IOSCameraPreviewView : UIView(frame = CGRectZero.readValue()) {

    private val session = AVCaptureSession().apply {
        sessionPreset = AVCaptureSessionPresetHigh
    }

    private val previewLayer: AVCaptureVideoPreviewLayer by lazy {
        requireNotNull(AVCaptureVideoPreviewLayer(session = session)).apply {
            videoGravity = AVLayerVideoGravityResizeAspectFill
        }
    }

    private var started = false
    private var configured = false

    init {
        layer.addSublayer(previewLayer)
    }

    override fun layoutSubviews() {
        super.layoutSubviews()
        previewLayer.frame = bounds
    }

    fun startIfNeeded() {
        if (started) return
        started = true

        AVCaptureDevice.requestAccessForMediaType(AVMediaTypeVideo) { granted ->
            if (!granted) return@requestAccessForMediaType

            dispatch_async(dispatch_get_main_queue()) {
                if (!configured) {
                    configured = true
                    setupInput()
                }
                session.startRunning()
            }
        }
    }

    private fun setupInput() {
        val device = AVCaptureDevice.defaultDeviceWithMediaType(AVMediaTypeVideo) ?: return
        val input = AVCaptureDeviceInput.deviceInputWithDevice(device, error = null) as? AVCaptureDeviceInput ?: return
        if (session.canAddInput(input)) session.addInput(input)
    }
}
