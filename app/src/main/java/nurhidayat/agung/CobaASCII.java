package nurhidayat.agung;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public class CobaASCII extends AppCompatActivity {
    private EditText edtConvStA;
    private Button btnConvStA;
    private TextView tvASCII;
    private String temp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coba_ascii);
        edtConvStA = (EditText) findViewById(R.id.edt_stringtoascii);
        btnConvStA = (Button) findViewById(R.id.btn_convtoascii);
        tvASCII = (TextView) findViewById(R.id.tv_ascii);
        //byte[] b = s.getBytes(StandardCharsets.US_ASCII);
        //new String(myByteArray, "UTF-8");
        btnConvStA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                temp = edtConvStA.getText().toString();
                byte[] bit = temp.getBytes(StandardCharsets.US_ASCII);
//                System.out.println(bit);
//                try {
//                    temp = new String(bit,"UTF-8");
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
                tvASCII.setText(temp);
            }
        });


    }
}
