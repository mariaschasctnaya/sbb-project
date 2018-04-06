package com.tsystems.train.validation.validator;


import com.tsystems.train.exception.ScheduleNotValidException;
import com.tsystems.train.exception.StationsScheduleHasWrongOrder;
import com.tsystems.train.facade.data.ScheduleData;
import com.tsystems.train.validation.constraint.ScheduleConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Map;
import java.util.Set;
//Defines the logic to validate constraint  @ScheduleConstraint
public class ScheduleValidator implements ConstraintValidator<ScheduleConstraint, Map<String, ScheduleData>> {
    @Override
    //Initializes the validator in preparation for isValid calls.
    public void initialize(ScheduleConstraint constraintAnnotation) {

    }
    //Implements the validation logic
    @Override
    public boolean isValid(Map<String, ScheduleData> value, ConstraintValidatorContext context) {
        Set<Map.Entry<String, ScheduleData>> entries = value.entrySet();
        Map.Entry<String, ScheduleData> prevSchedule = null;
        for (Map.Entry<String, ScheduleData> entry : entries) {
            if(prevSchedule == null) {
                prevSchedule = entry;
            }
            else {
                if(prevSchedule.getValue().getDeparture().isAfter(entry.getValue().getArrive()))
                    throw new StationsScheduleHasWrongOrder(prevSchedule.getKey(), entry.getKey());

            }
            if(entry.getValue().getDeparture().isBefore(entry.getValue().getArrive()))
                throw new ScheduleNotValidException(entry.getKey());
        }
        return true;
    }
}
