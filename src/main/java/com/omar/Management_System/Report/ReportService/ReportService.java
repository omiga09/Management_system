package com.omar.Management_System.Report.ReportService;
import com.omar.Management_System.Commission.Commission.Commission;
import com.omar.Management_System.Property.Property.Property;
import com.omar.Management_System.Property.PropertyRepository.PropertyRepository;
import com.omar.Management_System.Report.ReportController.PropertyPerformanceDTO;
import com.omar.Management_System.Reservation.ReservationDto.ReservationDto;
import com.omar.Management_System.Reservation.ReservationRepository.ReservationRepository;
import com.omar.Management_System.Transaction.Transaction.Transaction;
import com.omar.Management_System.Transaction.TransactionRepository.TransactionRepository;
import com.omar.Management_System.User.User.User;
import com.omar.Management_System.User.UsetrRepository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ReportService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReservationRepository reservationRepository;


    public BigDecimal getTotalSales() {
        return transactionRepository.findAll().stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public int getTransactionCount() {

        return transactionRepository.findAll().size();
    }

    public BigDecimal getMonthlyCommission(int year, int month) {
        YearMonth targetMonth = YearMonth.of(year, month);
        LocalDate startDate = targetMonth.atDay(1);
        LocalDate endDate = targetMonth.atEndOfMonth();

        List<Transaction> monthlyTransactions = transactionRepository.findByTransactionDateBetween(startDate, endDate);

        return monthlyTransactions.stream()
                .map(Transaction::getCommission)
                .filter(c -> c != null && c.getCalculatedDate() != null &&
                        c.getCalculatedDate().getYear() == year &&
                        c.getCalculatedDate().getMonthValue() == month)
                .map(Commission::getAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public List<User> getUserActivityReport() {
        return userRepository.findAll();
    }

}

