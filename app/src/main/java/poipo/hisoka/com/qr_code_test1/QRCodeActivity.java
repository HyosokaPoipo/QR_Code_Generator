package poipo.hisoka.com.qr_code_test1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class QRCodeActivity extends AppCompatActivity {

    public final static int WHITE = 0xFFFFFFFF;
    public final static int BLACK = 0xFF000000;
    public final static int WIDTH = 400;
    public final static int HEIGHT = 400;
    public final static int REQUEST_BARCODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        final ImageView imageView = (ImageView) findViewById(R.id.imageView);
        final EditText myText = (EditText)findViewById(R.id.myText);
        Button gen = (Button) findViewById(R.id.button);
        gen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    Bitmap bitmap = generateQRCode(myText.getText().toString());
                    imageView.setImageBitmap(bitmap);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
            }
        });


        Button scan = (Button) findViewById(R.id.scanButton);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanQRCode();
                /*
                String packageString = "com.poipo.hisoka.com.qr_code_test1";
                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                //intent.setPackage(packageString);
                intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                startActivityForResult(intent, REQUEST_BARCODE);

                Toast toast = Toast.makeText(getApplicationContext(), "Start scanning QR code", Toast.LENGTH_SHORT);
                toast.show();
                //showDialog(QRCodeActivity.this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
                */
            }
        });

    }




    Bitmap generateQRCode(String str) throws WriterException {
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(str,
                    BarcodeFormat.QR_CODE, WIDTH, HEIGHT, null);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }

        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, w, 0, 0, w, h);
        return bitmap;
    }


    public void scanQRCode()
    {
        //https://github.com/DynamsoftRD/Android-Barcode-Reader/blob/master/src/com/dynamsoft/barcodereader/CameraPreview.java
        try {
            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
            startActivityForResult(intent, REQUEST_BARCODE);

            Toast toast = Toast.makeText(this, "Start scanning QR code", Toast.LENGTH_SHORT);
            toast.show();
        } catch (Exception e) {
            Uri marketUri = Uri.parse("market://details?id=com.google.zxing.client.android");
            Intent marketIntent = new Intent(Intent.ACTION_VIEW,marketUri);
            startActivity(marketIntent);
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = data.getStringExtra("SCAN_RESULT");
                Toast.makeText(getApplicationContext(),contents,Toast.LENGTH_SHORT).show();
            }
            if (resultCode == RESULT_CANCELED) {
                //handle cancel
            }
        }
    }
}
