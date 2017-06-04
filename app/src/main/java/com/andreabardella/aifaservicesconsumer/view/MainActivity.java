package com.andreabardella.aifaservicesconsumer.view;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.andreabardella.aifaservicesconsumer.R;
import com.andreabardella.aifaservicesconsumer.SearchType;
import com.andreabardella.aifaservicesconsumer.util.PermissionUtils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.Arrays;
import java.util.Collections;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

//    public static final String[] PERMISSION_TO_REQUEST = new String[] {
//            Manifest.permission.WRITE_EXTERNAL_STORAGE
//    };

    public static final String SEARCH_TYPE = "SEARCH_TYPE";
    public static final String SEARCH_BY = "SEARCH_BY";
    public static final String SEARCH_TEXT = "SEARCH_TEXT";
    public static final String SEARCH_INDUSTRY_CODE = "SEARCH_INDUSTRY_CODE";

    /*@BindView(R.id.main_activity_search_by_barcode)
    FontAwesomeTextView barcodeView;
    @BindView(R.id.main_activity_search_by_drug)
    FontAwesomeTextView drugView;
    @BindView(R.id.main_activity_search_by_active_ingredient)
    FontAwesomeTextView activeIngredientView;
    @BindView(R.id.main_activity_search_by_industry)
    FontAwesomeTextView industryView;*/

    @OnClick(R.id.main_activity_search_by_barcode)
    void searchByBarcode() {
        scanBarcode();
    }
    @OnClick(R.id.main_activity_search_by_drug)
    void searchByDrug() {
        searchBy(SearchType.DRUG, null);
    }
    @OnClick(R.id.main_activity_search_by_active_ingredient)
    void searchByActiveIngredient() {
        searchBy(SearchType.ACTIVE_INGREDIENT, null);
    }
    @OnClick(R.id.main_activity_search_by_industry)
    void searchByIndustry() {
        searchBy(SearchType.INDUSTRY, null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);

//        if (!PermissionUtils.hasPermissions(this, PERMISSION_TO_REQUEST)) {
//            PermissionUtils.requestPermissions(this, PERMISSION_TO_REQUEST);
//        }
    }

    private void searchBy(SearchType type, String text) {
        Intent intent;
        if (type == SearchType.AIC && text != null) {
            intent = new Intent(this, DrugActivity.class);
            intent.putExtra(SEARCH_TEXT, text);
        } else {
            intent = new Intent(this, SearchActivity.class);
            intent.putExtra(SEARCH_TYPE, type); // enum is Serializable
        }
        startActivity(intent);
    }

    private void scanBarcode() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        String[] values = new String[]{BarcodeFormat.CODE_39.name()};
        integrator.setDesiredBarcodeFormats(Collections.unmodifiableList(Arrays.asList(values)));
        integrator.setPrompt("Scan a CODE 39 barcode");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            String reading = result.getContents();
            if (reading != null) {
                searchBy(SearchType.AIC, reading.substring(0, 6));
            } else {
                Toast.makeText(MainActivity.this, "No reading performed", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(MainActivity.this, "Something went wrong!\nUnable to read barcode!", Toast.LENGTH_LONG).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
