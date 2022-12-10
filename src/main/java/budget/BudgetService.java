package budget;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;

public class BudgetService {

    private final IBudgetRepo repo;

    public BudgetService(IBudgetRepo repo) {
        this.repo = repo;
    }

    public double query(LocalDate start, LocalDate end) {
        if (start.isAfter(end)) {
            return 0;
        }

        List<Budget> budgetList = repo.getAll();
        Map<String, Budget> map = new HashMap<>();
        budgetList.forEach(new Consumer<Budget>() {
            @Override
            public void accept(Budget budget) {
                map.put(budget.getYearMonth(), budget);
            }
        });

        int startYear = start.getYear();
        int endYear = end.getYear();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMM");

        int deltaYear = endYear - startYear;

        if (deltaYear == 0) {
            if (start.getMonth() == end.getMonth()) {
                int day = end.getDayOfMonth() - start.getDayOfMonth() + 1;

                String key1 = String.format("%04d", start.getYear());
                String key2 = String.format("%02d", start.getMonthValue());
                Budget budget = map.get(key1 + key2);
                if (budget == null) {
                    return 0;
                } else {
                    int dayAmount = budget.getAmount() / YearMonth.of(start.getYear(), start.getMonth()).lengthOfMonth();
                    return dayAmount * day;
                }
            } else {

                int tempBudget = 0;

                YearMonth startMonth = YearMonth.of(start.getYear(), start.getMonth());
                int starDay = startMonth.lengthOfMonth() - start.getDayOfMonth() +1 ;
                String key1 = String.format("%04d", start.getYear());
                String key2 = String.format("%02d", start.getMonthValue());
                Budget startBudget = map.get(key1 + key2);

                if (startBudget != null) {
                    tempBudget += (startBudget.getAmount() / startMonth.lengthOfMonth()) * starDay;
                }

                YearMonth endMonth = YearMonth.of(end.getYear(), end.getMonth());
                int endDay = end.getDayOfMonth();
                key1 = String.format("%04d", end.getYear());
                key2 = String.format("%02d", end.getMonthValue());
                Budget endBudget = map.get(key1 + key2);

                if (endBudget != null) {
                    tempBudget += (endBudget.getAmount() / endMonth.lengthOfMonth()) * endDay;
                }

                for (int i = start.getMonthValue() + 1; i < end.getMonthValue(); i++) {

                    YearMonth yearMonth = YearMonth.of(start.getYear(), i);

                    key1 = String.format("%04d", yearMonth.getYear());
                    key2 = String.format("%02d", yearMonth.getMonthValue());
                    Budget budget = map.get(key1 + key2);

                    if (budget != null) {
                        tempBudget += budget.getAmount();
                    }
                }


                return tempBudget;
            }
        }

        int tempBudget = 0;

        YearMonth startMonth = YearMonth.of(start.getYear(), start.getMonth());
        int starDay = startMonth.lengthOfMonth() - start.getDayOfMonth() +1 ;
        String key1 = String.format("%04d", start.getYear());
        String key2 = String.format("%02d", start.getMonthValue());
        Budget startBudget = map.get(key1 + key2);

        if (startBudget != null) {
            tempBudget += (startBudget.getAmount() / startMonth.lengthOfMonth()) * starDay;
        }

        YearMonth endMonth = YearMonth.of(end.getYear(), end.getMonth());
        int endDay = end.getDayOfMonth();
        key1 = String.format("%04d", end.getYear());
        key2 = String.format("%02d", end.getMonthValue());
        Budget endBudget = map.get(key1 + key2);

        if (endBudget != null) {
            tempBudget += (endBudget.getAmount() / endMonth.lengthOfMonth()) * endDay;
        }

        for (int i = start.getMonthValue() + 1; i <= 12; i++) {

            YearMonth yearMonth = YearMonth.of(start.getYear(), i);

            key1 = String.format("%04d", yearMonth.getYear());
            key2 = String.format("%02d", yearMonth.getMonthValue());
            Budget budget = map.get(key1 + key2);

            if (budget != null) {
                tempBudget += budget.getAmount();
            }
        }

        for (int i = 1; i < end.getMonthValue(); i++) {

            YearMonth yearMonth = YearMonth.of(end.getYear(), i);

            key1 = String.format("%04d", yearMonth.getYear());
            key2 = String.format("%02d", yearMonth.getMonthValue());
            Budget budget = map.get(key1 + key2);

            if (budget != null) {
                tempBudget += budget.getAmount();
            }
        }

        for(int year = startYear + 1; year < endYear; year++){
            for (int month = 1 ; month <= 12 ; month ++){
                key1 = String.format("%04d", year);
                key2 = String.format("%02d", month);
                Budget budget = map.get(key1 + key2);
                if (budget != null) {
                    tempBudget += budget.getAmount();
                }
            }
        }

        return tempBudget;
    }

}
