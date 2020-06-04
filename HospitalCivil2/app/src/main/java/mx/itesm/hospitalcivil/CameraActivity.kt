package mx.itesm.hospitalcivil

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.util.SparseArray
import android.view.*
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import kotlinx.android.synthetic.main.activity_camera.*
import kotlinx.android.synthetic.main.activity_main.*

class CameraActivity : Fragment() {

    var cameraSource: CameraSource? = null
    var barCodeDetector: BarcodeDetector? = null
    var cameraView: SurfaceView? = null
    var resultScan = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_camera,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        barCodeDetector = BarcodeDetector.Builder(activity).setBarcodeFormats(Barcode.QR_CODE).build()

        cameraSource = CameraSource.Builder(activity, barCodeDetector).setRequestedPreviewSize(640, 480).build()
        cameraView = view.findViewById(R.id.cameraView)

        scanContinue.setTextColor(Color.GRAY)
        scanContinue.setOnClickListener {
            cameraSource!!.stop()
//            val transaction = activity?.supportFragmentManager?.beginTransaction()
            var newIntent = EditAppointmentFragment()
            var bun = Bundle()
            bun.putString("scan",resultScan)
            newIntent.arguments = bun
            (activity as MainActivity).replaceFragments(newIntent, "camera", null)
        }

        scanCancel.setOnClickListener {
            (activity as MainActivity).goBack()
        }

        cameraView!!.holder.addCallback(object: SurfaceHolder.Callback{
            override fun surfaceCreated(holder: SurfaceHolder?) {
                if(ActivityCompat.checkSelfPermission(view.context, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(activity as Activity, arrayOf(Manifest.permission.CAMERA), MY_CAMERA_PERMISSION_CODE)
                    surfaceCreated(holder)// To call the function again with permissions granted
                    return
                }
                cameraSource!!.start(holder)
            }

            override fun surfaceChanged(
                    holder: SurfaceHolder?,
                    format: Int,
                    width: Int,
                    height: Int
            ) {
                //Nothing to do here by now
            }

            override fun surfaceDestroyed(holder: SurfaceHolder?) {
                cameraSource!!.stop()
            }
        })



        barCodeDetector!!.setProcessor(object: Detector.Processor<Barcode>{

            override fun release() {

            }

            override fun receiveDetections(p0: Detector.Detections<Barcode>?) {
                var qrCodes: SparseArray<Barcode> = p0!!.detectedItems

                if(qrCodes.size()!=0)
                {

                    text_obtained.post{
                        text_obtained.setText("")
                        resultScan = qrCodes.valueAt(0).displayValue
                        var fields = resultScan.split("/")
                        if(fields.size == 5) {
                            var res = ""
                            res += fields[0] + "\n"
                            res += fields[1] + "\n"
                            //res += getString(R.string.qr_scan_age) + " " + fields[2] + "\n"
                            //res += getString(R.string.qr_scan_gender) + " " + fields[3] + "\n"
                            //res += getString(R.string.qr_scan_allergies) + " " + fields[4] + "\n"

                            text_obtained.setText(res)
                            scanContinue.isEnabled = true
                            scanContinue.setTextColor(ContextCompat.getColor(view.context,R.color.hospitalAzul))
                        } else{
                            text_obtained.setText(R.string.qr_scan_error)
                            scanContinue.isEnabled = false
                            scanContinue.setTextColor(Color.GRAY)
                        }
                        //text_obtained.setText(qrCodes.valueAt(0).displayValue)
                    }
                }
            }
        })

    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        cameraView!!.holder.addCallback(object: SurfaceHolder.Callback{
            override fun surfaceCreated(holder: SurfaceHolder?) {
                cameraSource!!.start(holder)
            }

            override fun surfaceChanged(
                    holder: SurfaceHolder?,
                    format: Int,
                    width: Int,
                    height: Int
            ) {
                //Nothing to do here by now
            }

            override fun surfaceDestroyed(holder: SurfaceHolder?) {
                cameraSource!!.stop()
            }
        })

    }
    companion object {
        private const val MY_CAMERA_PERMISSION_CODE = 100
    }
}