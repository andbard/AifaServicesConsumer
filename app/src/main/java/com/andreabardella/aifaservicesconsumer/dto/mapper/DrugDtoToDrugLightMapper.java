package com.andreabardella.aifaservicesconsumer.dto.mapper;

import android.os.Build;
import android.text.Html;

import com.andreabardella.aifaservicesconsumer.dto.DrugDto;
import com.andreabardella.aifaservicesconsumer.model.DrugLight;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DrugDtoToDrugLightMapper {

    public static DrugLight map(DrugDto input) {
        DrugLight output = null;
        if (input != null) {
            output = new DrugLight();
            StringBuilder builder = new StringBuilder();
            boolean isFirstOccurrence = true;
            for (String drug : input.drugDescriptionList) {
                String str;
                if (Build.VERSION.SDK_INT >= 24) {
                    str = Html.fromHtml(drug, Html.FROM_HTML_MODE_LEGACY).toString();
                } else {
                    //noinspection deprecation
                    str = Html.fromHtml(drug).toString();
                }
                str = isFirstOccurrence ? str : "; " + str;
                builder.append(str);
                isFirstOccurrence = false;
            }
            output.setName(builder.toString());
            //
            output.setCode(input.drugCodeList.get(0));
            //
            builder = new StringBuilder();
            isFirstOccurrence = true;
            for (String industry : input.industryDescriptionList) {
                String str;
                if (Build.VERSION.SDK_INT >= 24) {
                    str = Html.fromHtml(industry, Html.FROM_HTML_MODE_LEGACY).toString();
                } else {
                    //noinspection deprecation
                    str = Html.fromHtml(industry).toString();
                }
                str = isFirstOccurrence ? str : "; " + str;
                builder.append(str);
                isFirstOccurrence = false;
            }
            output.setIndustry(builder.toString());
        }
        return output;
    }

    public static List<DrugLight> map(Collection<DrugDto> inputs) {
        List<DrugLight> outputs = new ArrayList<>();
        if (inputs != null && inputs.size() > 0) {
            for (DrugDto input : inputs) {
                DrugLight output = map(input);
                outputs.add(output);
            }
        }
        return outputs;
    }
}
