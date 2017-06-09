package com.andreabardella.aifaservicesconsumer.dto.mapper;

import com.andreabardella.aifaservicesconsumer.dto.DrugDto;
import com.andreabardella.aifaservicesconsumer.model.CompanyLight;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DrugDtoToCompanyLightMapper {

    public static CompanyLight map(DrugDto input) {
        CompanyLight output = null;
        if (input != null) {
            output = new CompanyLight();
            StringBuilder builder = new StringBuilder();
            boolean isFirstOccurrence = true;
            if (input.industryDescriptionList != null) {
                for (String industry : input.industryDescriptionList) {
                    builder.append(isFirstOccurrence ? industry : "; " + industry);
                    isFirstOccurrence = false;
                }
                output.setName(builder.toString());
            }
            if (input.industryCodeList != null) {
                output.setCode(input.industryCodeList.get(0));
            }
        }
        return output;
    }

    public static List<CompanyLight> map(Collection<DrugDto> inputs) {
        List<CompanyLight> outputs = new ArrayList<>();
        if (inputs != null && inputs.size() > 0) {
            for (DrugDto input : inputs) {
                CompanyLight output = map(input);
                outputs.add(output);
            }
        }
        return outputs;
    }
}
