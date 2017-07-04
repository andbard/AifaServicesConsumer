package com.andreabardella.aifaservicesconsumer.dto.mapper;

import android.os.Build;
import android.text.Html;

import com.andreabardella.aifaservicesconsumer.dto.DrugDto;
import com.andreabardella.aifaservicesconsumer.model.CompanyLight;
import com.andreabardella.aifaservicesconsumer.model.DrugItem;
import com.andreabardella.aifaservicesconsumer.model.DrugStatus;
import com.andreabardella.aifaservicesconsumer.model.Packaging;
import com.andreabardella.aifaservicesconsumer.model.UnrecognizedDrugStatusException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import timber.log.Timber;

public class DrugDtoToDrugItemMapper {

    public static DrugItem map(List<DrugDto> inputs) {
        DrugItem output = null;
        if (inputs != null && inputs.size() > 0) {
            output = new DrugItem();
            List<Packaging> packagingList = new ArrayList<>();
            Set<String> nameSet = new HashSet<>();
            Set<CompanyLight> companySet = new HashSet<>();
            Set<String> activeIngredientSet = new HashSet<>();
            for (DrugDto input : inputs) {
                if (input.drugDescriptionList != null) {
                    for (String str : input.drugDescriptionList) {
                        if (!nameSet.contains(decodeHtml(str))) {
                            nameSet.add(decodeHtml(str));
                        }
                    }
                }
                if (input.companyDescriptionList != null) {
                    for (int i = 0; i<input.companyDescriptionList.size(); i++) {
                        CompanyLight company = new CompanyLight();
                        String str = decodeHtml(input.companyDescriptionList.get(i));
                        company.setName(str);
                        if (input.companyCodeList != null && input.companyCodeList.size() > i) {
                            company.setCode(input.companyCodeList.get(i));
                        }
                        if (!companySet.contains(company)) {
                            companySet.add(company);
                        }
                    }
                }
                if (input.atcDescriptionList != null) {
                    for (String str : input.atcDescriptionList) {
                        if (!activeIngredientSet.contains(decodeHtml(str))) {
                            activeIngredientSet.add(decodeHtml(str));
                        }
                    }
                }
                Packaging packaging = new Packaging();
                String description = "";
                if (input.bundleDescriptionList != null) {
                    for (String str : input.bundleDescriptionList) {
                        description += description.length() > 0 ? ", "+decodeHtml(str) : decodeHtml(str);
                    }
                }
                packaging.setDescription(description);
                packaging.setAic(input.aicList.get(0));
                try {
                    DrugStatus drugStatus = DrugStatus.getStatus(input.drugStateList.get(0));
                    switch (drugStatus) {
                        case AUTHORIZED:
                            packaging.setStatus("authorized");
                            break;
                        case RETIRED:
                            packaging.setStatus("retired");
                            break;
                        case SUSPENDED:
                            packaging.setStatus("suspended");
                            break;
                        default:
                            packaging.setStatus("unrecognized: " + input.drugStateList.get(0));
                    }
                } catch (UnrecognizedDrugStatusException e) {
                    packaging.setStatus("unrecognized: " + input.drugStateList.get(0));
                    Timber.w(e);

                }
                packagingList.add(packaging);
            }

            output.setAic(inputs.get(0).aicList.get(0).substring(0, 6));
            output.setFiUrl(inputs.get(0).linkFiList.get(0));
            output.setRcpUrl(inputs.get(0).linkRcpList.get(0));
            output.setNameSet(nameSet);
            output.setCompanySet(companySet);
            output.setActiveIngredientSet(activeIngredientSet);
            output.setPackagingList(packagingList);
        }
        return output;
    }

    private static String decodeHtml(String toDecode) {
        String str;
        if (Build.VERSION.SDK_INT >= 24) {
            str = Html.fromHtml(toDecode, Html.FROM_HTML_MODE_LEGACY).toString();
        } else {
            //noinspection deprecation
            str = Html.fromHtml(toDecode).toString();
        }
        return str;
    }
}
