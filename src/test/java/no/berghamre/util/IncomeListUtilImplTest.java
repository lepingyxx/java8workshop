package no.berghamre.util;

import no.berghamre.DataReader;
import no.berghamre.data.Gender;
import no.berghamre.data.IncomeStatistics;
import no.berghamre.data.IncomeYear;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;

public class IncomeListUtilImplTest {

    private IncomeListUtil ilu;
    private List<IncomeStatistics> statistics;

    @Before
    public void setUp() throws Exception {
        ilu = new no.berghamre.util.fasit.IncomeListUtilImpl();
        statistics = ilu.decodeLinesUsingForEach(DataReader.readDataStripHeader());
    }

    // TODO add tests that show the danger with these methods mutating all contained collections

    @Test
    public void testDecodeLinesUsingForEach() throws Exception {
        Assert.assertThat("Statistics contains 38 elements", statistics.size(), is(38));
        Assert.assertThat("Third element is from Akershus", statistics.get(2).county, is("Akershus"));
        Assert.assertThat("Gender of first element is male", statistics.get(0).sex, is(Gender.male));
        Assert.assertThat("Last element is from Finnmark", statistics.get(37).county, is("Finnmark"));
        Assert.assertThat("Gender of last element is female", statistics.get(37).sex, is(Gender.female));
        Assert.assertThat("Income for Vestfold, female, 1997 is 132400", statistics.get(13).incomeYears.get(0).averageIncome, is(132400));
        Assert.assertThat("Income for Vestfold, female, 2011 is 281900", statistics.get(13).incomeYears.get(14).averageIncome, is(281900));
    }

    @Test
    public void testGetStatisticsForGender() throws Exception {
        ArrayList<IncomeStatistics> copy = new ArrayList<>(statistics);
        List<IncomeStatistics> male = ilu.getStatisticsForGender(copy, Gender.male);

        Assert.assertThat("male contains 19 elements", male.size(), is(19));
        male.forEach((IncomeStatistics is) -> {
            if(is.sex == Gender.female){
                throw new RuntimeException("Gender was female");
            }
        });
    }

    @Test
    public void testGetStatisticsForYearsBefore() throws Exception {
        ArrayList<IncomeStatistics> copy = new ArrayList<>(statistics);
        List<IncomeStatistics> before = ilu.getStatisticsForYearsBefore(copy, 2003);

        Assert.assertThat("before contains 38 elements", before.size(), is(38));
        before.forEach((IncomeStatistics is) -> {
            is.incomeYears.forEach((IncomeYear iy) -> {
                if(iy.year >= 2003){
                    throw new RuntimeException("Year was not less than 2003");
                }
            });
        });
    }

    @Test
    public void testGetStatisticsForYearsAfter() throws Exception {
        ArrayList<IncomeStatistics> copy = new ArrayList<>(statistics);
        List<IncomeStatistics> after = ilu.getStatisticsForYearsAfter(copy, 2004);

        Assert.assertThat("after contains 38 elements", after.size(), is(38));
        after.forEach((IncomeStatistics is) -> {
            is.incomeYears.forEach((IncomeYear iy) -> {
                if (iy.year <= 2004) {
                    throw new RuntimeException("Year was not greater than 2004");
                }
            });
        });
    }

    @Test
    public void testGetStatisticsForIncomeLessThan() throws Exception {
        ArrayList<IncomeStatistics> copy = new ArrayList<>(statistics);
        List<IncomeStatistics> less = ilu.getStatisticsForIncomeLessThan(copy, 200000);

        Assert.assertThat("less contains 38 elements", less.size(), is(38));
        less.forEach((IncomeStatistics is) -> {
            is.incomeYears.forEach((IncomeYear iy) -> {
                if (iy.averageIncome >= 200000) {
                    throw new RuntimeException("averageIncome was not less than 200000");
                }
            });
        });
    }

    @Test
    public void testGetStatisticsForIncomeMoreThan() throws Exception {
        ArrayList<IncomeStatistics> copy = new ArrayList<>(statistics);
        List<IncomeStatistics> more = ilu.getStatisticsForIncomeMoreThan(copy, 200000);

        Assert.assertThat("more contains 38 elements", more.size(), is(38));
        more.forEach((IncomeStatistics is) -> {
            is.incomeYears.forEach((IncomeYear iy) -> {
                if (iy.averageIncome <= 200000) {
                    throw new RuntimeException("averageIncome was not more than 200000");
                }
            });
        });
    }
}
