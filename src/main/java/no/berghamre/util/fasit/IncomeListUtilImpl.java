package no.berghamre.util.fasit;

import no.berghamre.data.Gender;
import no.berghamre.data.IncomeStatistics;
import no.berghamre.data.IncomeYear;
import no.berghamre.util.IncomeListUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class IncomeListUtilImpl implements IncomeListUtil{
    @Override
    public List<IncomeStatistics> decodeLinesUsingForEach(List<String> lines) {
        final ArrayList<IncomeStatistics> retval = new ArrayList<>();
        lines.forEach(line -> {
            List<String> strings = Arrays.asList(line.split(","));
            final IncomeStatistics is = new IncomeStatistics(strings.get(0), Gender.valueOf(strings.get(1)));
            List<String> years = strings.subList(2, strings.size());
            for (int i = 0; i < years.size(); i++) {
                is.incomeYears.add(new IncomeYear(1997 + i, Integer.parseInt(years.get(i))));
            }
            retval.add(is);
        });
        return retval;
    }

    @Override
    public List<IncomeStatistics> getStatisticsForGender(List<IncomeStatistics> stats, Gender gender) {
        stats.removeIf(is -> is.sex != gender);
        return stats;
    }

    @Override
    public List<IncomeStatistics> getStatisticsForYearsBefore(List<IncomeStatistics> stats, int year) {
        stats.forEach(is -> {
            is.incomeYears.removeIf(iy -> iy.year >= year);
        });
        return stats;
    }

    @Override
    public List<IncomeStatistics> getStatisticsForYearsAfter(List<IncomeStatistics> stats, int year) {
        stats.forEach(is -> {
            is.incomeYears.removeIf(iy -> iy.year <= year);
        });
        return stats;
    }

    @Override
    public List<IncomeStatistics> getStatisticsForIncomeLessThan(List<IncomeStatistics> stats, int income) {
        stats.forEach(is -> {
            is.incomeYears.removeIf(iy -> iy.averageIncome >= income);
        });
        return stats;
    }

    @Override
    public List<IncomeStatistics> getStatisticsForIncomeMoreThan(List<IncomeStatistics> stats, int income) {
        stats.forEach(is -> {
            is.incomeYears.removeIf(iy -> iy.averageIncome <= income);
        });
        return stats;
    }
}
