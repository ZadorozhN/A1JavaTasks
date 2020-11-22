import javax.xml.crypto.Data;
import java.util.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        //first task
        System.out.println(Integer.toUnsignedString(IpParser.ipToIntParser("128.32.10.0")));
        System.out.println(IpParser.intToIpParser(2149583360L));

        //second task
        for (int i = 1; i < 10; i++) {
            System.out.println(ZeroOrInfinity.zeroOrInfinityFunction(i));
        }

        //third task
        Map<String, Login> authorizedUsers = UnauthorizedDeliveries.readLogins("D:/Downloads/logins.csv");
        Map<Long, Posting> postings = UnauthorizedDeliveries.readPostings("D:/Downloads/postings.csv", authorizedUsers);

        DataProvider dataProvider = new DataProvider();
        dataProvider.dropLoginTable();
        dataProvider.dropPostingTable();
        dataProvider.createLoginTable();
        dataProvider.createPostingTable();
        authorizedUsers.values().forEach(dataProvider::saveLogin);
        postings.values().forEach(dataProvider::savePosting);

        dataProvider.getAllPostings();
        dataProvider.getAllLogins();
        System.out.println(dataProvider.getLoginById(dataProvider.getAllLogins().get(0).getAppAccountName()));
        System.out.println(dataProvider.getPostingById(dataProvider.getAllPostings().get(0).getMatDoc()));
        System.out.println(dataProvider.getPostingsByPeriod("03.08.2020", "05.08.2020", false));
        System.out.println(dataProvider.getPostingsByPeriod("03.08.2020", "05.08.2020", true));
    }
}
