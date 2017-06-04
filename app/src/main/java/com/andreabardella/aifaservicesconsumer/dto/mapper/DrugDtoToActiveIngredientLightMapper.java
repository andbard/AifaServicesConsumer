package com.andreabardella.aifaservicesconsumer.dto.mapper;

import com.andreabardella.aifaservicesconsumer.dto.DrugDto;
import com.andreabardella.aifaservicesconsumer.model.ActiveIngredientLight;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DrugDtoToActiveIngredientLightMapper {

    public static ActiveIngredientLight map(DrugDto input) {
        ActiveIngredientLight output = null;
        if (input != null) {
            output = new ActiveIngredientLight();
            StringBuilder builder = new StringBuilder();
            boolean isFirstOccurrence = true;
            for (String activeIngredient : input.atcDescriptionList) {
                builder.append(isFirstOccurrence ? activeIngredient : "; " + activeIngredient);
                isFirstOccurrence = false;
            }
            output.setName(builder.toString());
        }
        return output;
    }

    public static List<ActiveIngredientLight> map(Collection<DrugDto> inputs) {
        List<ActiveIngredientLight> outputs = new ArrayList<>();
        if (inputs != null && inputs.size() > 0) {
            for (DrugDto input : inputs) {
                ActiveIngredientLight output = map(input);
                outputs.add(output);
            }
        }
        return outputs;
    }
}
