import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class UnauthorizedDeliveries {

    @SneakyThrows
    public static Map<String, Login> readLogins(String path) {
        BufferedReader br = new BufferedReader(new FileReader(path));
        System.out.println(br.readLine());
        HashMap<String, Login> dataProvider = new HashMap<>();
        String line;
        String[] dataRow;

        while ((line = br.readLine()) != null) {
            dataRow = line.split("[,]");
            dataProvider.put(dataRow[1].trim(), new Login(dataRow[0].trim(), dataRow[1].trim(),
                    Boolean.parseBoolean(dataRow[2].trim().toLowerCase()), dataRow[3].trim(), dataRow[4].trim()));
            System.out.println(dataProvider.get(dataRow[1].trim()));
        }

        return dataProvider;
    }

    @SneakyThrows
    public static Map<Long, Posting> readPostings(String path, Map<String, Login> authorizedUsers) {
        BufferedReader br = new BufferedReader(new FileReader(path));
        System.out.println(br.readLine());
        HashMap<Long, Posting> dataProvider = new HashMap<>();
        String line;
        String[] dataRow;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        NumberFormat doubleFormat = NumberFormat.getInstance(Locale.FRANCE);

        while ((line = br.readLine()) != null) {
            dataRow = line.split("[;]");
            if (dataRow.length == 10) {
                dataProvider.put(Long.parseLong(dataRow[0].trim()),
                        new Posting(Long.parseLong(dataRow[0].trim()),
                                Integer.parseInt(dataRow[1].trim()),
                                LocalDate.parse(dataRow[2].trim(), formatter).atStartOfDay(ZoneId.systemDefault()).toInstant(),
                                LocalDate.parse(dataRow[3].trim(), formatter).atStartOfDay(ZoneId.systemDefault()).toInstant(),
                                dataRow[4].trim(),
                                Integer.parseInt(dataRow[5].trim()),
                                dataRow[6].trim(),
                                doubleFormat.parse(dataRow[7].trim()).doubleValue(),
                                dataRow[8].trim(),
                                dataRow[9].trim(),
                                authorizedUsers.containsKey(dataRow[9].trim()) && authorizedUsers.get(dataRow[9].trim()).isActive()));
                System.out.println(dataProvider.get(Long.parseLong(dataRow[0].trim())));
            }
        }

        return dataProvider;
    }
}
