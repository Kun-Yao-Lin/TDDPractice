package budget;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import budget.Budget;
import budget.BudgetService;
import budget.IBudgetRepo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

public class BudgetServiceTest {
    private IBudgetRepo repo;
    private BudgetService service;

    @Before
    public void before() {
        repo = Mockito.mock(IBudgetRepo.class);
        service = new BudgetService(repo);

        List<Budget> budgets = new ArrayList<>();
        budgets.add(new Budget("202111", 300));
        budgets.add(new Budget("202112", 3100));
        budgets.add(new Budget("202201", 310));
        budgets.add(new Budget("202202",2800));
        budgets.add(new Budget("202203", 31));
        budgets.add(new Budget("202204", 300));
        budgets.add(new Budget("202207", 31));
        budgets.add(new Budget("202210", 31));
        budgets.add(new Budget("202211", 30));
        budgets.add(new Budget("202212", 31));
        budgets.add(new Budget("202301", 31));

        when(repo.getAll()).thenReturn(
                budgets
        );
    }

    @Test
    public void same_day() {
        LocalDate sd = LocalDate.of(2022,1,1);
        LocalDate ed = LocalDate.of(2022,1,1);
      double amount =  service.query(sd, ed);
        Assert.assertEquals(10, amount, 0.00);
    }

    @Test
    public void same_Month_dif_day() {
        LocalDate sd = LocalDate.of(2022,1,1);
        LocalDate ed = LocalDate.of(2022,1,2);
        double amount =  service.query(sd, ed);
        Assert.assertEquals(20, amount, 0.00);
    }

    @Test
    public void same_Month_all_day() {
        LocalDate sd = LocalDate.of(2022,1,1);
        LocalDate ed = LocalDate.of(2022,1,31);
        double amount =  service.query(sd, ed);
        Assert.assertEquals(310, amount, 0.00);
    }

    @Test
    public void dif_Month() {
        LocalDate sd = LocalDate.of(2022,1,31);
        LocalDate ed = LocalDate.of(2022,2,2);
        double amount =  service.query(sd, ed);
        Assert.assertEquals(10+200, amount, 0.00);
    }

    @Test
    public void dif_Year() {
        LocalDate sd = LocalDate.of(2021,11,29);
        LocalDate ed = LocalDate.of(2022,3,2);
        double amount =  service.query(sd, ed);
        double expected = 20 + 3100 + 310 + 2800 + 2;
        Assert.assertEquals(expected , amount, 0.00);
    }

    @Test
    public void dif_Year_2() {
        LocalDate sd = LocalDate.of(2021,11,29);
        LocalDate ed = LocalDate.of(2023,1,2);
        double amount =  service.query(sd, ed);
        double expected = 20 + 3100 + 310 + 2800 + 31 + 300 + 31 + 31 + 30 + 31 + 2;
        Assert.assertEquals(expected , amount, 0.00);
    }
}
