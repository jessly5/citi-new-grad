package com.citi.project.PortfolioProject.services;

import com.citi.project.PortfolioProject.entities.Accounts;
import com.citi.project.PortfolioProject.entities.History;
import com.citi.project.PortfolioProject.entities.Securities;
import com.citi.project.PortfolioProject.repos.AccountRepository;
import com.citi.project.PortfolioProject.repos.HistoryRepository;
import com.citi.project.PortfolioProject.repos.SecurityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

@Service
public class AccountsServiceImp implements AccountsService {

    @Autowired
    private AccountRepository repository;

    @Autowired
    private SecurityRepository securityRepository;
    
    @Autowired
    private HistoryRepository historyRepository;

    @Transactional(propagation= Propagation.REQUIRED)
    public Iterable<Accounts> getAccounts(){
        return repository.findAll();
    }

    @Transactional(propagation= Propagation.REQUIRED)
    public Accounts getAccountById(Integer id){
        Optional<Accounts> accountOptional =  repository.findById(id);
        if (accountOptional.isPresent()) {
            return accountOptional.get();
        }
        else return null;
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED)
    public Accounts getAccountByName(String name) {
       Accounts account =  repository.findByName(name);
       return account;
    }

    @Override
    public Accounts addNewAccount(Accounts account) {
        if(account.getAmount()==null){
            account.setAmount(0.0);
        }
        return repository.save(account);

    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED)
    public Iterable<Accounts> getAccountByType(String type) {
        return repository.findByType(type);
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED)
    public Securities addSecurity(Securities security) throws IOException {
        Stock stock = YahooFinance.get(security.getSymbol());
        security.setCurrent_cost(stock.getQuote().getPrice().doubleValue());
        security.setClosing_cost(stock.getQuote().getPreviousClose().doubleValue());

        Accounts account=getAccountById(security.getAccount_id());
        Accounts cashAccount=getAccountById(security.getCash_account_id());

        double money = security.getHoldings()*security.getCurrent_cost();

        if (cashAccount.getAmount() >= money){
            account.addSecurity(security);      //will add to existing secuirty if same symbol
            cashAccount = cashAccountChanges(cashAccount, -money);
            repository.save(cashAccount);
            History history = new History("investment", new Date(), money, account.getId());
            account.addHistory(history);
        } else {
            //in case of being poor
            // Assuming front end will implement a check
            return null;
        }
        calculateInvestmentSummary();
        repository.save(account);
        return security;
    }

    private Accounts cashAccountChanges(Accounts account, double changeInCash){
        account.setAmount(account.getAmount()+changeInCash);
        History h1 = new History("cash", new Date(), changeInCash, account.getId());
        account.addHistory(h1);
        return account;
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED)
    public void updateAccountCashAmount(Integer account_id, double changeInCash){
        Accounts account=getAccountById(account_id);
        account = cashAccountChanges(account, changeInCash);
        repository.save(account);
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED)
    public void removeAllSecurityBySymbol(String symbol, Integer invest_account_id, Integer cash_account_id) {
        Accounts account=getAccountById(invest_account_id);

        Iterable<Securities> securitiesToDelete = account.findBySymbol(symbol);
        double totalValue= account.removeSecurityBySymbol(symbol);
        account.updateAmount(-totalValue);
        History history = new History("investment", new Date(), -totalValue, account.getId());
        account.addHistory(history);
        repository.save(account);
        for(Securities s: securitiesToDelete){
            securityRepository.delete(s);
        }
        calculateInvestmentSummary();
        updateAccountCashAmount(cash_account_id, totalValue);

    }


    @Override
    @Transactional(propagation= Propagation.REQUIRED)
    public void removeSomeSecuritiesBySymbol(String symbol, Integer invest_account_id, Integer cash_account_id, int quantity) {
        Accounts account=getAccountById(invest_account_id);

        double totalValue = account.removeSecurityQuantityBySymbol(symbol, quantity);
        if(totalValue>0.0){
            account.updateAmount(-totalValue);
            History history = new History("investment", new Date(), -totalValue, account.getId());
            account.addHistory(history);
            repository.save(account);
            updateAccountCashAmount(invest_account_id, totalValue);
        }else if(totalValue == -1.0){     //chosen value is in fact all that is owned
            removeAllSecurityBySymbol(symbol, invest_account_id, cash_account_id);
        }else if(totalValue==0.0){
            // when trying to sell more than owned
            // Assuming that front end will handle this
        }
        calculateInvestmentSummary();

    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED)
    public void calculateInvestmentSummary() {
        Iterable<Accounts> accounts = getAccountByType("investment");
        for(Accounts acc: accounts){
            List<Securities> sec = acc.getSecuritiesList();
            double total = 0;
            for(Securities s: sec){
                total += s.getHoldings() * s.getCurrent_cost(); // to fix ????
            }
            acc.setAmount(total);
            repository.save(acc);
        }
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED)
    public Iterable<Accounts> updateAllSecurityInfo() {
        Iterable<Accounts> accounts = getAccountByType("investment");
        for(Accounts acc: accounts){
            List<Securities> sec = acc.getSecuritiesList();
            for(Securities s: sec) {
                try {
                    System.out.println(s.getSymbol());
                    Stock stock = YahooFinance.get(s.getSymbol());
                    s.setCurrent_cost(stock.getQuote().getPrice().doubleValue()) ;
                    s.setClosing_cost(stock.getQuote().getPreviousClose().doubleValue());
                } catch (IOException e) {
                    e.printStackTrace();
                }catch(NullPointerException e2){
                    e2.printStackTrace();
                }
            }
            repository.save(acc);
        }
        Double prev = summarizeInvsetments();

        calculateInvestmentSummary();

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        Date date = cal.getTime();
        Iterable<History> hist = historyRepository.findByTransactionDate(date);
        boolean exists = false;
        for(History h: hist){
            if(h.getAccountType().equals("investment")){
                exists = true;
            }
        }

        if(!exists){
            History h = new History("investment", date,summarizeInvsetments() - prev);
            historyRepository.save(h);
        }

        return accounts;
    }

    @Override
    public Iterable<Securities> getAllSecurities() {
        return securityRepository.findAll();
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED)
    public Double summarizeCash() {
        Iterable<Accounts> accounts= getAccountByType("cash");
        double cashAmount =0;
        for (Accounts acc: accounts){
            cashAmount+=acc.getAmount();
        }
        return cashAmount;
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED)
    public Double summarizeInvsetments(){
        Iterable<Accounts> accounts= getAccountByType("investment");
        double investmentAmount=0;
        for (Accounts acc: accounts){
            investmentAmount+=acc.getAmount();
        }
        return investmentAmount;
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED)
    public Double summarizeNetWorth(){
        return summarizeCash()+summarizeInvsetments();
    }

    @Override
    public Iterable<Securities> getSecuritiesInAccount(Integer id) {
        Accounts account = getAccountById(id);
        return account.getSecuritiesList();

    }

    private Calendar setHistoricDate(String type){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
        if(type.equals("year")){
            cal.add(Calendar.YEAR, -1);
        }else if(type.equals("month")){
            cal.add(Calendar.MONTH, -1);
        }else if(type.equals("week")){
            cal.add(Calendar.WEEK_OF_YEAR, -1);
        }
        return cal;
    }

    private ArrayList<Double> calculateChangesOverTime(Iterable<History> hist){
        Double cash=0.0;
        Double invest=0.0;
        for(History h: hist){
            if(h.getAccountType().equals("cash")){
                cash+=h.getAmount();
            }else{
                invest+=h.getAmount();
            }
        }
        ArrayList<Double> result = new ArrayList<>();
        result.add(cash);
        result.add(invest);
        return result;
    }


    @Override
    public Map<String, Double> getAllAccountChanges() {
        DecimalFormat df = new DecimalFormat("#.##");

        Double currentCash = this.summarizeCash();
        Double currentInvest = this.summarizeInvsetments();
        Map<String,Double> historicData=new HashMap<>();

        Calendar cal = setHistoricDate("year");
        Iterable<History> yearHist= historyRepository.findByTransactionDateIsAfter(cal.getTime());

        ArrayList<Double> year = calculateChangesOverTime(yearHist);
        historicData.put("Year Cash", Double.parseDouble(df.format(currentCash-year.get(0))));
        historicData.put("Year Investment", Double.parseDouble(df.format(currentInvest-year.get(1))));

        Calendar monthCal =setHistoricDate("month");

        Iterable<History> monthHist= historyRepository.findByTransactionDateIsAfter(monthCal.getTime());

        ArrayList<Double> month = calculateChangesOverTime(monthHist);
        historicData.put("Month Cash", Double.parseDouble(df.format(currentCash-month.get(0))));
        historicData.put("Month Investment", Double.parseDouble(df.format(currentInvest-month.get(1))));

        Calendar weekCal =  setHistoricDate("week");
        Iterable<History> weekHist= historyRepository.findByTransactionDateIsAfter(weekCal.getTime());

        ArrayList<Double> week = calculateChangesOverTime(weekHist);
        historicData.put("Week Cash", Double.parseDouble(df.format(currentCash-week.get(0))));
        historicData.put("Week Investment", Double.parseDouble(df.format(currentInvest-week.get(1))));
        return historicData;
    }

    @Override
    public Iterable<HistoryData> getInvestmentYearHistory() {
        Calendar cal =setHistoricDate("year");
//        DecimalFormat df = new DecimalFormat("#.##");

        Iterable<History> yearHist= historyRepository.findByAccountTypeAndTransactionDateIsAfter("investment", cal.getTime());

        List<History> result = new ArrayList<>();
        yearHist.forEach(result::add);
        Collections.sort(result, new Comparator<History>() {
            @Override
            public int compare(History c1, History c2) {
                if(c1.getTransactionDate().before(c2.getTransactionDate())){
                    return 1;
                }else {
                    return -1;
                }
            }
        });

        List<HistoryData> investment = new ArrayList<>();

        Double total = summarizeInvsetments();
        investment.add(new HistoryData(new Date(), total));
        investment.add(new HistoryData(result.get(0).getTransactionDate(), total-result.get(0).getAmount()));
        for(int i =1;i<result.size();i++){
            investment.add(new HistoryData(result.get(i).getTransactionDate(), investment.get(i).getValue()-result.get(i).getAmount()));
        }

        Collections.reverse(investment);
        return investment;
    }

    @Override
    public String deletAccount(Integer id) {
        Accounts account = repository.getById(id);
//        System.out.println(account.getSecuritiesList().isEmpty());
        if(!account.getSecuritiesList().isEmpty() && account.getSecuritiesList().size()>0){
            return "Can not remove account. Sell your securities first";
        }
        repository.delete(account);

        return "Account removed";
    }


    @Override
    public Iterable<Securities> getTopDailyPerformers() {
        List<Securities> allSecurities = sortByPercentChange();
        Collections.reverse(allSecurities);
        List<Securities> toReturn = new ArrayList<>();
        for(Securities s:allSecurities){
            if(toReturn.size()==5){
                break;
            }
            if(s.getCurrent_cost()>s.getClosing_cost()){
                toReturn.add(s);
            }
        }
        return toReturn;
    }

    @Override
    public Iterable<Securities> getWorstDailyPerformers() {
        List<Securities> allSecurities = sortByPercentChange();
        List<Securities> toReturn = new ArrayList<>();
        for(Securities s:allSecurities){
            if(toReturn.size()==5){
                break;
            }
            if(s.getCurrent_cost()<s.getClosing_cost()){
                toReturn.add(s);
            }
        }
        return toReturn;
    }

    private List<Securities> sortByPercentChange(){
        Iterable<Securities> allSecurities = getAllSecurities();
        List<Securities> result = new ArrayList<>();
        allSecurities.forEach(result::add);
        Collections.sort(result, new Comparator<Securities>() {
            @Override
            public int compare(Securities c1, Securities c2) {
                return Double.compare((c1.getCurrent_cost()-c1.getClosing_cost())/c1.getClosing_cost(), (c2.getCurrent_cost()-c2.getClosing_cost())/c2.getClosing_cost());
            }
        });
        return result;
    }

}
